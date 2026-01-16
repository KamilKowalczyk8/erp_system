package kamil.kowalczyk.erp_system.inventory.domain.product.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateStockDto(
    @NotNull(message = "Podaj liczbę produktów do dodania lub usunięcia")
    Integer quantityChange
) {}
