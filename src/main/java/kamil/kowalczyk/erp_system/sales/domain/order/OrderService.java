package kamil.kowalczyk.erp_system.sales.domain.order;

import kamil.kowalczyk.erp_system.inventory.domain.product.ProductService;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.ProductDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.CreateOrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.CreateOrderItemDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.OrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.OrderItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Transactional
    public Long placeOrder(CreateOrderDto dto) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        for (CreateOrderItemDto itemDto : dto.items()) {
            ProductDto product = productService.getProduct(itemDto.prdouctId());

            productService.updateStock(product.id(), -itemDto.quantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.id());
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(product.price());

            order.addItem(orderItem);
        }
        return orderRepository.save(order).getId();
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamówienie"));

        List<OrderItemDto> itemsDtos = order.getItems().stream()
                .map(item -> {
                    BigDecimal calculatedTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new OrderItemDto(
                            item.getProductId(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            calculatedTotalPrice
                    );
                })
                .toList();

        BigDecimal finalOrderValue = itemsDtos.stream()
                .map(OrderItemDto::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderDto(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                itemsDtos,
                finalOrderValue
        );
    }

}
