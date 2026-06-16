package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Match Response DTO - represents potential friend matching result")
public class MatchDto {

    @Schema(description = "Matched user details")
    private User_Simple_Dto user;

    @Schema(
            description = "Match score between users (0.0 - 1.0)",
            example = "0.85"
    )
    private Double matchScore;

    @Schema(
            description = "Common interests between users",
            example = "[\"SPORTS\", \"MUSIC\"]"
    )
    private Set<String> commonInterests;

    @Schema(
            description = "Number of mutual friends",
            example = "3"
    )
    private Integer mutualFriendsCount;

    @Schema(
            description = "Whether both users are from the same city",
            example = "true"
    )
    private Boolean sameCity;
}