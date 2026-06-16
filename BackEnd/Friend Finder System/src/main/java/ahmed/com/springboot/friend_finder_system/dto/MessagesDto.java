package ahmed.com.springboot.friend_finder_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Messages Data Transfer Object")
public class MessagesDto {

    @Schema(
            description = "Message ID",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "Content is required")
    @Schema(
            description = "Message content",
            example = "Hello, how are you?"
    )
    private String content;

    @Schema(
            description = "Read status of the message",
            example = "false"
    )
    private boolean isRead;

    //______________relations_______________________________
}