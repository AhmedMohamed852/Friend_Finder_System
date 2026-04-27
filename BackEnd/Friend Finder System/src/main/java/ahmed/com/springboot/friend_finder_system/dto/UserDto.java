package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 50)
    private String username;


    @NotBlank(message = "First Name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(max = 50)
    private String lastName;


    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 8 , message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;


    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String profilePicture;

    @Size(max = 500)
    private String bio;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String city;

    // _______________relations__________________________________




}
