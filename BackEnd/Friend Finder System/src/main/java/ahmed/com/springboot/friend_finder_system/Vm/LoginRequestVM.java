package ahmed.com.springboot.friend_finder_system.Vm;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestVM {
    @NotBlank(message = "error.auth.username.required")
    private String username;

    @NotBlank(message = "error.auth.password.required")
    private String password;
}
