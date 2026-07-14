package ahmed.com.springboot.friend_finder_system.Vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentRequest_Vm {
    @NotNull(message = "error.comment.postId.required")
    Long postId;

    @NotNull(message = "error.comment.content.required")
    String content;
}
