package ahmed.com.springboot.friend_finder_system.service.impl;


import ahmed.com.springboot.friend_finder_system.GlobalExService.NotificationEx;
import ahmed.com.springboot.friend_finder_system.GlobalExService.UserEx;
import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;
import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import ahmed.com.springboot.friend_finder_system.globalCurrentUserId.CurrentUser;
import ahmed.com.springboot.friend_finder_system.mapper.MapperSimble.ToUserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.mapper.NotificationMapper;
import ahmed.com.springboot.friend_finder_system.mapper.PostMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.models.Notification;
import ahmed.com.springboot.friend_finder_system.models.Post;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Notification_Repo;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import ahmed.com.springboot.friend_finder_system.service.Post_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class Notification_Service_Impl implements Notification_Service {

    //TODO: Declare Service Methods

    private final Notification_Repo notification_Repo;
    private  final NotificationMapper notificationMapper;
    private final User_Service userService;
    private final UserMapper userMapper;
    private final UserSimpleMapper userSimpleMapper;
    private final ToUserSimpleMapper toUserSimpleMapper;
    private final User_Repo user_Repo;
    @Lazy
    private final Post_Service postService;
    private final PostMapper postMapper;
    private final ResourceBundleMessageSource messageSource;



    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Create Notification ____________________________
    @Override
    public void createNotification(NotificationDto notificationDTO) {
        //
    }




    //TODO:_______________ Create FriendRequest Notification ____________________________
    @Override
    public void createFriendRequestNotification( Long userId) {

        if( userId == null)
        {
            throw NotificationEx.userIdRequired();
        }

        /*if (notification_Repo.existsByUser_IdAndTriggeredBy_Id_AndType(userId ,currentUser() , NotificationType.FRIEND_REQUEST)){
            throw new RuntimeException("error.this.notification.exist");
        }*/

        User userSender = userSimpleMapper.toEntity(userService.simple_User(CurrentUser.currentUserId()));

        User user = userSimpleMapper.toEntity(userService.simple_User(userId));


        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTriggeredBy(userSender);
        notification.setType(NotificationType.FRIEND_REQUEST);


        String content = buildContent("notification.friend.rejected", userSender.getFirstName(), userSender.getLastName());
        notification.setContent(content);

        notification_Repo.save(notification);

    }



    //TODO:_______________ Create FriendAccept Notification ____________________________
    @Override
    public void createFriendAcceptNotification( Long userId) {

        if( userId == null)
        {
            throw NotificationEx.userIdRequired();
        }

        if(notification_Repo.existsByUser_IdAndTriggeredBy_IdAndType(CurrentUser.currentUserId() , userId ,NotificationType.FRIEND_ACCEPTED )){
            throw NotificationEx.notificationAlreadyExists();
        }


        User userSender = userSimpleMapper.toEntity(userService.simple_User(CurrentUser.currentUserId()));

        User user = userSimpleMapper.toEntity(userService.simple_User(userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTriggeredBy(userSender);
        notification.setType(NotificationType.FRIEND_ACCEPTED);


        String content = buildContent("notification.friend.accepted", userSender.getFirstName(), userSender.getLastName());
        notification.setContent(content);


        notification_Repo.save(notification);

    }



    //TODO:_______________ Create FriendReject Notification ____________________________
    @Override
    public void createFriendRejectNotification(Long userId)
    {
        if( userId == null)
        {
            throw NotificationEx.userIdRequired();
        }



        User sender = user_Repo.findById(CurrentUser.currentUserId()).orElseThrow(UserEx::userNotFound);

        User receiver = user_Repo.findById(userId).orElseThrow(UserEx::userNotFound);

        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setTriggeredBy(sender);
        notification.setType(NotificationType.FRIEND_REJECT);


        String content = buildContent("notification.friend.rejected", sender.getFirstName(), sender.getLastName());
        notification.setContent(content);

        notification_Repo.save(notification);

    }





    //TODO:_______________ Create PostLiked Notification ____________________________
    @Override
    public void createPostLikedNotification(Long postId)
    {
        Post post = postMapper.toEntity(postService.getPostById(postId));


        // todo ==> we have Exception hir ______________
        if(notification_Repo.existsByUser_IdAndTriggeredBy_IdAndPostIdAndType( CurrentUser.currentUserId() ,post.getAuthor().getId() , post.getId() ,NotificationType.POST_LIKED)){

           // throw new RuntimeException("error.this.notification.exist");
            return;
        }

        User user = userMapper.toEntity(userService.getUserById(post.getAuthor().getId()));
        User currentUser = userMapper.toEntity(userService.getUserById(CurrentUser.currentUserId()));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTriggeredBy(currentUser);
        notification.setType(NotificationType.POST_LIKED);
        notification.setPostId(post.getId());


        String content = buildContent("notification.post.liked", currentUser.getFirstName(), currentUser.getLastName());
        notification.setContent(content);

        notification_Repo.save(notification);


    }



    //TODO:_______________ Create Comment Notification ____________________________
    @Override
    public void createCommentNotification(Long postId, Long commentId, Long userId) {

    }



    //TODO:_______________ Get User Notification ____________________________
    @Override
    public List<NotificationDto> getUserNotifications()
    {

        List<Notification> notifications = notification_Repo.findByUser_id(CurrentUser.currentUserId())
        .orElse(Collections.emptyList());

        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }

        List<NotificationDto> notificationDto =  notifications.stream().map(notification -> {
        notification.setTriggeredBy(toUserSimpleMapper.toUser(userService.simple_User(notification.getTriggeredBy().getId())));
        return notificationMapper.toDto(notification);}).toList();


        return notificationDto;
    }



    //TODO:_______________ Mark As Read Notification ____________________________
    @Override
    public void markAsRead(Long notificationId, Long userId) {

    }



    //TODO:_______________ Delete Notification ____________________________
    @Override
    public void deleteNotification(Long notificationId) {

    }

    private String buildContent(String key, String firstName, String lastName)
    {
        return messageSource.getMessage(
                key,
                new Object[]{firstName, lastName},
                LocaleContextHolder.getLocale()
        );
    }
}
