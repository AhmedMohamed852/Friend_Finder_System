package ahmed.com.springboot.friend_finder_system.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "likes" , uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class Like extends BaseEntity{



    //______________ Relationships _________________

    @JoinColumn(name = "user_id" , nullable = false)
    @ManyToOne
    private User user;


    @JoinColumn(name = "post_id" , nullable = false)
    @ManyToOne
    private Post post;
}
