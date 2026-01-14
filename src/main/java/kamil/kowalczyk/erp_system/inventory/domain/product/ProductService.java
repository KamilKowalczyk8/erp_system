package kamil.kowalczyk.erp_system.inventory.domain.product;


import kamil.kowalczyk.erp_system.inventory.domain.product.dto.CreateProductDto;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.ProductDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createProduct(CreateProductDto dto) {
        Product prod = new Product();
        prod.setName(dto.name());
        prod.setPrice(dto.price());
        prod.setStockQuantity(dto.stockQuantity());
        prod.setSkuCode(dto.skuCode());

        prod.setCreatedAt(LocalDateTime.now());
        prod.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(prod).getId();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(pr -> new ProductDto(
                        pr.getId(),
                        pr.getName(),
                        pr.getPrice(),
                        pr.getStockQuantity(),
                        pr.getSkuCode(),
                        pr.getCreatedAt(),
                        pr.getUpdatedAt()
                ))
                .toList();
    }

}
