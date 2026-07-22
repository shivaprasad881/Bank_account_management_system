package repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   
    User findByAccnoAndPassword(String accno, String password);
    User findByPhonenumberAndPassword(String phonenumber, String password);
    User findByAccno(String accno);
    User findByPhonenumber(String phonenumber);
    User findByEmail(String email);

    List<User> findAll();

    @Query("SELECT u.userid, u.uname, u.age, u.city, u.accno, u.phonenumber, u.email, u.balance FROM User u")
List<Object[]> fetchUsersByManager();

    @Query("SELECT u.userid, u.uname, u.accno, u.phonenumber, u.balance FROM User u")
List<Object[]> fetchUsersByCashier();

    @Query("SELECT u.userid, u.uname, u.age, u.city, u.accno, u.phonenumber, u.email, u.balance, u.availableAt, u.lastActiveAt, u.accountStatus FROM User u")
List<Object[]> fetchUsersByClerk();


    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.joinDate) = CURDATE()")
long countUsersJoinedToday();

}