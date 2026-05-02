package ahmed.com.springboot.friend_finder_system.repo;

import ahmed.com.springboot.friend_finder_system.eNum.NotificationType;
import ahmed.com.springboot.friend_finder_system.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface Notification_Repo extends JpaRepository<Notification, Long> {


    boolean existsByUser_IdAndTriggeredBy_Id_AndType(Long userId, Long triggeredById, NotificationType type);

    boolean existsByUser_IdAndTriggeredBy_IdAndType(Long userId, Long triggeredById, NotificationType type);

   Optional<List<Notification>> findAllByUser_Id(Long userId);
}
