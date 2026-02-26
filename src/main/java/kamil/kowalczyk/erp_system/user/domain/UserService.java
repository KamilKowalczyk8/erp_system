package kamil.kowalczyk.erp_system.user.domain;

import kamil.kowalczyk.erp_system.common.infrastructure.security.JwtService;
import kamil.kowalczyk.erp_system.user.domain.dto.LoginUserDto;
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
    private final JwtService jwtService;

    @Value("${app.security.pepper}")
    private String pepper;

    public Long registerUser(RegisterUserDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Ten email jest już zajęty");
        }

        String passwordWithPepper = dto.rawPassword() + pepper;
        String hashedPassword = passwordEncoder.encode(passwordWithPepper);

        User user = new User();
        user.setPassword(hashedPassword);
        user.setEmail(dto.email());

        user.setRole(Role.USER);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    public String loginUser(LoginUserDto dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Błędny email lub hasło"));

        String passwordWithPepper = dto.password() + pepper;

        if(!passwordEncoder.matches(passwordWithPepper, user.getPassword())) {
            throw new RuntimeException("Błędny email lub hasło");
        }

        if(!user.isActive()) {
            throw new RuntimeException("Konto nie jest aktywowane");
        }

        return jwtService.generateToken(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkowmnika"));
    }
}
