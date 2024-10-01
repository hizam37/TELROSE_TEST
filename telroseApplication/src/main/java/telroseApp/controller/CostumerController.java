package telroseApp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import telroseApp.model.User;
import telroseApp.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/login/costumer")
public class CostumerController {

    private final UserService userService;




    @PutMapping("/update_costumer_info")
    public void updateCostumerInfo(@RequestBody User costumer, HttpServletRequest request)
    {
        userService.updateCostumerInfo(costumer,request);
    }



    @PutMapping ("/update_contact_info")
    public void updateCostumerContactInfo(@RequestBody User costumer, HttpServletRequest request)
    {
        userService.updateContactInfo(costumer,request);
    }





}
