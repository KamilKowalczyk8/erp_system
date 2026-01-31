package kamil.kowalczyk.erp_system.inventory.domain.product;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    public Product(String name, BigDecimal price, int stockQuantity, String skuCode) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.skuCode = skuCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stockQuantity;

    private String skuCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void PrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
