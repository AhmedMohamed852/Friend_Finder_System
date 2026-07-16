package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.eNum.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "StoriesDto",
        description = "Data Transfer Object representing a user story with media URL and type"
)
public class StoriesDto {

    @Schema(
            description = "Unique identifier of the story",
            example = "25",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "The direct URL to the story media (image or video stored on cloud/server)",
            example = "https://my-bucket.s3.amazonaws.com/stories/story_123.mp4",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "error.story.url.required")
    private String url;

    @Schema(
            description = "The media type of the story (e.g., IMAGE, VIDEO)",
            example = "VIDEO",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private MediaType type;

    @Schema(
            description = "Simplified details of the user who published the story"
    )
    private User_Simple_Dto user;
}