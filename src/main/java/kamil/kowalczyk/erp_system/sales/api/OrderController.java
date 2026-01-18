package kamil.kowalczyk.erp_system.sales.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.sales.domain.order.OrderService;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.CreateOrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/sales/orders")
@Tag(name = "Sprzedaż - Zamówienia", description = "Składanie zamówień i obsługa procesu sprzedaży")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Złóż nowe zamówienie", description = "Przyjmuje listę produktów i ilości. Automatycznie zdejmuje towar ze stanu magazynowego.")
    ResponseEntity<Long> createOrder(@RequestBody @Valid CreateOrderDto dto) {
        Long orderId = orderService.placeOrder(dto);
        return ResponseEntity
                .created(URI.create("/api/sales/orders/" + orderId))
                .body(orderId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz zamówienie", description = "Pobiera wartości całego zamówienia razem z ich ceną całkowitą oraz produktami.")
    ResponseEntity<OrderDto> getOneOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

}
