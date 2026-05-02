package ahmed.com.springboot.friend_finder_system.service.impl;

import ahmed.com.springboot.friend_finder_system.dto.DtoSimble.User_Simple_Dto;
import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;
import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import ahmed.com.springboot.friend_finder_system.mapper.NotificationMapper;
import ahmed.com.springboot.friend_finder_system.mapper.UserSimpleMapper;
import ahmed.com.springboot.friend_finder_system.models.Notification;
import ahmed.com.springboot.friend_finder_system.models.User;
import ahmed.com.springboot.friend_finder_system.repo.Notification_Repo;
import ahmed.com.springboot.friend_finder_system.repo.User_Repo;
import ahmed.com.springboot.friend_finder_system.service.Notification_Service;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import lombok.RequiredArgsConstructor;
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
            throw new RuntimeException("error.must.be.not.null.argument(id)");
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
        notification.setRead(false);
        notification.setContent("I sent you "+userSender.getFirstName()+" " + userSender.getLastName() +" a friend request.");

        notification_Repo.save(notification);

    }





    //TODO:_______________ Create FriendAccept Notification ____________________________
    @Override
    public void createFriendAcceptNotification(Long myId , Long userId) {

        if(myId == null || userId == null)
        {
            throw new RuntimeException("error.must.be.not.null.argument(id)");
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
        notification.setRead(false);
        notification.setContent("You are now friends with "+userSender.getFirstName()+" " + userSender.getLastName() +".");

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


        User userSender = userSimpleMapper.toEntity(user_Service.simple_User(myId));

        User user = userSimpleMapper.toEntity(user_Service.simple_User(userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTriggeredBy(userSender);
        notification.setType(NotificationType.FRIEND_REJECT);
        notification.setRead(false);
        notification.setContent("You rejected "+userSender.getFirstName()+" " + userSender.getLastName() +" friend request.");

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
        Optional<List<Notification>> notifications = notification_Repo.findAllByUser_Id(userId);
        if(Objects.isNull(notifications) || notifications.get().isEmpty())
        {
            throw new RuntimeException("ou.Are.Not.Found.Any.Notifications");

        }

        List<Notification> notification = notifications.get();


        List<NotificationDto> notificationDto =  notification.stream().map(notifications1 ->
        {
            NotificationDto notificationDto1 = new NotificationDto();

            User_Simple_Dto triggeredBy = new User_Simple_Dto();

            triggeredBy.setId(notifications1.getTriggeredBy().getId());
            triggeredBy.setFirstName(notifications1.getTriggeredBy().getFirstName());
            triggeredBy.setLastName(notifications1.getTriggeredBy().getLastName());
            triggeredBy.setProfilePicture(notifications1.getTriggeredBy().getProfilePicture());

            notificationDto1.setId(notifications1.getId());
            notificationDto1.setTriggeredBy(triggeredBy);
            notificationDto1.setType(notifications1.getType());
            notificationDto1.setContent(notifications1.getContent());
            notificationDto1.setRead(notifications1.isRead());

            return notificationDto1;
        }).toList();


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
