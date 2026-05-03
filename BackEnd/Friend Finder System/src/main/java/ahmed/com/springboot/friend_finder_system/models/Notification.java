package ahmed.com.springboot.friend_finder_system.models;

import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "notifications")
public class Notification extends  BaseEntity{

    private String content;

    private boolean isRead = false;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    //______________ Relationships _________________


    @JoinColumn(name = "user_id" , nullable = false)
    @ManyToOne
    private User user;


    @JoinColumn(name = "triggered_by_id" , nullable = false)
    @ManyToOne
    private User triggeredBy;


}
