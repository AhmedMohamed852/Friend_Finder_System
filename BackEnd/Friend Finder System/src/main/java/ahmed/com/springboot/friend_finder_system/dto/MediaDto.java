package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Media Data Transfer Object")
public class MediaDto {

    @Schema(
            description = "Media URL",
            example = "https://example.com/images/post-image.jpg"
    )
    private String url;

    @Schema(
            description = "Media type",
            example = "IMAGE"
    )
    private MediaType type;
}