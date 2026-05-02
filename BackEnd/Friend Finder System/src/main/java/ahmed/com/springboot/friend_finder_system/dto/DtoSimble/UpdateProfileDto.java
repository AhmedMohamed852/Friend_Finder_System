package ahmed.com.springboot.friend_finder_system.dto.DtoSimble;

import ahmed.com.springboot.friend_finder_system.eNum.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UpdateProfileDto {

    private Long id;

    private String image;

    @NotBlank(message = "First Name is required")
    @NotNull(message = "First Name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @NotNull(message = "Last Name is required")
    @Size(max = 50)
    private String lastName;


    private String bio;
    private String city;
    private String country;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    // NO: username, email, password, roles
}
