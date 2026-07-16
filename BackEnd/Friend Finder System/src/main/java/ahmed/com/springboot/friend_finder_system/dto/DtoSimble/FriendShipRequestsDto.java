package ahmed.com.springboot.friend_finder_system.dto.DtoSimble;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "FriendShipRequestsDto",
        description = "Data Transfer Object representing a friendship request or friendship record detail"
)
public class FriendShipRequestsDto {

    @Schema(
            description = "The unique ID of the friendship record (primary key in database)",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long Friendship_Id;

    @Schema(
            description = "The ID of the other user involved in the friendship (not the authenticated user)",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long userSenderId; // not me

    @Schema(
            description = "URL of the other user's profile picture",
            example = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
            nullable = true
    )
    private String profilePicture;

    @Schema(
            description = "First name of the other user",
            example = "Omar",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstName;

    @Schema(
            description = "Last name of the other user",
            example = "Khaled",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String last_Name;
}