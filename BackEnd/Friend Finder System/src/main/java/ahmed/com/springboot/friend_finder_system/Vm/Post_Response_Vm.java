package ahmed.com.springboot.friend_finder_system.Vm;

import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Post_Response_Vm",
        description = "View Model wrapping the paginated list of posts and the total count for pagination"
)
public class Post_Response_Vm {

    @ArraySchema(
            schema = @Schema(
                    implementation = PostDto.class,
                    description = "List of posts on the current page"
            )
    )
    private List<PostDto> posts;

    @Schema(
            description = "Total number of posts available in the system matching the query",
            example = "150"
    )
    private Long totalPosts;
}