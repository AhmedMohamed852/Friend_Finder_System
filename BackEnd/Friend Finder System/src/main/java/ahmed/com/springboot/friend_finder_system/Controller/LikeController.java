package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.service.Like_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
@Tag(
        name = "Like Controller",
        description = "APIs for managing post likes"
)
public class LikeController {

    private final Like_Service likeService;

    // ===================== ADD LIKE =====================

    @Operation(
            summary = "Toggle Like on Post",
            description = "Add or remove like from a post depending on current state"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Like updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{postId}")
    public ResponseEntity<Void> toggleLike(

            @Parameter(
                    description = "ID of the post to like/unlike",
                    example = "10",
                    required = true
            )
            @PathVariable Long postId
    ) {
        likeService.toggleLike(postId);
        return ResponseEntity.noContent().build();
    }
}