package model;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.sql.Timestamp;
import java.sql.Date;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    
    private String uname;
    private Integer age;
    private String city;
    private String phonenumber;
    private String password;
    private String accno;
    private String pin;
    private Double balance;

    @Column (name = "failure_attempts")
    private Integer failureAttempts;

    @Column (name = "available_at")
    private LocalTime availableAt;

    
    @Column (name = "invalid_jwt_tokens")
    private String invalidJwtTokens;


    @Column (name = "logout_count")
    private Integer logoutCount;

    private String email;

    
    @Column(name = "last_active_at")
    private Timestamp lastActiveAt;

    @Column(name = "join_date")
    private Date joinDate;


    @Column (name = "account_status")
    private String accountStatus;

    @Column(name = "is_logged_in")
    private Boolean isLoggedIn;

    @Column(name = "current_token", length = 500)
    private String currentToken;

    
    public User() {}
    
    public User(String uname, Integer age, String city, String phonenumber,String password,String email,Timestamp lastactiveat,Date joindate) {
        this.uname = uname;
        this.age = age;
        this.city = city;
        this.phonenumber = phonenumber;
        this.password = password;
        this.balance = 0.00;
        this.failureAttempts = 0;
        this.invalidJwtTokens = "[]";
        this.logoutCount = 0;
        this.email = email;
        this.lastActiveAt = lastactiveat;
        this.joinDate = joindate;
        this.isLoggedIn = false;
        this.currentToken = null;
    }
    
    // Getters
    public Long getUserid() { return userid; }
    public String getUname() { return uname; }
    public Integer getAge() { return age; }
    public String getCity() { return city; }
    public String getPhonenumber() { return phonenumber; }
    public String getPassword() { return password; }
    public String getAccno() { return accno; }
    public String getPin() { return pin; }
    public Double getBalance() { return balance; }
    public Integer getFailureAttempts() { return failureAttempts; }
    public LocalTime getAvailableAt() { return availableAt; }
    public String getInvalidJwtTokens() { return invalidJwtTokens; }
    public Integer getLogoutCount() { return logoutCount; }
    public String getEmail() { return email; }
    public Timestamp getLastActiveAt() {return lastActiveAt;}
    public Date getJoinDate() {return joinDate;}
    public String getAccountStatus() { return accountStatus; }
    public Boolean getIsLoggedIn() { return isLoggedIn; }
    public String getCurrentToken() { return currentToken; }
    
    // Setters
    public void setUserid(Long userid) { this.userid = userid; }
    public void setUname(String uname) { this.uname = uname; }
    public void setAge(Integer age) { this.age = age; }
    public void setCity(String city) { this.city = city; }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }
    public void setPassword(String password) { this.password = password; }
    public void setAccno(String accno) { this.accno = accno; }
    public void setPin(String pin) { this.pin = pin; }
    public void setBalance(Double balance) { this.balance = balance; }
    public void setFailureAttempts(Integer failureAttempts) { this.failureAttempts = failureAttempts; }
    public void setAvailableAt(LocalTime availableAt) { this.availableAt = availableAt ; }
    public void setInvalidJwtTokens(String invalidJwtTokens) { this.invalidJwtTokens = invalidJwtTokens; }
    public void setLogoutCount(Integer logoutCount) { this.logoutCount = logoutCount; }
    public void setEmail(String email) { this.email = email; }
    public void setLastActiveAt(Timestamp lastActiveAt) {this.lastActiveAt = lastActiveAt;}
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public void setIsLoggedIn(Boolean isLoggedIn) { this.isLoggedIn = isLoggedIn; }
    public void setCurrentToken(String currentToken) { this.currentToken = currentToken; }
}