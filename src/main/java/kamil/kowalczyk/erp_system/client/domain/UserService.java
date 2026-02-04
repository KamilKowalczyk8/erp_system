package kamil.kowalczyk.erp_system.client.domain;

import kamil.kowalczyk.erp_system.user.domain.dto.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


}
