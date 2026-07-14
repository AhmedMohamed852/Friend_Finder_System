package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @NotBlank(message = "error.message.content.required")
    @Schema(
            description = "Message content",
            example = "Hello, how are you?"
    )
    private String content;

    @Schema(
            description = "Read status of the message",
            example = "false"
    )
    private User_Simple_Dto sender;

    private User_Simple_Dto  receiver;

    private Boolean isRead;

    //______________relations_______________________________
}