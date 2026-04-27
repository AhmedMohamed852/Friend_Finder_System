package ahmed.com.springboot.friend_finder_system.models;

import ahmed.com.springboot.friend_finder_system.eNum.InterestCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interests")
public class Interests extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private InterestCategory category;


    @Column(name = "icon", length = 50)
    private String icon;


    // _______________relations__________________________________

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "interests")
    private Set<User> users = new HashSet<>();
}
