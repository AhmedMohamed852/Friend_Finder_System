package ahmed.com.springboot.friend_finder_system.eNum;

public enum FriendshipStatus {
    PENDING,        // في الانتظار - الطلب متبعت لكن لسه مردش
    ACCEPTED,       // مقبول - تم قبول الطلب
    REJECTED,       // مرفوض - تم رفض الطلب
    BLOCKED         // محظور - أحد الطرفين حظر التاني
}
