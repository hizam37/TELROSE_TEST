package telroseApp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telroseApp.exception.RegistrationException;
import telroseApp.repository.ReferenceTokenRepository;
import telroseApp.dto.CostumerDto;
import telroseApp.model.ReferenceToken;
import telroseApp.model.User;
import telroseApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final ReferenceTokenRepository referenceTokenRepository;
    private final UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public void create(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RegistrationException("This email already exists");
        }

        saveUser(user);
    }



    public User getByEmail(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }


    public void updateContactInfo(User costumer, HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            Optional<ReferenceToken> costumerFound = referenceTokenRepository.findByToken(cookie.getValue());
            if(costumerFound.isPresent())
            {
                Optional<User> costumerToBeUpdated = userRepository.findById(costumerFound.get().getId());
                if(costumerToBeUpdated.isPresent())
                {
                    User getCostumer = costumerToBeUpdated.get();
                    getCostumer.setEmail(costumer.getEmail());
                    getCostumer.setPhoneNumber(costumer.getPhoneNumber());
                    userRepository.save(getCostumer);
                }
            }
        }
    }


    public void updateCostumerInfo(User costumer,HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            Optional<ReferenceToken> costumerFound = referenceTokenRepository.findByToken(cookie.getValue());
            if(costumerFound.isPresent())
            {
                Optional<User> costumerToBeUpdated = userRepository.findById(costumerFound.get().getId());
                if(costumerToBeUpdated.isPresent()) {
                    User getCostumer = costumerToBeUpdated.get();
                    getCostumer.setFirstName(costumer.getFirstName());
                    getCostumer.setMiddleName(costumer.getMiddleName());
                    getCostumer.setLastName(costumer.getLastName());
                    getCostumer.setEmail(costumer.getEmail());
                    getCostumer.setPhoneNumber(costumer.getPhoneNumber());
                    userRepository.save(getCostumer);
                }
            }
        }
    }


    public void removeCostumerById(Long id)
    {
        Optional<User> userFound = userRepository.findById(id);
        userFound.ifPresent(userRepository::delete);
    }


    public CostumerDto getCostumerById(Long id) {
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isPresent() && !userFound.get().getRole().name().equals("ADMIN")) {
            return convertToDto(userFound.get());
        } else {
            throw new RegistrationException("Клиент с таким идентификатором не существует");
        }
    }

    public List<CostumerDto> getAllCostumers() {
        List<User> costumerList = userRepository.findAll();
        return costumerList.stream().filter(costumer -> costumer.getRole().name().equals("USER"))
                .map(this::convertToDto).collect(Collectors.toList());
    }

    private CostumerDto convertToDto(User user) {
        return new CostumerDto(user.getId(),user.getFirstName(),user.getMiddleName(),user.getLastName(),user.getDateOfBirth(),user.getEmail(),user.getPhoneNumber(),user.getPassword());
    }






}

