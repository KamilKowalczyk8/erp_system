package kamil.kowalczyk.erp_system.inventory.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductDto(
    @NotBlank(message = "Nazwa jest wymagana")
    String name,

    @Positive(message = "Cena musi być dodatnia")
    BigDecimal price,

    @PositiveOrZero(message = "Ilość nie może być ujemna")
    Integer stockQuantity,

    @NotBlank(message = "SKU jest wymagane")
    String skuCode
) {}

