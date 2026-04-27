package ahmed.com.springboot.friend_finder_system.eNum;

public enum NotificationType {
    FRIEND_REQUEST,      // طلب صداقة جديد
    FRIEND_ACCEPTED,     // تم قبول طلبك
    NEW_MESSAGE,         // رسالة جديدة
    POST_LIKED,          // إعجاب على منشورك
    POST_COMMENTED,      // تعليق على منشورك
    COMMENT_REPLY,       // رد على تعليقك
    MENTION,             // شخص منشنك
    SUGGESTED_FRIEND     // اقتراح صديق جديد
}
