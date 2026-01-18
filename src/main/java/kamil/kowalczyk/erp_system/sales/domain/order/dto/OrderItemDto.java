package kamil.kowalczyk.erp_system.sales.domain.order.dto;

import java.math.BigDecimal;

public record OrderItemDto(
      Long productId,
      Integer quantity,
      BigDecimal unitPrice,
      BigDecimal totalPrice
) {}
