package ahmed.com.springboot.friend_finder_system.service;

import ahmed.com.springboot.friend_finder_system.dto.NotificationDto;

import java.util.List;

public interface Notification_Service {

   void createNotification(NotificationDto notificationDTO);

    void createFriendRequestNotification(Long myId , Long userId); // الشخص الي رايحله الرساله (UserID)

    void createFriendAcceptNotification(Long myId , Long userId);

    void createFriendRejectNotification(Long myId , Long userId);

    void createPostLikedNotification(Long postId, Long userId);

    void createCommentNotification(Long postId, Long commentId, Long userId);

    List<NotificationDto> getUserNotifications(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void deleteNotification(Long notificationId);




}
