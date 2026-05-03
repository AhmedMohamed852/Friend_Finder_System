package ahmed.com.springboot.friend_finder_system.service.impl;


import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;
import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import ahmed.com.springboot.friend_finder_system.mapper.MapperSimble.ToUserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.mapper.NotificationMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.models.Notification;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Notification_Repo;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
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
    private final User_Service  user_Service;
    private final UserSimpleMapper userSimpleMapper;
    private final ToUserSimpleMapper toUserSimpleMapper;
    private final User_Repo user_Repo;
    private final ResourceBundleMessageSource messageSource;  // ✅ Inject MessageSource



    //TODO:_______________ Implement Service Methods ____________________________





    //TODO:_______________ Create Notification ____________________________
    @Override
    public void createNotification(NotificationDto notificationDTO) {
        //
    }




    //TODO:_______________ Create FriendRequest Notification ____________________________
    @Override
    public void createFriendRequestNotification(Long myId , Long userId) {

        if(myId == null || userId == null)
        {
            throw new RuntimeException("error.must.be.not.null.argument.id");
        }

        if (notification_Repo.existsByUser_IdAndTriggeredBy_Id_AndType(userId ,myId , NotificationType.FRIEND_REQUEST)){
            throw new RuntimeException("error.this.notification.exist");
        }

        User userSender = userSimpleMapper.toEntity(user_Service.simple_User(myId));

        User user = userSimpleMapper.toEntity(user_Service.simple_User(userId));


        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTriggeredBy(userSender);
        notification.setType(NotificationType.FRIEND_REQUEST);


       // ✅ Use message key
        String content = messageSource.getMessage("notification.friend.request",
        new Object[]{userSender.getFirstName(), userSender.getLastName()},
        LocaleContextHolder.getLocale());
        notification.setContent(content);

        notification_Repo.save(notification);

    }





    //TODO:_______________ Create FriendAccept Notification ____________________________
    @Override
    public void createFriendAcceptNotification(Long myId , Long userId) {

        if(myId == null || userId == null)
        {
            throw new RuntimeException("error.must.be.not.null.argument.id");
        }

        if(notification_Repo.existsByUser_IdAndTriggeredBy_IdAndType(myId , userId ,NotificationType.FRIEND_ACCEPTED )){
            throw new RuntimeException("error.this.notification.exist");
        }


        User userSender = userSimpleMapper.toEntity(user_Service.simple_User(myId));

        User user = userSimpleMapper.toEntity(user_Service.simple_User(userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTriggeredBy(userSender);
        notification.setType(NotificationType.FRIEND_ACCEPTED);


        String content = messageSource.getMessage("notification.friend.accepted",
        new Object[]{userSender.getFirstName(), userSender.getLastName()},
        LocaleContextHolder.getLocale());
        notification.setContent(content);


        notification_Repo.save(notification);

    }




    //TODO:_______________ Create FriendReject Notification ____________________________
    @Override
    public void createFriendRejectNotification(Long myId , Long userId)
    {
        if(myId == null || userId == null)
        {
            throw new RuntimeException("error.must.be.not.null.argument(id)");
        }

        if(notification_Repo.existsByUser_IdAndTriggeredBy_IdAndType(myId , userId ,NotificationType.FRIEND_REJECT)){
            throw new RuntimeException("error.this.notification.exist");
        }


        User sender = user_Repo.findById(myId).orElseThrow(() -> new RuntimeException("error.user.not.found"));

        User receiver = user_Repo.findById(userId).orElseThrow(() -> new RuntimeException("error.user.not.found"));

        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setTriggeredBy(sender);
        notification.setType(NotificationType.FRIEND_REJECT);


        String content = messageSource.getMessage("notification.friend.rejected",
        new Object[]{sender.getFirstName(), sender.getLastName()},
        LocaleContextHolder.getLocale());
        notification.setContent(content);

        notification_Repo.save(notification);

    }





    //TODO:_______________ Create PostLiked Notification ____________________________
    @Override
    public void createPostLikedNotification(Long postId, Long userId)
    {

    }



    //TODO:_______________ Create Comment Notification ____________________________
    @Override
    public void createCommentNotification(Long postId, Long commentId, Long userId) {

    }



    //TODO:_______________ Get User Notification ____________________________
    @Override
    public List<NotificationDto> getUserNotifications(Long userId)
    {
        List<Notification> notifications = notification_Repo.findAllByUser_Id(userId)
        .orElse(Collections.emptyList());

        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }

        List<NotificationDto> notificationDto =  notifications.stream().map(notification -> {
        notification.setTriggeredBy(toUserSimpleMapper.toUser(user_Service.simple_User(notification.getTriggeredBy().getId())));
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
}
