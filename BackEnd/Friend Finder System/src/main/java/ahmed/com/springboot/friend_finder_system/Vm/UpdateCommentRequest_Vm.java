package ahmed.com.springboot.friend_finder_system.Vm;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest_Vm {

    @NotNull(message = "error.comment.id.is.required")
    private Long commentId;

    @NotNull(message = "error.comment.content.required")
    private String content;
}
