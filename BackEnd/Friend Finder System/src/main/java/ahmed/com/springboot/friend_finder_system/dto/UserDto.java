package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.Gender;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "User Data Transfer Object")
public class UserDto {

    @Schema(
            description = "User ID",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;


    @Schema(
            description = "Username of the user",
            example = "ahmed123"
    )
    @NotBlank(message = "error.user.username.required")
    private String username;


    @Schema(
            description = "First name",
            example = "Ahmed"
    )
    @NotBlank(message = "error.user.firstName.required")
    private String firstName;


    @Schema(
            description = "Last name",
            example = "Mohamed"
    )
    @NotBlank(message = "error.user.lastName.required")
    private String lastName;

    @Schema(
            description = "Email address",
            example = "ahmed@gmail.com"
    )
    @NotBlank(message = "error.user.email.required")
    @Email(message = "error.user.email.invalid")
    private String email;


    @Schema(
            description = "User password",
            example = "********"
    )
    @NotBlank(message = "error.user.password.required")
    @Size(min = 8, message = "error.user.password.tooShort")
    private String password;

    @Schema(
            description = "Gender",
            example = "MALE"
    )
    @NotNull(message = "error.user.gender.required")
    private Gender gender;


    @Schema(
            description = "Date of birth",
            example = "2000-01-01"
    )
    @Past(message = "error.user.dateOfBirth.mustBePast")
    @NotNull(message = "error.user.dateOfBirth.required")
    private LocalDate dateOfBirth;

    @Schema(
            description = "Profile picture URL",
            example = "https://example.com/profile.jpg"
    )
    private String profilePicture;

    @Schema(
            description = "Cover photo URL",
            example = "https://example.com/cover.jpg"
    )
    private String coverPhoto;

    @Size(max = 500)
    @Schema(
            description = "User bio",
            example = "Software Engineer | Java Developer"
    )
    private String bio;

    @Size(max = 100)
    @Schema(
            description = "Country",
            example = "Egypt"
    )
    private String country;

    @Size(max = 100)
    @Schema(
            description = "City",
            example = "Cairo"
    )
    private String city;

    // _______________relations__________________________________

    @Schema(
            description = "User roles"
    )
    private Set<RolesDto> roles = new HashSet<>();
}