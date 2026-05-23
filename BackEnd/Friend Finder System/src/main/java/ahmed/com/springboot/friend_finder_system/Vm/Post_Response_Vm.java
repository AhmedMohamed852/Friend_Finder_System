package ahmed.com.springboot.friend_finder_system.Vm;

import ahmed.com.springboot.friend_finder_system.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post_Response_Vm {

    private List<PostDto> posts;

    private Long totalPosts;
}
