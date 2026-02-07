package kamil.kowalczyk.erp_system.user.domain;

import kamil.kowalczyk.erp_system.user.domain.dto.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.pepper}")
    private String pepper;

    public Long registerUser(RegisterUserDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new RuntimeException("Nazwa użytkownika jest już zajęta");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Ten email jest już zajęty");
        }

        String passwordWithPepper = dto.rawPassword() + pepper;
        String hashedPassword = passwordEncoder.encode(passwordWithPepper);

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(hashedPassword);
        user.setEmail(dto.email());

        user.setRole(Role.USER);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
}
