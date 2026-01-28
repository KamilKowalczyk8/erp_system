package kamil.kowalczyk.erp_system.sales.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.sales.domain.order.OrderService;
import kamil.kowalczyk.erp_system.sales.domain.order.OrderStatus;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.CreateOrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.OrderDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.SalesReportDto;
import kamil.kowalczyk.erp_system.sales.domain.order.dto.UpdateOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    @Operation(summary = "Pobieram liste wszystkich zamówień", description = "Ukazuje liste wszystkich zamówień")
    ResponseEntity<Page<OrderDto>> getAllOrders(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Pobieram zamówienia konkretnego klienta", description = "Pokazuje wszystkie zamówienia użytkownika")
    ResponseEntity<List<OrderDto>> getAllOrdersByClientId(
            @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(orderService.getOrdersByClient(clientId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz zamówienie", description = "Pobiera wartości całego zamówienia razem z ich ceną całkowitą oraz produktami.")
    ResponseEntity<OrderDto> getOneOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/report")
    @Operation(summary = "Pobranie raportu finasowego za zamówienia", description = "Tworzy raport z ilością zamówień oraz kwotą zsumowaną za nie")
    ResponseEntity<SalesReportDto> getSalesReport() {
        return ResponseEntity.ok(orderService.getSalesReport());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Zmień status zamówienia", description = "Jeśli status zostanie zmieniony na CANCELLED, system automatycznie zwróci towar do magazynu.")
    ResponseEntity<Void> updateStatus(@PathVariable Long id,@RequestBody @Valid UpdateOrderStatus dto) {
        orderService.updateOrderStatus(id, dto.newStatus());
        return ResponseEntity.noContent().build();
    }

}
