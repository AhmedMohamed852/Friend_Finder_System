package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.PostPrivacy;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {

    private  Long id;

    @NotBlank(message = "Content is required")
    private String content;

    private Set<MediaDto> media;

    private Integer countLikes;

    private Integer countComments;

    private PostPrivacy privacy;





}
