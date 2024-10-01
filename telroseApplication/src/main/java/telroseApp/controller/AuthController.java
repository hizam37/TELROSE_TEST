package telroseApp.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import telroseApp.service.UserService;
import telroseApp.dto.CostumerDto;
import telroseApp.dto.JwtAuthenticationResponse;
import telroseApp.dto.SignInRequest;
import telroseApp.service.AuthenticationService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody CostumerDto registrationDto)
    {
        authenticationService.registerCostumer(registrationDto);
    }
    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest signInRequest, HttpServletResponse response)
    {
      return authenticationService.login(signInRequest,response);
    }

    @Secured("ADMIN")
    @GetMapping("/get_costumer/{id}")
    public CostumerDto getCostumerById(@PathVariable Long id)
    {
         return userService.getCostumerById(id);
    }

    @Secured("ADMIN")
    @GetMapping("/get_all_costumers")
    public ResponseEntity<?> getAllCostumers()
    {
        List<CostumerDto> costumerList = userService.getAllCostumers();
        return ResponseEntity.ok(costumerList);
    }

    @Secured("ADMIN")
    @GetMapping("/remove_costumer/{id}")
    public void removeCostumer(@PathVariable Long id)
    {
        userService.removeCostumerById(id);
    }



}
