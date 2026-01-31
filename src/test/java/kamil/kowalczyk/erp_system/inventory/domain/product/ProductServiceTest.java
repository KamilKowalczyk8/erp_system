package kamil.kowalczyk.erp_system.inventory.domain.product;


import kamil.kowalczyk.erp_system.inventory.domain.product.dto.CreateProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfuly() {
        CreateProductDto dto = new CreateProductDto(
                "Laptop Testowy",
                BigDecimal.valueOf(3000.00),
                10,
                "TEST-SKU-123"
        );

        Product savedProduct = new Product("Laptop Testowy", BigDecimal.valueOf(3000.00), 10, "TEST-SKU-123");
        savedProduct.setId(1L);
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Long resultId = productService.createProduct(dto);
        Assertions.assertEquals(1L, resultId);
        Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
    }

    @Test
    void shouldUpdateStockSuccessfuly() {
        Product existingProduct = new Product("Myszka", BigDecimal.valueOf(100.00), 10, "SKU-1");
        existingProduct.setId(2L);

        Mockito.when(productRepository.findById(2L)).thenReturn(java.util.Optional.of(existingProduct));
        productService.updateStock(2L, 50);

        Assertions.assertEquals(60, existingProduct.getStockQuantity());
    }
}
