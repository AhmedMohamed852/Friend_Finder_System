package ahmed.com.springboot.friend_finder_system.Controller;


import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.FriendShipRequestsDto;
import ahmed.com.springboot.friend_finder_system.helper.MessageResponse;
import ahmed.com.springboot.friend_finder_system.service.Friendship_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Tag(
        name = "Friendship Controller",
        description = "APIs for managing friendship requests"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/friendship")
@RequiredArgsConstructor
public class FriendShipController {
    //TODO: Declare Service Methods

    private final Friendship_Service friendship_service;



    //TODO:_______________ Implement Service Methods ____________________________





//TODO:_______________ Send Friend Request ____________________________

    /*
     * Swagger Documentation:
     * Send a new friend request to another user.
     */
    @Operation(
            summary = "Send Friend Request",
            description = "Send a friend request to another user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Friend request sent successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Target user not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Friend request already exists"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/send-FriendRequest/{User_Received}")
    public ResponseEntity<Void> sendFriendRequest(

            @Parameter(
                    description = "ID of the user receiving the friend request",
                    example = "5",
                    required = true
            )
            @PathVariable Long User_Received
    ) {
        friendship_service.sendFriendRequest(User_Received);
        return ResponseEntity.noContent().build();
    }



    //TODO:_______________ Get My Friendship Requests ____________________________

    /*
     * Swagger Documentation:
     * Retrieve all friendship requests for the current user.
     */
    @Operation(
            summary = "Get My Friendship Requests",
            description = "Returns all pending friendship requests for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Friendship requests retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = FriendShipRequestsDto.class
                                    )
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/show_FriendshipRequests")
    public ResponseEntity<List<FriendShipRequestsDto>> showFriendshipRequests() {
        return ResponseEntity.ok(friendship_service.getFriendshipsByUser1Id());
    }
    //TODO:_______________ Sent Friendship Requests ____________________________

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/SentFriendshipRequests")
    public ResponseEntity<List<FriendShipRequestsDto>> SentFriendshipRequests() {
        return ResponseEntity.ok(friendship_service.getSentFriendships());
    }

    //TODO:_______________ Get My Friends ____________________________

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getMyFriends")
    public ResponseEntity<List<FriendShipRequestsDto>> getMyFriends() {
        return ResponseEntity.ok(friendship_service.getMyFriends());
    }





//TODO:_______________ Accept Friendship Request ____________________________

    // TODO -----------> SWAGGER {
    @Operation(
            summary = "Accept Friend Request",
            description = "API To Accept Friend Request",

            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Friend Request Accepted Successfully"
                    ),

                    @ApiResponse(
                            responseCode = "404",
                            description = "{friend.request.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
// TODO               SWAGGER }     <---------------------

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/accept-FriendRequest/{friendship_Id}")
    public ResponseEntity<Void> acceptFriendRequest(

            @Parameter(
                    description = "Friend Request ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long friendship_Id
    ) {
        friendship_service.acceptFriendRequest(friendship_Id);
        return ResponseEntity.noContent().build();
    }



//TODO:_______________ Rejected Friendship Request ____________________________

    // TODO -----------> SWAGGER {
    @Operation(
            summary = "Reject Friend Request",
            description = "API To Reject Friend Request",

            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Friend Request Rejected Successfully"
                    ),

                    @ApiResponse(
                            responseCode = "404",
                            description = "{friend.request.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
// TODO               SWAGGER }     <---------------------

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/reject-FriendRequest/{friendship_Id}")
    public ResponseEntity<Void> rejectFriendRequest(

            @Parameter(
                    description = "Friend Request ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long friendship_Id
    ) {
        friendship_service.rejectFriendRequest(friendship_Id);
        return ResponseEntity.noContent().build();
    }





    //TODO:_______________ Cancel Friendship Request ____________________________

    // TODO -----------> SWAGGER {
    @Operation(
            summary = "Cancel Friend Request",
            description = "API To Cancel Friend Request",

            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Friend Request Cancelled Successfully"
                    ),

                    @ApiResponse(
                            responseCode = "404",
                            description = "{friend.request.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
// TODO               SWAGGER }     <---------------------

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cancel-FriendRequest/{friendship_Id}")
    public ResponseEntity<Void> cancelFriendRequest(

            @Parameter(
                    description = "Friend Request ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long friendship_Id
    ) {
        friendship_service.cancelFriendRequest(friendship_Id);
        return ResponseEntity.noContent().build();
    }




}
