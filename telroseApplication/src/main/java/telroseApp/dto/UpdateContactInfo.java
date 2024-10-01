package telroseApp.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateContactInfo {

    private String email;
    private String phoneNumber;

}
