package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.PostPrivacy;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {

    private  Long id;

    @NotBlank(message = "Content is required")
    private String content;

    private String imageOrVideo;

    private Integer countLikes;

    private Integer countComments;

    private PostPrivacy privacy;
}
