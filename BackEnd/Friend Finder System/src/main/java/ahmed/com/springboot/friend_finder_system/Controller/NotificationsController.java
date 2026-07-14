package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notification Controller",
        description = "APIs for managing user notifications"
)
public class NotificationsController {

    private final Notification_Service notification_Service;

    // ===================== GET USER NOTIFICATIONS =====================

    @Operation(
            summary = "Get User Notifications",
            description = "Retrieve all notifications for the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Notifications retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = NotificationDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/userNotifications")
    public ResponseEntity<List<NotificationDto>> getUserNotifications() {
        return ResponseEntity.ok(notification_Service.getUserNotifications());
    }
}