package kamil.kowalczyk.erp_system.sales.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemDto(
    @NotNull(message = "Musisz podac id produktu")
    Long productId,

    @Positive(message = "Ilość musi być większa od zera")
    Integer quantity
    ) {}


