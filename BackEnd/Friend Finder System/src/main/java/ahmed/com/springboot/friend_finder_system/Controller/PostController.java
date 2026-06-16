package ahmed.com.springboot.friend_finder_system.Controller;

import ahmed.com.springboot.friend_finder_system.Vm.Post_Response_Vm;
import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import ahmed.com.springboot.friend_finder_system.helper.MessageResponse;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Tag(
        name = "Post Controller",
        description = "APIs for managing posts"
)
@RestController
@RequestMapping("/api/post/")
@RequiredArgsConstructor
public class PostController {

    //TODO: Declare Service Methods

    private final Post_Service post_Service;


    //TODO:_______________ Implement Service Methods ____________________________


    //TODO:_______________ Create New Post ____________________________
    @Operation(
            summary = "Create New Post",
            description = "Create a new post for the authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Post created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "{post.validation.error}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping("creatPost")
    public ResponseEntity<Void> creatPost(@RequestBody PostDto postDto) throws URISyntaxException {
        post_Service.creatPost(postDto);
        return ResponseEntity.created(new URI("/api/posts/")).build();
    }


    //TODO:_______________ Update Post ____________________________
    @Operation(
            summary = "Update Post",
            description = "Update an existing post",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Post updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "{post.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PutMapping("updatePost")
    public ResponseEntity<Void> updatePost(
            @RequestBody PostDto postDto
    )
    {
        post_Service.updatePost(postDto);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Delete Post ____________________________
    @Operation(
            summary = "Delete Post",
            description = "Delete a post by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Post deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "{post.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("deletePost/{id}")
    public ResponseEntity<Void> deletePost(

            @Parameter(
                    description = "Post ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    )
    {
        post_Service.deletePost(id);
        return ResponseEntity.noContent().build();
    }


    //TODO:_______________ Get User Posts ____________________________
    @Operation(
            summary = "Get User Posts",
            description = "Retrieve all posts for the authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Posts retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = Post_Response_Vm.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("getUserPosts")
    public ResponseEntity<Post_Response_Vm> getUserPosts()
    {
        return ResponseEntity.ok(post_Service.getUserPosts(1));
    }
    //TODO:_______________ Get Post  ____________________________
    @Operation(
            summary = "Get Post By ID",
            description = "Retrieve a post by its ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = PostDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "{post.not.found}",
                            content = @Content(
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("getPost/{id}")
    public ResponseEntity<PostDto> getPost(

            @Parameter(
                    description = "Post ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    )
    {
        return ResponseEntity.ok(post_Service.getPostById(id));
    }

}
