package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.InterestsDto;
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
       if(!interests.isEmpty())
       {
           throw new RuntimeException("error.interests.not.found");
       }

        return interestsMapper.toDtoList(interests);
    }


    //TODO:_______________ Set List Of Interests For User ____________________________
    @Override
    public void setListInterests(List<InterestsDto> interestsDto, Long id) {

        if(Objects.isNull(interestsDto) || interestsDto.isEmpty() || id == null)
        {
            throw new RuntimeException("error.interests.not.found");
        }

       User user =  userMapper.toEntity(user_Service.getUserById(id));

        Set<Interests> interests = interestsDto.stream().map(dto -> {
          return interests_Repo.findByCategory(dto.getCategory());
        }).collect(Collectors.toSet());

        user.setInterests(interests);
        user_Repo.save(user);
    }



    //TODO:_______________ Get List Of Interests For User ____________________________
    @Override
    public List<InterestsDto> getInterestsById(Long id) {

        if (interests_Repo.findByUsers_Id(id).isEmpty() || Objects.isNull(interests_Repo.findByUsers_Id(id)))
        {
            throw new RuntimeException("error.interests.not.found");
        }
        List<Interests> interests = interests_Repo.findByUsers_Id(id);

        return interestsMapper.toDtoList(interests);
    }
}
