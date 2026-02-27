package kamil.kowalczyk.erp_system.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import kamil.kowalczyk.erp_system.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
