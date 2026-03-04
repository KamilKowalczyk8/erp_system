package kamil.kowalczyk.erp_system.sales.domain.order;

import kamil.kowalczyk.erp_system.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClientId(Long clientId);

}
