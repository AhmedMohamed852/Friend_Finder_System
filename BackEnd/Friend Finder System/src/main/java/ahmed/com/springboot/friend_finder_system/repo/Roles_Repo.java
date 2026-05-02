package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.eNum.RoleType;
import ahmed.com.springboot.friend_finder_system.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Roles_Repo extends JpaRepository<Roles, Long> {
    boolean existsByName(RoleType roleType);

    Roles findByName(RoleType name);
}
