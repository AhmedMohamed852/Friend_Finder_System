package ahmed.com.springboot.friend_finder_system.models;

import ahmed.com.springboot.friend_finder_system.eNum.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "stories")
public class Stories extends BaseEntity{

    private String url;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

}
