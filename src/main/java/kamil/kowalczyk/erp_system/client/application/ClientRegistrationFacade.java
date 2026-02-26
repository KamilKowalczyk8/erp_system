package kamil.kowalczyk.erp_system.client.application;


import kamil.kowalczyk.erp_system.client.application.dto.RegisterClientRequestDto;
import kamil.kowalczyk.erp_system.client.domain.ClientService;
import kamil.kowalczyk.erp_system.client.domain.dto.CreateClientDto;
import kamil.kowalczyk.erp_system.user.domain.UserService;
import kamil.kowalczyk.erp_system.user.domain.dto.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientRegistrationFacade {

    private final UserService userService;
    private final ClientService clientService;

    public Long registerFullClient(RegisterClientRequestDto request) {

        RegisterUserDto registerUserDto = new RegisterUserDto(
                request.email(),
                request.password()
        );
        Long newUserId = userService.registerUser(registerUserDto);

        CreateClientDto createClientDto = new CreateClientDto(
                request.firstName(),
                request.lastName(),
                request.phoneNumber()
        );

        return clientService.createClient(newUserId, createClientDto);
    }
}
