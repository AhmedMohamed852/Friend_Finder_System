package ahmed.com.springboot.friend_finder_system.models;

import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "friendships" ,
        uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = {"user_1_id", "user_2_id"}))
public class Friendship extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_1_id")
    private User user1;


    @ManyToOne
    @JoinColumn(name = "user_2_id")
    private User user2;


    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)
    private FriendshipStatus status;


    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

}
