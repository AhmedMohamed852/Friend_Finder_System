package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private User_Simple_Dto author;

    @Schema(
            description = "ID of the post associated with this comment",
            example = "10"
    )
    private Long postId;

    private Integer likedIs;

    private Integer countComments;

    private LocalDateTime localDateTime;
}