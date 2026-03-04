package kamil.kowalczyk.erp_system.sales.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDto(
        @NotEmpty(message = "Zamówienie musi zawierać przynajmnije jedną pozycję")
        @Valid
        List<CreateOrderItemDto> items
) {
}
