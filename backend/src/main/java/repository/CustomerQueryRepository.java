package repository;

import model.CustomerQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerQueryRepository extends JpaRepository<CustomerQuery, Integer> {
    List<CustomerQuery> findByAccno(String accno);
    
}