package kamil.kowalczyk.erp_system.user.domain;

import kamil.kowalczyk.erp_system.user.domain.dto.RegisterUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "pepper", "TestPepper");
    }

    @Test
    void shouldRegisterUserSuccessfuly() {
        RegisterUserDto dto = new RegisterUserDto(
                "kamil_admin",
                "kamil@firma.pl",
                "mojetajnehaslo123"
        );

        User savedUser = new User("kamil_admin", "mojetajnehaslo123" ,"kamil@firma.pl",Role.USER );
        savedUser.setId(1L);
        savedUser.setActive(true);

        Mockito.when(userRepository.existsByUsername(dto.username())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("mojetajnehaslo123");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Long resultId = userService.registerUser(dto);
        Assertions.assertEquals(1L,resultId);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(anyString());
    }
}
