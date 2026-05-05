package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.eNum.InterestCategory;
import ahmed.com.springboot.friend_finder_system.models.Interests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Interests_Repo extends JpaRepository<Interests, Long> {

    boolean existsByCategory(InterestCategory interest);

    Interests findByCategory(InterestCategory category);

    List<Interests> findByUsers_Id(Long id);
}
