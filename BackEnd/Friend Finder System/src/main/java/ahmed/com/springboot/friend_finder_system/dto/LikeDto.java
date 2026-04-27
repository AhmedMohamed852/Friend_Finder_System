package ahmed.com.springboot.friend_finder_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeDto {

    private Long id;


    //______________relations_______________________________

    @NotNull(message = "Post is required")
    private PostDto postDto;

    @NotNull(message = "User is required")
    private UserDto userDto;
}
