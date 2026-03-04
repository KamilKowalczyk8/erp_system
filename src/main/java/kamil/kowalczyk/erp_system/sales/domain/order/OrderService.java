package kamil.kowalczyk.erp_system.sales.domain.order;

import jakarta.persistence.EntityNotFoundException;
import kamil.kowalczyk.erp_system.client.domain.Client;
import kamil.kowalczyk.erp_system.client.domain.ClientService;
import kamil.kowalczyk.erp_system.inventory.domain.product.ProductService;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.ProductDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.*;
import kamil.kowalczyk.erp_system.user.domain.User;
import kamil.kowalczyk.erp_system.user.domain.UserRepository;
import kamil.kowalczyk.erp_system.user.domain.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ClientService clientService;
    private final UserService userService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductService productService, ClientService clientService, UserService userService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.clientService = clientService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Long placeOrder(String email, CreateOrderDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));
        Client client = clientService.getClientByUserId(user.getId());

        Order order = new Order(client, OrderStatus.NEW);

        for (CreateOrderItemDto itemDto : dto.items()) {
           ProductDto product = productService.decreaseStock(itemDto.productId(), itemDto.quantity());

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
                .orElseThrow(() -> new RuntimeException("Zamówienie nie istnieje"));

        return mapToDto(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);

        return orders.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByClient(Long clientId) {
        clientService.getClient(clientId);

        return orderRepository.findAllByClientId(clientId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SalesReportDto getSalesReport() {
        List<Order> allOrders = orderRepository.findAll();

        long totalOrders = allOrders.size();

        BigDecimal totalRevenue = allOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(orderItem -> orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SalesReportDto(totalOrders, totalRevenue);
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

    private OrderDto mapToDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
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

        BigDecimal finalOrderValue = itemDtos.stream()
                .map(OrderItemDto::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderDto(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getClient().getId(),
                order.getClient().getFirstName() + " " + order.getClient().getLastName(),
                itemDtos,
                finalOrderValue
        );
    }
}
