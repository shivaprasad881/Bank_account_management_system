package repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.Transaction;
import org.springframework.data.domain.Pageable;

@Repository 
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Page<Transaction> findByAccnoOrderByTransIdDesc(String accno, Pageable pageable);
}