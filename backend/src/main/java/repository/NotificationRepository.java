package repository;

import model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByReceiver(String receiver);
    
    // List<Notification> findByReceiverAndViewed(String receiver, boolean viewed);

    @Query("SELECT n FROM Notification n WHERE n.receiver = :receiver AND n.viewed = :viewed ORDER BY n.createdAt DESC LIMIT :k")
    List<Notification> findLatestKByReceiverAndViewed(
        @Param("receiver") String receiver,
        @Param("viewed") boolean viewed,
        @Param("k") int k
    );
}