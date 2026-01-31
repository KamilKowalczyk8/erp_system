package kamil.kowalczyk.erp_system.inventory.domain.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductDto(
    @NotBlank(message = "Nazwa jest wymagana")
    @Size(min = 3, max = 50, message = "Nazwa musi mieć od 3 do 50 znaków")
    String name,

    @Positive(message = "Cena musi być dodatnia")
    @DecimalMin(value = "0.01", message = "Cena musi być większa od zera")
    BigDecimal price,

    @NotNull(message = "Stan magazynowy jest wymagany")
    @PositiveOrZero(message = "Ilość nie może być ujemna")
    Integer stockQuantity,

    @NotBlank(message = "SKU jest wymagane")
    String skuCode
) {}

