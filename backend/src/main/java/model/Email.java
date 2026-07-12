package model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "emails")
public class Email {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String otp;

     @Column (name = "expire_at")
    private LocalTime expireAt;




    public Email() {}
    public Email( String email, String otp, LocalTime expireAt) {
        this.email = email;
        this.otp = otp;
        this.expireAt = expireAt;
    }


    // Getters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getOtp() { return otp; }
    public LocalTime getExpireAt() { return expireAt; }




    // Setters
    public void setOtp(String otp) { this.otp = otp; }
    public void setExpireAt(LocalTime expireAt) { this.expireAt = expireAt; }

   
}