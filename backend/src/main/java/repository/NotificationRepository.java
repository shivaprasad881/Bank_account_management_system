package repository;

import model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByReceiver(String receiver);
    List<Notification> findByReceiverAndViewed(String receiver, boolean viewed);
}