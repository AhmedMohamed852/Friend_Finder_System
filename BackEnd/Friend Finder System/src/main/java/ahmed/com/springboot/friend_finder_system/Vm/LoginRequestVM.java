package ahmed.com.springboot.friend_finder_system.Vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestVM {
    private String username;
    private String password;
}
