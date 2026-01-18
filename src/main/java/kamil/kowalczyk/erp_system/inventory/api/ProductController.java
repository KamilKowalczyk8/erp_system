package kamil.kowalczyk.erp_system.inventory.api;

import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.inventory.domain.product.ProductService;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.CreateProductDto;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.ProductDto;
import kamil.kowalczyk.erp_system.inventory.domain.product.dto.UpdateStockDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/products")
@Tag(name = "Magazyn - Produkty", description = "Zarządzanie towarami w systemie")
class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Dodaj nowy produkt", description = "Tworzy produkt i zwraca jego ID. Wymaga unikalnego SKU.")
    ResponseEntity<Long> createProduct(@RequestBody @Valid CreateProductDto dto) {
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie produkty", description = "Domyślnie 10 produktów na stronę, sortowane po ID.")
    ResponseEntity<Page<ProductDto>> getProducts(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz szczegóły produktu", description = "Zwraca pojedynczy produkt po ID.")
    ResponseEntity<ProductDto> getOneProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }


    @PatchMapping("/{id}/stock")
    @Operation(summary = "Zaktualizuj stan magazynowy", description = "Dodatnia wartość -> Dostawa. Ujemna wartość -> Wydanie.")
    ResponseEntity<Void> updateStock(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStockDto dto
    ){
        productService.updateStock(id, dto.quantityChange());
        return ResponseEntity.noContent().build();
    }
}
