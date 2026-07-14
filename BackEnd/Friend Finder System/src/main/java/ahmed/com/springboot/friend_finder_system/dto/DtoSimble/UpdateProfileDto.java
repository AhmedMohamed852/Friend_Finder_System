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

    @NotBlank(message = "error.user.firstName.required")
    @Size(max = 50, message = "error.user.firstName.tooLong")
    private String firstName;

    @NotBlank(message = "error.user.lastName.required")
    @Size(max = 50, message = "error.user.lastName.tooLong")
    private String lastName;

    private String coverPhoto;

    private String bio;
    private String city;
    private String country;

    @Past(message = "error.user.dateOfBirth.mustBePast")
    @NotNull(message = "error.user.dateOfBirth.required")
    private LocalDate dateOfBirth;

    @NotNull(message = "error.user.gender.required")
    private Gender gender;

    // NO: username, email, password, roles
}
