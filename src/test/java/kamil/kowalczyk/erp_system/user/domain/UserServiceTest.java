package kamil.kowalczyk.erp_system.user.domain;

import kamil.kowalczyk.erp_system.common.infrastructure.security.JwtService;
import kamil.kowalczyk.erp_system.user.domain.dto.LoginUserDto;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private final String FAKE_PEPPER = "testPepper";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "pepper", FAKE_PEPPER);
    }


    //Rejestracja ----------------------------------------------------------
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

        when(userRepository.existsByUsername(dto.username())).thenReturn(false);
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenReturn("mojetajnehaslo123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Long resultId = userService.registerUser(dto);
        Assertions.assertEquals(1L,resultId);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(anyString());
    }

    //Logowanie Happy End----------------------------------------------------------
    @Test
    void shouldLoginUserSuccessfully() {
        LoginUserDto dto = new LoginUserDto("jan@test.pl", "haslo123");

        User userFromDb = new User();
        userFromDb.setEmail("jan@test.pl");
        userFromDb.setPassword("encoded_password_from_db");
        userFromDb.setActive(true);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(userFromDb));

        when(passwordEncoder.matches("haslo123" + FAKE_PEPPER, "encoded_password_from_db"))
                .thenReturn(true);

        when(jwtService.generateToken(userFromDb)).thenReturn("super_tajny_token_jwt");

        String token = userService.loginUser(dto);

        assertEquals("super_tajny_token_jwt", token);

        verify(passwordEncoder).matches(eq("haslo123" + FAKE_PEPPER), anyString());
    }

    //Złe hasło
    @Test
    void shouldThrowException_WhenPasswordIsInvalid() {
        LoginUserDto dto = new LoginUserDto("jan@test.pl", "zle_haslo");

        User userFromDb = new User();
        userFromDb.setEmail("jan@test.pl");
        userFromDb.setPassword("poprawny_hash");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(userFromDb));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.loginUser(dto));

        assertEquals("Błędny email lub hasło", ex.getMessage());
        verifyNoInteractions(jwtService);
    }

    //Konto nie aktywne
    @Test
    void shouldThrowException_WhenAccountIsInactive() {
        LoginUserDto dto = new LoginUserDto("jan@test.pl", "haslo123");

        User userFromDb = new User();
        userFromDb.setEmail("jan@test.pl");
        userFromDb.setPassword("hash");
        userFromDb.setActive(false);

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(userFromDb));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.loginUser(dto));

        assertEquals("Konto nie jest aktywowane", ex.getMessage());

        verifyNoInteractions(jwtService);
    }
}
