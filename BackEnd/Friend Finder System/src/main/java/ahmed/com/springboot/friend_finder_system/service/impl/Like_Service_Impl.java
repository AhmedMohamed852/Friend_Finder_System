package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.mapper.LikeMapper;
import ahmed.com.springboot.friend_finder_system.mapper.NotificationMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.models.Like;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Like_Repo;
import ahmed.com.springboot.friend_finder_system.service.Like_Service;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class Like_Service_Impl implements Like_Service {


    //TODO: Declare Service Methods


    private final Like_Repo likeRepo;
    private final Post_Service postService;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final Notification_Service notificationService;

    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Create Notification ____________________________
    @Override
    public void toggleLike(Long postId) {

        Post post = postMapper.toEntity(postService.getPostById(postId));

        if(Objects.isNull(post))
        {
            throw new RuntimeException("error.post.not.found");
        }

        if(likeRepo.existsByUserIdAndPostId(currentUser() , post.getId()))
        {
            Optional<Like> like = Optional.of(likeRepo.findByUserIdAndPostId(currentUser(), post.getId()).orElseThrow());
            likeRepo.delete(like.get());
            post.setCountLikes(post.getCountLikes()-1);
            postService.savePost(postMapper.toDto(post));
            return;
        }

        if(Objects.isNull(post.getCountLikes()))
        {
            post.setCountLikes(1);
        }else{
            post.setCountLikes(post.getCountLikes()+1);
        }


        User user = userMapper.toEntity((UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Like newLik = new Like();


       newLik.setPost(post);
       newLik.setUser(user);

       likeRepo.save(newLik);

       postService.savePost(postMapper.toDto(post));
       notificationService.createPostLikedNotification(post.getId());



    }
    //TODO:_______________ is Liked By Me ____________________________
    @Override
    public boolean isLikedByMe(Long postId) {
        return likeRepo.existsByPostIdAndUserId(postId, currentUser());
    }

    public Set<Long> getLikedPostIds(List<Long> postIds, Long userId) {
        return new HashSet<>(likeRepo.findLikedPostIds(userId, postIds));
    }


    //TODO:_______________ Get CurrentUser ____________________________
    public Long currentUser()
    {
        UserDto currentUser = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        return userId;
    }
}
