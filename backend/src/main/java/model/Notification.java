package model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sender;
    private String receiver;
    private String message;
    private boolean viewed;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public Notification() {}

    public Notification(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.viewed = false;
    }

    // Getters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getMessage() { return message; }
    public boolean getViewed() { return viewed; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setSender(String sender) { this.sender = sender; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public void setMessage(String message) { this.message = message; }
    public void setViewed(boolean viewed) { this.viewed = viewed; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}