package kamil.kowalczyk.erp_system.sales.domain.order.dto;

import java.math.BigDecimal;

public record SalesReportDto(
        Long totalOrders,
        BigDecimal totalRevenue
) {
}
