package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.UserDto;

public interface User_Service {

    void register(UserDto userDto);

    void login(String username, String password);

    void updateProfile(UserDto userDto);

    void deleteUser(Long id);

    UserDto getUserById(Long id);

    UserDto getUserByUsername(String username);

    UserDto  search(String usernameOrEmail);




}
