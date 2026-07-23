package repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import model.Transaction;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.sql.Timestamp;


@Repository  
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Page<Transaction> findByAccnoOrderByTransIdDesc(String accno, Pageable pageable);

    

   @Query("""
       SELECT COALESCE(SUM(t.amount), 0)
       FROM Transaction t
       WHERE t.accno = :accno
         AND t.transactionType = :transactionType
         AND t.transactionDate > :fromTime
         AND t.tarAcc <> :taracc
       """)
    Double getTotalAmountAfterTime(
            @Param("accno") String accno,
            @Param("transactionType") String transactionType,
            @Param("fromTime") Timestamp fromTime,
            @Param("taracc") String taracc
    );


    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :time")
    List<Transaction> findTransactionsAfterTime(@Param("time") Timestamp time);

    @Query(value = "SELECT trans_id, accno, tar_acc, amount, transaction_type, available_balance, transaction_date FROM transactions", nativeQuery = true)
List<Object[]> fetchAllTransactionsData();
}

