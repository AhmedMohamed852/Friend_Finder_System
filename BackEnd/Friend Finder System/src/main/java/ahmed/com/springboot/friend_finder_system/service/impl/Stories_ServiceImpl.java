package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.GlobalExService.StoriesEx;
import ahmed.com.springboot.friend_finder_system.dto.StoriesDto;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.StoriesMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Stories;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Stories_Repo;
import ahmed.com.springboot.friend_finder_system.service.Stories_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class Stories_ServiceImpl implements Stories_Service {

    //TODO: Declare Service Methods

    private final StoriesMapper storiesMapper;
    private final User_Service userService;
    private final UserMapper userMapper;
    private final Stories_Repo storiesRepo;


    //TODO:_______________ Implement Service Methods ____________________________



    //TODO:_______________ New Story  ____________________________
    @Override
    public void newStory(StoriesDto storiesDto) {

        if(Objects.isNull(storiesDto))
        {
            throw StoriesEx.storyUploadFailed();
        }

        Stories stories = storiesMapper.toEntity(storiesDto);

        User user = userMapper.toEntity(userService.getUserById(CurrentUser.currentUserId()));
        stories.setUser(user);

        user.setStories(stories);

        storiesRepo.save(stories);
    }

    @Override
    public List<StoriesDto> getStories() {

        List<Stories> stories = storiesRepo.getStoriesForUser(CurrentUser.currentUserId() , LocalDateTime.now().minusMinutes(10));

        if(Objects.isNull(stories))
        {
            throw StoriesEx.storyNotFound();
        }

        return storiesMapper.toDtoList(stories);
    }


}
