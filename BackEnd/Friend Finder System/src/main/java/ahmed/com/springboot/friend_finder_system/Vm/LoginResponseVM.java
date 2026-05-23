package ahmed.com.springboot.friend_finder_system.Vm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class LoginResponseVM {
    String token;
    Set<String> roles;
}
