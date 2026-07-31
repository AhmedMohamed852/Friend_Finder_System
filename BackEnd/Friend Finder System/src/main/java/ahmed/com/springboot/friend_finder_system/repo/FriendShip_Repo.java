package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.dto.FriendshipDto;
import ahmed.com.springboot.friend_finder_system.eNum.FriendshipStatus;
import ahmed.com.springboot.friend_finder_system.models.Friendship;
import ahmed.com.springboot.friend_finder_system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FriendShip_Repo extends JpaRepository<Friendship, Long> {

    boolean existsByUser1IdAndUser2_Id(Long user1Id, Long user2Id);

    Optional<List<Friendship>> findAllByUser2_IdAndStatus(Long user2Id, FriendshipStatus status);

    List<Friendship> findByUser1_IdOrUser2_IdAndStatus(Long user1Id, Long user2Id, FriendshipStatus status);

    Optional<List<Friendship>> findAllByUser1_IdAndStatus(Long userId, FriendshipStatus friendshipStatus);

    Optional<List<Friendship>> findByStatusAndUser1_IdOrStatusAndUser2_Id(
            FriendshipStatus status1,
            Long user1Id,
            FriendshipStatus status2,
            Long user2Id
    );


    @Query("""
    SELECT f
    FROM Friendship f
    WHERE f.status = 'ACCEPTED'
      AND (
            (
                f.user1.id = :userId
                AND (
                    LOWER(f.user2.firstName) LIKE LOWER(CONCAT(:keyword, '%'))
                    OR LOWER(CONCAT(f.user2.firstName, ' ', f.user2.lastName))
                        LIKE LOWER(CONCAT(:keyword, '%'))
                )
            )
            OR
            (
                f.user2.id = :userId
                AND (
                    LOWER(f.user1.firstName) LIKE LOWER(CONCAT(:keyword, '%'))
                    OR LOWER(CONCAT(f.user1.firstName, ' ', f.user1.lastName))
                        LIKE LOWER(CONCAT(:keyword, '%'))
                )
            )
      )
""")
    List<Friendship> search(@Param("userId") Long userId,
                            @Param("keyword") String keyword);

    boolean existsByUser1IdOrUser2_Id(Long user1  , Long user2);
}
