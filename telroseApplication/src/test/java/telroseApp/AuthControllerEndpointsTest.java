package telroseApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import telroseApp.config.SecurityConfig;
import telroseApp.dto.CostumerDto;
import telroseApp.dto.JwtAuthenticationResponse;
import telroseApp.dto.SignInRequest;
import telroseApp.model.Role;
import telroseApp.model.User;
import telroseApp.repository.UserRepository;
import telroseApp.service.AuthenticationService;
import telroseApp.service.JwtService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Transactional
class AuthControllerEndpointsTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@MockBean
	private AuthenticationService authenticationService;


	@Test
	@DisplayName("Testing register endpoint")
	void registerCostumerTest() throws Exception {
		AuthenticationService authenticationService = mock(AuthenticationService.class);
		CostumerDto costumerDto = new CostumerDto();
		costumerDto.setEmail("AMIRA@exmple.com");
		costumerDto.setDateOfBirth(LocalDate.of(2022,10,10));
		costumerDto.setFirstName("John");
		costumerDto.setMiddleName("Lio");
		costumerDto.setLastName("Doe");
		costumerDto.setPhoneNumber("960406587");
		costumerDto.setPassword("123");
		doNothing().when(authenticationService).registerCostumer(costumerDto);
		mockMvc.perform(post("/api/v1/auth/register")
						.content(asJsonString(costumerDto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("Testing login endpoint")
	void loginTest() throws Exception {
		SignInRequest signInRequest = new SignInRequest();
		Long userId = 123L;
		User user = User.builder()
				.id(userId)
				.email("daniilhizam@gmail.com")
				.password(passwordEncoder.encode("123"))
				.role(Role.USER)
				.build();
		when(userRepository.findByEmail("AMIRA@exmple.com")).thenReturn(Optional.of(user));
		signInRequest.setEmail("AMIRA@exmple.com");
		signInRequest.setPassword("123");
		JwtAuthenticationResponse authenticateResponseDto = new JwtAuthenticationResponse();
		authenticateResponseDto.setToken("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlkIjoiZDc2MWU3YWItMGFiNy00MTM5LTk0ZjktOWJhYzUxZDY0MWFmIiwiZW1haWwiOiJHT09GWUBnbWFpbC5jb20iLCJzdWIiOiJHT09GWUBnbWFpbC5jb20iLCJpYXQiOjE3MjUwMTU5NjMsImV4cCI6MTcyNTE1OTk2M30.Hp0XgHrVTmHBLO9r42uMyHCQ-5mMPfEBvjyHejVpoGQ");
		authenticateResponseDto.setReferenceToken("bcf748c3-3556-4080-9fe7-79f6c62031b6");
		when(authenticationService.login(any(SignInRequest.class),any(HttpServletResponse.class))).thenReturn(authenticateResponseDto);
		mockMvc.perform(post("/api/v1/auth/login")
						.content(asJsonString(signInRequest))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.referenceToken").isNotEmpty())
				.andExpect(status().isOk())
				.andDo(print());
	}



	public static String asJsonString(final Object obj) {
		{
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				return objectMapper.writeValueAsString(obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
