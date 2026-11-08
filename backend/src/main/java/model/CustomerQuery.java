package model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "customerqueries")
public class CustomerQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String accno;
    private String type;
    private String message;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    // No-args constructor
    public CustomerQuery() {}

    // Parameterized constructor
    public CustomerQuery(String accno, String type, String message) {
        this.accno = accno;
        this.type = type;
        this.message = message;
        
    }

    // Getters
    public int getId() { return id; }
    public String getAccno() { return accno; }
    public String getType() { return type; }
    public String getMessage() { return message; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setAccno(String accno) { this.accno = accno; }
    public void setType(String type) { this.type = type; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}