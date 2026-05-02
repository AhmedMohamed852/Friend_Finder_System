package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;

public interface User_Service {

    void register(UserDto userDto);

    void login(String username, String password);

    UserDto profile(Long id);

    User_Simple_Dto simple_User(Long id);

    void updateProfile(UpdateProfileDto userDto);


    void deleteAccount(Long id);

    boolean existsById(Long id);


/*
    UserDto getUserById(Long id);

    UserDto getUserByUsername(String username);

    UserDto  search(String usernameOrEmail);*/



}
