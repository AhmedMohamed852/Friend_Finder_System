package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.dto.StoriesDto;
import ahmed.com.springboot.friend_finder_system.service.Stories_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Stories Controller",
        description = "APIs for creating and retrieving user stories"
)
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/stories")
@RequiredArgsConstructor
@RestController
public class Stories_Controller {


    //TODO: Declare Service Methods

    private final Stories_Service storiesService;

    //TODO:_______________ Implement Service Methods ____________________________




    //TODO:_______________ new Story ____________________________
    @Operation(
            summary = "Create a new story",
            description = "Allows an authenticated user to publish a new story (image or video)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Story published successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input payload / Validation failed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/newStory")
    public ResponseEntity<Void> newStory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the story to be created", required = true)
            @RequestBody @Valid StoriesDto storiesDto) {
        storiesService.newStory(storiesDto);
        return ResponseEntity.noContent().build();
    }



    //TODO:_______________ Get My Story ____________________________
    @Operation(
            summary = "Get active stories",
            description = "Retrieves a list of active stories for the current authenticated user and their friends."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Stories retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = StoriesDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getStories")
    public ResponseEntity<List<StoriesDto>> getMyStory() {
        return ResponseEntity.ok(storiesService.getStories());
    }

}