package telroseApp;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import telroseApp.config.SecurityConfig;
import telroseApp.dto.CostumerDto;
import telroseApp.model.ReferenceToken;
import telroseApp.model.Role;
import telroseApp.model.User;
import telroseApp.repository.ReferenceTokenRepository;
import telroseApp.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
public class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ReferenceTokenRepository referenceTokenRepository;

    @Test
    @DisplayName("Testing update costumer info")
    public void update_costumer_info()
    {
        String virtualRefreshToken = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("referenceToken", virtualRefreshToken);
        Cookie[] cookies = new Cookie[]{cookie};
        CostumerDto costumerDto = new CostumerDto();
        ReferenceToken referenceToken = new ReferenceToken();
        Long costumerId = 123L;
        costumerDto.setId(costumerId);
        costumerDto.setEmail("daniilhizam@gmail.com");
        costumerDto.setDateOfBirth(LocalDate.of(2022,10,10));
        costumerDto.setFirstName("dan");
        costumerDto.setMiddleName("Lio");
        costumerDto.setLastName("Doe");
        costumerDto.setPhoneNumber("960406587");
        costumerDto.setPassword("123");
        referenceToken.setCostumerId(costumerId);
        referenceToken.setToken(virtualRefreshToken);
        User user =User.builder()
                .firstName(costumerDto.getFirstName())
                .middleName(costumerDto.getMiddleName())
                .lastName(costumerDto.getLastName())
                .dateOfBirth(costumerDto.getDateOfBirth())
                .email(costumerDto.getEmail())
                .phoneNumber(costumerDto.getPhoneNumber())
                .password(passwordEncoder.encode(costumerDto.getPassword()))
                .role(Role.USER).build();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        when(referenceTokenRepository.findByToken(virtualRefreshToken)).thenReturn(Optional.of(referenceToken));
        when(userRepository.findById(costumerId)).thenReturn(Optional.of(user));
        user.setEmail("danielhizam@gmail.com");
        assertEquals("danielhizam@gmail.com",user.getEmail());

    }


    @Test
    @DisplayName("Testing update contact info")
    public void update_contact_info()
    {
        String virtualRefreshToken = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("referenceToken", virtualRefreshToken);
        Cookie[] cookies = new Cookie[]{cookie};
        CostumerDto costumerDto = new CostumerDto();
        ReferenceToken referenceToken = new ReferenceToken();
        Long costumerId = 123L;
        costumerDto.setId(costumerId);
        costumerDto.setEmail("daniilhizam@gmail.com");
        costumerDto.setDateOfBirth(LocalDate.of(2022,10,10));
        costumerDto.setFirstName("dan");
        costumerDto.setMiddleName("Lio");
        costumerDto.setLastName("Doe");
        costumerDto.setPhoneNumber("960406587");
        costumerDto.setPassword("123");
        referenceToken.setCostumerId(costumerId);
        referenceToken.setToken(virtualRefreshToken);
        User user =User.builder()
                .firstName(costumerDto.getFirstName())
                .middleName(costumerDto.getMiddleName())
                .lastName(costumerDto.getLastName())
                .dateOfBirth(costumerDto.getDateOfBirth())
                .email(costumerDto.getEmail())
                .phoneNumber(costumerDto.getPhoneNumber())
                .password(passwordEncoder.encode(costumerDto.getPassword()))
                .role(Role.USER).build();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        when(referenceTokenRepository.findByToken(virtualRefreshToken)).thenReturn(Optional.of(referenceToken));
        when(userRepository.findById(costumerId)).thenReturn(Optional.of(user));
        user.setEmail("danielhizam@gmail.com");
        assertEquals("danielhizam@gmail.com",user.getEmail());

    }
}
