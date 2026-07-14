package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Stories;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface Stories_Repo extends JpaRepository< Stories , Long> {

    @Query("""
SELECT s
FROM Stories s
WHERE (
        s.user.id = :userId
        OR s.user.id IN (
            SELECT
                CASE
                    WHEN f.user1.id = :userId THEN f.user2.id
                    ELSE f.user1.id
                END
            FROM Friendship f
            WHERE f.status = 'ACCEPTED'
              AND (f.user1.id = :userId OR f.user2.id = :userId)
        )
      )
AND s.createdDate >= :fromTime
ORDER BY s.createdDate DESC
""")
    List<Stories> getStoriesForUser(Long userId, LocalDateTime fromTime);

    @Modifying
    @Transactional
    @Query("DELETE FROM Stories s WHERE s.createdDate < :cutoffTime")
    void deleteExpiredStories(@Param("cutoffTime") LocalDateTime cutoffTime);


}
