package telroseApp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import telroseApp.exception.RegistrationException;
import telroseApp.dto.CostumerDto;
import telroseApp.dto.JwtAuthenticationResponse;
import telroseApp.dto.SignInRequest;
//import rest_api.userDataEditor.model.Costumer;
import telroseApp.model.Role;
import telroseApp.model.User;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final ReferenceTokenService referenceTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public void registerCostumer(CostumerDto costumerDto)
    {
        var costumer = User.builder()
                .firstName(costumerDto.getFirstName())
                .middleName(costumerDto.getMiddleName())
                .lastName(costumerDto.getLastName())
                .dateOfBirth(costumerDto.getDateOfBirth())
                .email(costumerDto.getEmail())
                .phoneNumber(costumerDto.getPhoneNumber())
                .password(passwordEncoder.encode(costumerDto.getPassword()))
                        .role(Role.USER).build();
        if(!Objects.nonNull(costumer))
        {
            throw new RegistrationException("Пожалуйста, заполните форму");
        }
        userService.create(costumer);
    }

    public JwtAuthenticationResponse login(SignInRequest signInRequest, HttpServletResponse response)
    {
       Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
       if(authentication.isAuthenticated())
       {
           throw new RegistrationException("Пользователь не зарегистрирован.");
       }
        var user = userService.getByEmail(signInRequest.getEmail());
        var jwt = jwtService.generateToken(user);
        var referenceToken = referenceTokenService.generateReferenceTokenByCostumerId(user.getId());
        Cookie cookie = new Cookie("Refresh_token", referenceToken.getToken());
        Cookie cookie2 = new Cookie("Access_token", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        response.addCookie(cookie2);
        response.addHeader("Authorization", "Bearer " + jwt);
        return new JwtAuthenticationResponse(jwt,referenceToken.getToken());
    }







}
