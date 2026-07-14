package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.PostPrivacy;
import ahmed.com.springboot.friend_finder_system.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Post Data Transfer Object")
public class PostDto {

    @Schema(
            description = "Post ID",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "error.post.content.required")
    @Schema(
            description = "Post content",
            example = "Hello Friends, this is my first post."
    )
    private String content;

    @Schema(
            description = "Post media files"
    )
    private Set<MediaDto> media;

    @Schema(
            description = "Number of likes on the post",
            example = "15",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer countLikes;

    @Schema(
            description = "Number of comments on the post",
            example = "8",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer countComments;

    private LocalDateTime createdDate;

    @Schema(
            description = "Post privacy level",
            example = "PUBLIC"
    )
    private PostPrivacy privacy;

    @Schema(
            description = "Post author",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UserDto author;

    Boolean likedIs;

}