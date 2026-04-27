package ahmed.com.springboot.friend_finder_system.models;

import ahmed.com.springboot.friend_finder_system.eNum.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Roles extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "name" , unique = true)
    @NotNull(message = "Role is required")
    private RoleType name;


    // _______________relations__________________________________

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
