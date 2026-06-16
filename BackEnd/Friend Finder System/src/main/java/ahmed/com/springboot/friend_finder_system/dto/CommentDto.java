package ahmed.com.springboot.friend_finder_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Comment Data Transfer Object")
public class CommentDto {

    @Schema(
            description = "Comment ID",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Comment content",
            example = "Great post!"
    )
    private String content;

    @Schema(
            description = "ID of the post associated with this comment",
            example = "10"
    )
    private Long postId;
}