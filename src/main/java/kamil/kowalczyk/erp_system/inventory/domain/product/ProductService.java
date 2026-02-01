package kamil.kowalczyk.erp_system.inventory.domain.product;


import kamil.kowalczyk.erp_system.common.infrastructure.ResourceNotFoundException;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.CreateProductDto;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.ProductDto;
import kamil.kowalczyk.erp_system.inventory.domain.product.exception.InsufficientStockException;
import kamil.kowalczyk.erp_system.inventory.domain.product.exception.ProductAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(CreateProductDto dto) {

        if (productRepository.existsBySkuCode(dto.skuCode())) {
            throw new ProductAlreadyExistsException(dto.skuCode());
        }

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
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(pr -> new ProductDto(
                        pr.getId(),
                        pr.getName(),
                        pr.getPrice(),
                        pr.getStockQuantity(),
                        pr.getSkuCode(),
                        pr.getCreatedAt(),
                        pr.getUpdatedAt()
                ));
    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt o id" + id + " nie istnieje"));

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getSkuCode(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

    }

    public ProductDto decreaseStock(Long productId, int quantityToDecrease) {
        Product product = productRepository.findById((productId))
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono takiego produktu"));

        if (product.getStockQuantity() < quantityToDecrease) {
            throw new InsufficientStockException(
                    product.getSkuCode(),
                    product.getStockQuantity(),
                    quantityToDecrease
            );
        }

        product.setStockQuantity(product.getStockQuantity() - quantityToDecrease);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    public void updateStock(Long id, Integer quantityChange) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt nie istnieje!"));

        int newQuantity = product.getStockQuantity() + quantityChange;

        if(newQuantity < 0) {
            throw new InsufficientStockException(
                    product.getSkuCode(),
                    product.getStockQuantity(),
                    Math.abs(quantityChange)
            );
        }
        product.setStockQuantity(newQuantity);
    }

    private ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getSkuCode(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

}
