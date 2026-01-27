package kamil.kowalczyk.erp_system.sales.domain.order.dto;

import kamil.kowalczyk.erp_system.sales.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        LocalDateTime createdAt,
        OrderStatus status,

        Long clientId,
        String clientName,
        String clientEmail,

        List<OrderItemDto> items,
        BigDecimal totalOrderValue
) {}
