package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Friendship Data Transfer Object")
public class FriendshipDto {

    @Schema(description = "Friendship ID", example = "1")
    private Long id;

    @NotNull(message = "User1 Id is required")
    @Schema(description = "Sender User ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long user1;

    @NotNull(message = "User2 Id is required")
    @Schema(description = "Receiver User ID", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long user2;

    @Schema(
            description = "Friendship status (PENDING, ACCEPTED, REJECTED)",
            example = "PENDING"
    )
    private FriendshipStatus status;
}