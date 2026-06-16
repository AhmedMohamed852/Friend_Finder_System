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

    @NotBlank(message = "Username is required")
    @Size(max = 50)
    @Schema(
            description = "Username of the user",
            example = "ahmed123"
    )
    private String username;

    @NotBlank(message = "First Name is required")
    @Size(max = 50)
    @Schema(
            description = "First name",
            example = "Ahmed"
    )
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(max = 50)
    @Schema(
            description = "Last name",
            example = "Mohamed"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    @Schema(
            description = "Email address",
            example = "ahmed@gmail.com"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(
            description = "User password",
            example = "********"
    )
    private String password;

    @NotNull(message = "Gender is required")
    @Schema(
            description = "Gender",
            example = "MALE"
    )
    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    @Schema(
            description = "Date of birth",
            example = "2000-01-01"
    )
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