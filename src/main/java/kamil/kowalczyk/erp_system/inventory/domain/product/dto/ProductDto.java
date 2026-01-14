package kamil.kowalczyk.erp_system.inventory.domain.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        String skuCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
