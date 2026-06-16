package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Notification Data Transfer Object")
public class NotificationDto {

    @Schema(description = "Notification ID", example = "1")
    private Long id;

    @NotBlank(message = "Content is required")
    @Schema(
            description = "Notification content message",
            example = "John liked your post"
    )
    private String content;

    @Schema(
            description = "Whether notification is read or not",
            example = "false"
    )
    private boolean isRead;

    @Schema(
            description = "Related post ID (if exists)",
            example = "10"
    )
    private Long postId;

    @NotNull(message = "Type is required")
    @Schema(
            description = "Type of notification",
            example = "LIKE"
    )
    private NotificationType type;

    //______________relations_______________________________

    @NotNull(message = "Triggered by user is required")
    @Schema(
            description = "User who triggered the notification"
    )
    private User_Simple_Dto triggeredBy;
}