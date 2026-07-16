package ahmed.com.springboot.friend_finder_system.dto.DtoSimble;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "User_Simple_Dto",
        description = "Simplified Data Transfer Object representing user public profile details"
)
public class User_Simple_Dto {

    @Schema(
            description = "Unique identifier of the user",
            example = "5",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id; // not me

    @Schema(
            description = "URL of the user's profile picture",
            example = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            nullable = true
    )
    private String profilePicture;

    @Schema(
            description = "URL of the user's profile cover photo",
            example = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500",
            nullable = true
    )
    private String CoverPhoto;

    @Schema(
            description = "First name of the user",
            example = "Ahmed",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
            description = "Last name of the user",
            example = "Mohamed",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String lastName;

    @Schema(
            description = "City where the user currently lives",
            example = "Cairo",
            nullable = true
    )
    private String city;
}