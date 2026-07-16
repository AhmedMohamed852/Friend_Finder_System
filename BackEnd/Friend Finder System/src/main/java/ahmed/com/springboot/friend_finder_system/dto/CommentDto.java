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
@Schema(
        name = "CommentDto",
        description = "Data Transfer Object representing a comment, including author details and counters"
)
public class CommentDto {

    @Schema(
            description = "Unique identifier of the comment",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "The actual text content of the comment",
            example = "Great post, looking forward to more updates!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @Schema(
            description = "Simplified details of the user who authored the comment"
    )
    private User_Simple_Dto author;

    @Schema(
            description = "The ID of the post that this comment belongs to",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long postId;

    @Schema(
            description = "Like status or reaction type of the current logged-in user on this comment (e.g., 1 if liked, null or 0 otherwise)",
            example = "1",
            nullable = true
    )
    private Integer likedIs;

    @Schema(
            description = "Total number of replies/nested comments under this comment",
            example = "3"
    )
    private Integer countComments;

    @Schema(
            description = "The timestamp when the comment was created",
            example = "2026-07-16T17:45:00",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime localDateTime;
}