package ahmed.com.springboot.friend_finder_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Like Data Transfer Object")
public class LikeDto {

    @Schema(description = "Like ID", example = "1")
    private Long id;

    @Schema(description = "Post ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long postId;

    @Schema(description = "User ID", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
}
