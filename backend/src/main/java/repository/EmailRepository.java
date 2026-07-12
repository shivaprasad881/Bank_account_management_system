package repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.Email;



@Repository
public interface EmailRepository  extends JpaRepository<Email,Integer> {
    Email findByEmail(String email);
}