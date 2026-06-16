package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.Vm.LoginRequestVM;
import ahmed.com.springboot.friend_finder_system.Vm.LoginResponseVM;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;

public interface AuthService {

    void register(UserDto userDto);

     LoginResponseVM login(LoginRequestVM loginRequestVM);
}
