package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;
import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import ahmed.com.springboot.friend_finder_system.models.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FriendShip_Repo extends JpaRepository<Friendship, Long> {

    boolean existsByUser1IdAndUser2_Id(Long user1Id, Long user2Id);

    Optional<List<Friendship>> findAllByUser2_IdAndStatus(Long user2Id, FriendshipStatus status);
}
