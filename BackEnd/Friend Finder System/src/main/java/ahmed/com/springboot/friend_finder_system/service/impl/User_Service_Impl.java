package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.RolesDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.eNum.RoleType;
import ahmed.com.springboot.friend_finder_system.GlobalExService.UserEx;
import ahmed.com.springboot.friend_finder_system.mapper.RolesMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Role_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class User_Service_Impl implements User_Service {

    //TODO: Declare Service Methods

    private final UserMapper userMapper;
    private final User_Repo user_Repo;
    private final Role_Service roleService;
    private final RolesMapper rolesMapper;
    private final UserSimpleMapper userSimpleMapper;
    private final  PasswordEncoder passwordEncoder;




    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Create New Account ____________________________
    @Override
    public void register(UserDto userDto) {

        if (user_Repo.existsByUsername(userDto.getUsername())) {
            throw UserEx.usernameAlreadyExists();
        }if (user_Repo.existsByEmail(userDto.getEmail())) {
            throw UserEx.emailAlreadyExists();
        }

        User user = userMapper.toEntity(userDto);

        RolesDto roles = roleService.getRole(RoleType.USER);
        roles.setName(RoleType.USER);
        user.setRoles(Set.of(rolesMapper.toEntity(roles)));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));


        user_Repo.save(user);
    }



    //TODO:_______________ Show My Profile ____________________________
    @Override
    public UserDto profile()
    {

       User user = user_Repo.findById(CurrentUser.currentUserId()).orElseThrow(UserEx::userNotFound);

        return userMapper.toDto(user);
    }


    //TODO:_______________ Simpl Profile ____________________________
    @Override
    public User_Simple_Dto simpleProfile() {

        User user = user_Repo.findById(CurrentUser.currentUserId()).orElseThrow(UserEx::userNotFound);
        return userSimpleMapper.toDto(user);
    }


    //TODO:_______________ Show My Profile ____________________________
    @Override
    public UserDto getUserByUserName(String username) {
        User user = user_Repo.findByUsername(username).orElseThrow(UserEx::userNotFound);
        UserDto userDto = userMapper.toDto(user);
        return userDto;

    }


    //TODO:_______________ Update My Profile ____________________________
    @Override
    public void updateProfile(UpdateProfileDto userDto)
    {
        if(userDto.getId() == null)
        {
            throw UserEx.userIdRequired();
        }

        User existingUser = user_Repo.findById(userDto.getId()).orElseThrow(UserEx::userNotFound);


           existingUser.setFirstName(userDto.getFirstName());
           existingUser.setLastName(userDto.getLastName());
           existingUser.setBio(userDto.getBio());
           existingUser.setCountry(userDto.getCountry());
           existingUser.setCity(userDto.getCity());
           existingUser.setDateOfBirth(userDto.getDateOfBirth());
           existingUser.setGender(userDto.getGender());
           existingUser.setCoverPhoto(userDto.getCoverPhoto());
           if(userDto.getImage() != null)
           {
               existingUser.setProfilePicture(userDto.getImage());
           }

        user_Repo.save(existingUser);

    }


    //TODO:_______________ Get User By Id ____________________________
    @Override
    public UserDto getUserById(Long userId) {

        if(!user_Repo.existsById(userId))
        {
            throw UserEx.userNotFound();
        }

        UserDto userDto = userMapper.toDto(user_Repo.findById(userId).orElseThrow(UserEx::userNotFound));
        userDto.setPassword(null);
        return userMapper.toDto(user_Repo.findById(userId).orElseThrow(UserEx::userNotFound));
    }


    //TODO:_______________ Delete My Account ____________________________
    @Override
    public void deleteAccount()
    {

        if(!user_Repo.existsById(CurrentUser.currentUserId()))
        {
            throw UserEx.userNotFound();
        }

        user_Repo.deleteById(CurrentUser.currentUserId());
    }



    //TODO:_______________ Delete My Account ____________________________
    @Override
    public boolean existsById(Long id)
    {
        if(!user_Repo.existsById(id))
        {
            return false;
        }
        return true;
    }






    @Override
    public User_Simple_Dto simple_User(Long id) {

        User user = user_Repo.findById(id).orElseThrow(UserEx::userNotFound);
        User_Simple_Dto user_Simple_Dto = userSimpleMapper.toDto(user);

        return user_Simple_Dto;
    }

    @Override
    public List<User_Simple_Dto> search(String key ,  int pageNumber) {


        if(key == null)
        {
            throw UserEx.userNotFound();
        }

        if(!user_Repo.existsByKeyword(key))
        {
            throw UserEx.searchKeyRequired();
        }


        validatePageNumberAndSize(pageNumber, 5);

        Pageable pageable = PageRequest.of(pageNumber - 1, 5);

        Page<User> users = user_Repo.search(key,pageable);

        List<User_Simple_Dto> user_Simple_Dto = userSimpleMapper.toDtoList(users);

        return user_Simple_Dto;
    }






    //TODO _________________validatePageNumberAndSize______________________
//TODO ________________________________________________________________
    boolean validatePageNumberAndSize(int pageNumber, int pageSize)
    {
        if (pageNumber < 1 || pageSize <= 0)
        {
            throw UserEx.invalidPageNumber();
        }
        return true;
    }

}
