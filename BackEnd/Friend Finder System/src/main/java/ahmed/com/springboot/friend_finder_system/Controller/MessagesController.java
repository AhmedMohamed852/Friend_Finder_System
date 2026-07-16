package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.MessagesDto;
import ahmed.com.springboot.friend_finder_system.service.Message_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(
        name = "Messages Controller",
        description = "APIs for sending and managing user messages"
)
public class MessagesController {

    private final Message_Service messagesService;

    //TODO ===================== SEND MESSAGE =====================

    @Operation(
            summary = "Send Message",
            description = "Send a new message from the authenticated user to another user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Message sent successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessagesDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<MessagesDto> sendMessage(
            @Parameter(description = "Receiver User ID") @PathVariable Long receiverId,
            @Valid @RequestBody MessagesDto messagesDto) {
        return ResponseEntity.ok(messagesService.sendMessage(receiverId, messagesDto));
    }


    //TODO ===================== GET MESSAGE BY ID =====================

    @Operation(
            summary = "Get Message By Id",
            description = "Retrieve a single message by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Message retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessagesDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Message not found"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{messageId}")
    public ResponseEntity<MessagesDto> getMessageById(
            @Parameter(description = "Message ID") @PathVariable Long messageId) {
        return ResponseEntity.ok(messagesService.getMessageById(messageId));
    }


    //TODO ===================== GET CONVERSATION =====================

    @Operation(
            summary = "Get Conversation",
            description = "Retrieve the full conversation between two users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conversation retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MessagesDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<List<MessagesDto>> getConversation(
            @Parameter(description = "First User ID") @PathVariable Long userId1,
            @Parameter(description = "Second User ID") @PathVariable Long userId2) {
        return ResponseEntity.ok(messagesService.getConversation(userId1, userId2));
    }


    //TODO ===================== GET INBOX =====================

    @Operation(
            summary = "Get Inbox",
            description = "Retrieve all messages received by a specific user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inbox retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MessagesDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/inbox/{userId}")
    public ResponseEntity<List<MessagesDto>> getInbox(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(messagesService.getInbox(userId));
    }


    //TODO ===================== GET SENT MESSAGES =====================

    @Operation(
            summary = "Get Sent Messages",
            description = "Retrieve all messages sent by a specific user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sent messages retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MessagesDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessagesDto>> getSentMessages(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(messagesService.getSentMessages(userId));
    }


    //TODO ===================== GET UNREAD MESSAGES =====================

    @Operation(
            summary = "Get Unread Messages",
            description = "Retrieve all unread messages for a specific user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Unread messages retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MessagesDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<MessagesDto>> getUnreadMessages(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(messagesService.getUnreadMessages(userId));
    }


    //TODO ===================== MARK AS READ =====================

    @Operation(
            summary = "Mark Message As Read",
            description = "Mark a single message as read"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Message marked as read successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessagesDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Message not found"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{messageId}/read")
    public ResponseEntity<MessagesDto> markAsRead(
            @Parameter(description = "Message ID") @PathVariable Long messageId) {
        return ResponseEntity.ok(messagesService.markAsRead(messageId));
    }


    //TODO ===================== MARK CONVERSATION AS READ =====================

    @Operation(
            summary = "Mark Conversation As Read",
            description = "Mark all messages in a conversation between two users as read"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conversation marked as read successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/conversation/{senderId}/{receiverId}/read")
    public ResponseEntity<Void> markConversationAsRead(
            @Parameter(description = "Sender User ID") @PathVariable Long senderId,
            @Parameter(description = "Receiver User ID") @PathVariable Long receiverId) {
        messagesService.markConversationAsRead(senderId, receiverId);
        return ResponseEntity.ok().build();
    }


    //TODO ===================== COUNT UNREAD MESSAGES =====================

    @Operation(
            summary = "Count Unread Messages",
            description = "Get the count of unread messages for a specific user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Unread messages count retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/unread/{userId}/count")
    public ResponseEntity<Long> countUnreadMessages(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(messagesService.countUnreadMessages(userId));
    }


    //TODO ===================== UPDATE MESSAGE =====================

    @Operation(
            summary = "Update Message",
            description = "Update the content of an existing message"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Message updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessagesDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Message not found"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{messageId}")
    public ResponseEntity<MessagesDto> updateMessage(
            @Parameter(description = "Message ID") @PathVariable Long messageId,
            @Valid @RequestBody MessagesDto messagesDto) {
        return ResponseEntity.ok(messagesService.updateMessage(messageId, messagesDto));
    }


    //TODO ===================== DELETE MESSAGE =====================

    @Operation(
            summary = "Delete Message",
            description = "Delete a message by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Message deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Message not found"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "Message ID") @PathVariable Long messageId) {
        messagesService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}