package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.Vm.CommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.Vm.UpdateCommentRequest_Vm;
import ahmed.com.springboot.friend_finder_system.dto.CommentDto;
import ahmed.com.springboot.friend_finder_system.service.Comment_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Comment Controller",
        description = "APIs for managing comments requests"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    //TODO: Declare Service Methods

    private final Comment_Service commentService;


    //TODO:_______________ Implement Service Methods ____________________________


    //TODO:_______________ Get Comments ____________________________
    @Operation(
            summary = "Get comments by Post ID",
            description = "Retrieves a paginated list of top-level comments associated with a specific post."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/CommentsByPostId/{postId}")
    public ResponseEntity<List<CommentDto>> commentsByPostId(
            @Parameter(description = "ID of the post to retrieve comments for", required = true, example = "1")
            @PathVariable Long postId,
            @Parameter(description = "Page number for pagination", required = true, example = "1")
            @RequestParam int pageNumber) {

        return ResponseEntity.ok(commentService.getComments(postId, pageNumber));
    }


    //TODO:_______________ New Comment ____________________________
    @Operation(
            summary = "Create a new comment",
            description = "Adds a new comment to a specific post."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload / Validation failed", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/newComment")
    public ResponseEntity<Void> newComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the new comment", required = true)
            @RequestBody @Valid CommentRequest_Vm commentRequestVm){
        commentService.createComment(commentRequestVm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    //TODO:_______________ Update Comment ____________________________
    @Operation(
            summary = "Update an existing comment",
            description = "Updates the content of an existing comment. Users can only update their own comments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateComment")
    public ResponseEntity<Void> updateComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the comment update request", required = true)
            @RequestBody @Valid UpdateCommentRequest_Vm commentRequestVm) {

        commentService.updateComment(commentRequestVm);
        return ResponseEntity.ok().build();
    }


    //TODO:_______________ Reply To Comment ____________________________
    @Operation(
            summary = "Reply to an existing comment",
            description = "Creates a threaded reply under an existing comment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reply created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parent comment not found", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/replyToComment")
    public ResponseEntity<Void> replyToComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the reply", required = true)
            @RequestBody @Valid UpdateCommentRequest_Vm commentRequestVm) {

        commentService.replyToComment(commentRequestVm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    //TODO:_______________ Delete Comment ____________________________
    @Operation(
            summary = "Delete a comment",
            description = "Deletes an existing comment or reply by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID of the comment to be deleted", required = true, example = "10")
            @PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }


    //TODO:_______________ Get Replies ____________________________
    @Operation(
            summary = "Get replies to a comment",
            description = "Retrieves a paginated list of replies (nested comments) associated with a specific parent comment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Replies retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Parent comment not found", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<List<CommentDto>> getReplies(
            @Parameter(description = "ID of the parent comment to fetch replies for", required = true, example = "10")
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "1") int pageNumber) {

        return ResponseEntity.ok(
                commentService.getReplies(commentId, pageNumber)
        );
    }
}