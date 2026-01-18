package kamil.kowalczyk.erp_system.sales.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import kamil.kowalczyk.erp_system.sales.domain.order.OrderStatus;

public record UpdateOrderStatus(
        @NotNull(message = "Musisz podać nowy status")
        OrderStatus newStatus
) {}
