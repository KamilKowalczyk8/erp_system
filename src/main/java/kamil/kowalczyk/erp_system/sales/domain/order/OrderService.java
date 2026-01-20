package kamil.kowalczyk.erp_system.sales.domain.order;

import kamil.kowalczyk.erp_system.inventory.domain.product.ProductService;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.ProductDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.CreateOrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.CreateOrderItemDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.OrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PageableArgumentResolver pageableArgumentResolver;

    public OrderService(OrderRepository orderRepository, ProductService productService, PageableArgumentResolver pageableArgumentResolver) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.pageableArgumentResolver = pageableArgumentResolver;
    }

    public Long placeOrder(CreateOrderDto dto) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        for (CreateOrderItemDto itemDto : dto.items()) {
            ProductDto product = productService.getProduct(itemDto.productId());

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

    public void updateOrderStatus(Long orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Zamówienie nie istnieje"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new RuntimeException("Nie można zmienić statusu zamówienia, które zostało już zakończone/anulowane.");
        }

        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                productService.updateStock(item.getProductId(), item.getQuantity());
            }
        }
        order.setStatus(newStatus);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);


        return orders.map(order -> {
            List<OrderItemDto> itemDtos = order.getItems().stream()
                    .map(item -> new OrderItemDto(
                            item.getProductId(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    ))
                    .toList();

            BigDecimal totalValue = itemDtos.stream()
                    .map(OrderItemDto::totalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new OrderDto(
                    order.getId(),
                    order.getCreatedAt(),
                    order.getStatus(),
                    itemDtos,
                    totalValue
            );
        });
    }
}
