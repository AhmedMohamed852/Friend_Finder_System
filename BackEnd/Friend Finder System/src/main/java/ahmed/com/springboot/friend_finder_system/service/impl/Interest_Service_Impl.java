package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.GlobalExService.InterestsEx;
import ahmed.com.springboot.friend_finder_system.dto.InterestsDto;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.InterestsMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Interests;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Interests_Repo;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Interest_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Interest_Service_Impl implements Interest_Service {


    //TODO: Declare Service Methods

    private final Interests_Repo interests_Repo;
    private final InterestsMapper interestsMapper;
    private final User_Service user_Service;
    private final UserMapper userMapper;
    private final User_Repo user_Repo;


    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ Get All Interests  ____________________________
    @Override
    public List<InterestsDto> getAllInterests() {
       List<Interests> interests = interests_Repo.findAll();
       if(interests.isEmpty())
       {
           throw InterestsEx.InterestsNotFoundException();
       }

        return interestsMapper.toDtoList(interests);
    }


    //TODO:_______________ Set List Of Interests For User ____________________________
    @Override
    public void setListInterests(List<InterestsDto> interestsDto) {

        if(Objects.isNull(interestsDto) || interestsDto.isEmpty() )
        {
            throw InterestsEx.InterestsNotFoundException();
        }

       User user =  userMapper.toEntity(user_Service.getUserById(CurrentUser.currentUserId()));

        Set<Interests> interests = interestsDto.stream().map(dto -> interests_Repo.findByCategory(dto.getCategory())).collect(Collectors.toSet());

        user.setInterests(interests);
        user_Repo.save(user);
    }



    //TODO:_______________ Get List Of Interests For User ____________________________
    @Override
    public List<InterestsDto> getInterestsById(Long id) {

        if (interests_Repo.findByUsers_Id(id).isEmpty() || Objects.isNull(interests_Repo.findByUsers_Id(id)))
        {
            throw InterestsEx.InterestsNotFoundException();
        }
        List<Interests> interests = interests_Repo.findByUsers_Id(id);

        return interestsMapper.toDtoList(interests);
    }



}
