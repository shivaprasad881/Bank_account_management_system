package repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   
    User findByAccnoAndPassword(String accno, String password);
    User findByPhonenumberAndPassword(String phonenumber, String password);
    User findByAccno(String accno);
    User findByPhonenumber(String phonenumber);

}