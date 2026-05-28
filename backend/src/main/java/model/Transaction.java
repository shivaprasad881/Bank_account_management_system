package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.sql.Timestamp;

@Entity
@Table(name = "transactions")

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column ( name = "trans_id")
    private int transId;

    private String accno;
    private String tar_acc;
    private Double amount;
    private String transaction_type;

    @Column(name = "transaction_date", insertable = false, updatable = false)
    private Timestamp transaction_date;

    // default constructor — required by JPA
    public Transaction() {}

    // constructor to create a transaction
    public Transaction(String accno, String tar_acc, Double amount, String transaction_type) {
        this.accno = accno;
        this.tar_acc = tar_acc;
        this.amount = amount;
        this.transaction_type = transaction_type;
    }

    // getters only — no setters since transactions are immutable
    public int getTransId() { return transId; }
    public String getAccno() { return accno; }
    public String getTarAcc() { return tar_acc; }
    public Double getAmount() { return amount; }
    public String getTransactionType() { return transaction_type; }
    public Timestamp getTransactionDate() { return transaction_date; }
}