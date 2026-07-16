package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;

import java.util.List;

public interface User_Service {

    void register(UserDto userDto);

    UserDto profile();
    User_Simple_Dto simpleProfile();

    UserDto getUserByUserName(String username);

    User_Simple_Dto simple_User(Long id);

    List<User_Simple_Dto> search(String key ,  int pageNumber);

    void updateProfile(UpdateProfileDto userDto);

    UserDto getUserById(Long UserId);


    void deleteAccount();

    boolean existsById(Long id);


}
