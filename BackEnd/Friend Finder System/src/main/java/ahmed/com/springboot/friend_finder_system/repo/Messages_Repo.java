package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface Messages_Repo extends JpaRepository<Messages, Long> {

    @Query("SELECT m FROM Messages m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
            "ORDER BY m.createdDate ASC")
    List<Messages> findConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    Optional<List<Messages>> findByReceiver_IdOrderByCreatedDateDesc(Long receiverId);

    Optional<List<Messages>> findBySender_IdOrderByCreatedDateDesc(Long senderId);

    List<Messages> findByReceiver_IdAndIsReadFalseOrderByCreatedDateDesc(Long receiverId);

    long countByReceiver_IdAndIsReadFalse(Long receiverId);

    List<Messages> findBySender_IdAndReceiver_IdAndIsReadFalse(Long senderId, Long receiverId);
}