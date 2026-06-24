package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.UpdateProfileDto;
import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.RolesDto;
import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.eNum.RoleType;
import ahmed.com.springboot.friend_finder_system.mapper.RolesMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Role_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("error.username.already.exists");
        }if (user_Repo.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("error.email.already.exists");
        }

        User user = userMapper.toEntity(userDto);

        RolesDto roles = roleService.getRole(RoleType.USER);
        roles.setName(RoleType.USER);
        user.setRoles(Set.of(rolesMapper.toEntity(roles)));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));


        user_Repo.save(user);
    }



    //TODO:_______________ Login To My Account ____________________________
/*    @Override
    public void login(String username, String password)
    {
        if(!(user_Repo.existsByUsername(username) && user_Repo.existsByPassword(password)))
        {
            throw new RuntimeException("error.invalid.username.or.password");
        }
    *//*

        if (!passwordEncoder.matches(password, user.getPassword()))
        {
        throw new InvalidCredentialsException("error.invalid.username.or.password");
         }
     *//*
        // return Token
    }*/


    //TODO:_______________ Show My Profile ____________________________
    @Override
    public UserDto profile()
    {

       User user = user_Repo.findById(getUserId()).orElseThrow(() -> new RuntimeException("error.user.not.found"));

        return userMapper.toDto(user);
    }


    //TODO:_______________ Simpl Profile ____________________________
    @Override
    public User_Simple_Dto simpleProfile() {

        User user = user_Repo.findById(getUserId()).orElseThrow(() -> new RuntimeException("error.user.not.found"));
        return userSimpleMapper.toDto(user);
    }


    //TODO:_______________ Show My Profile ____________________________
    @Override
    public UserDto getUserByUserName(String username) {
        User user = user_Repo.findByUsername(username).orElseThrow(() -> new RuntimeException("user.not.found"));
        UserDto userDto = userMapper.toDto(user);
        return userDto;

    }


    //TODO:_______________ Update My Profile ____________________________
    @Override
    public void updateProfile(UpdateProfileDto userDto)
    {
        if(userDto.getId() == null)
        {
            throw new RuntimeException("error.user.id.is.required");
        }

        User existingUser = user_Repo.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("error.user.not.found"));

           existingUser.setFirstName(userDto.getFirstName());
           existingUser.setLastName(userDto.getLastName());
           existingUser.setBio(userDto.getBio());
           existingUser.setCountry(userDto.getCountry());
           existingUser.setCity(userDto.getCity());
           existingUser.setDateOfBirth(userDto.getDateOfBirth());
           existingUser.setGender(userDto.getGender());
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
            throw new RuntimeException("error.user.not.found");
        }
        return userMapper.toDto(user_Repo.findById(userId).orElseThrow(() -> new RuntimeException("error.user.not.found")));
    }


    //TODO:_______________ Delete My Account ____________________________
    @Override
    public void deleteAccount()
    {

        if(!user_Repo.existsById(getUserId()))
        {
            throw new RuntimeException("error.user.not.found");
        }

        user_Repo.deleteById(getUserId());
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

        User user = user_Repo.findById(id).orElseThrow(() -> new RuntimeException("error.user.not.found"));
        User_Simple_Dto user_Simple_Dto = userSimpleMapper.toDto(user);

        return user_Simple_Dto;
    }





    public Long getUserId()
    {
        UserDto currentUser = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        return userId;
    }
}
