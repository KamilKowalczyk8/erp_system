package kamil.kowalczyk.erp_system.inventory.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySkuCode(String skuCode);
}
