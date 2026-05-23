package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.Vm.LoginRequestVM;
import ahmed.com.springboot.friend_finder_system.Vm.LoginResponseVM;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.jwt.TokenHandler;
import ahmed.com.springboot.friend_finder_system.mapper.RolesMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.AuthService;
import ahmed.com.springboot.friend_finder_system.service.Role_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class Auth_Service_Impl implements AuthService {

    //TODO: Declare Service Methods

    private final User_Service userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenHandler tokenHandler;




    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Create New Account ____________________________
    @Override
    public void register(UserDto userDto)
    {
        userService.register(userDto);
    }

    //TODO:_______________ Login To My Account ____________________________

    @Override
    public LoginResponseVM login(LoginRequestVM  loginRequestVM) {

      UserDto userDto =  userService.getUserByUserName(loginRequestVM.getUsername());

      if(!passwordEncoder.matches(loginRequestVM.getPassword(),userDto.getPassword()))
      {
          throw  new RuntimeException("invalid.password.error");
      }
        Set<String> roles = userDto.getRoles().stream().map(rolesDto -> rolesDto.toString()).collect(Collectors.toSet());
        String token = tokenHandler.generateToken(userDto);

       return new LoginResponseVM(token,roles);

    }
}
