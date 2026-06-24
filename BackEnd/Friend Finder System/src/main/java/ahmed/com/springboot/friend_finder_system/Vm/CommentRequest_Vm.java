package ahmed.com.springboot.friend_finder_system.Vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentRequest_Vm {
    @NotNull(message = "muste.be.Id.notNull")
    Long postId;


    @NotNull(message = "muste.be.comment.notNull")
    String content;
}
