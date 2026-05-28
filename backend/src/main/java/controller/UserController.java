package controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import com.example.demo.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import model.User;
import repository.TransactionRepository;
import repository.UserRepository;
import java.util.Map;
import model.Transaction;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, Object> jsonBody) {

        String uname = (String) jsonBody.get("uname");
        Integer age = Integer.parseInt((String) jsonBody.get("age"));
        String city = (String) jsonBody.get("city");
        String phonenumber = (String) jsonBody.get("phonenumber");
        String password = (String) jsonBody.get("password");

        if(uname == null || uname.isEmpty() || city == null || city.isEmpty() || phonenumber == null || phonenumber.isEmpty()  || phonenumber.length()!=10 || password == null || password.isEmpty() || age <= 0){
            return "Please enter valid details !!";
        }
        else{
            User newUser = new User(uname, age, city, phonenumber,password);
            User savedUser = userRepository.save(newUser);

            String accno = "ACC" + String.format("%08d", savedUser.getUserid());
            String pin = String.format("%04d", savedUser.getUserid() % 10000);
            
            savedUser.setAccno(accno);
            savedUser.setPin(pin);
            userRepository.save(savedUser);

            return "Account Number: " + accno + " | PIN: " + pin;
        }

        
    } 

    @PostMapping("/check_accno")
    public String checkAccno(@RequestBody Map<String, Object> jsonBody) {
        String accno = (String) jsonBody.get("accno");

        if(accno.length()==0){
            return "Please enter valid account number !!";
        }
        else{
            
            User user = userRepository.findByAccno(accno);

            if(user==null){
                //the user is not present
                return "false";
            }
            else{
                return "true";
            }
        }

        
    }


    @GetMapping("/validate_user")
    public String validateuser(@RequestParam String accno, @RequestParam String password) {

        if(accno.length()==0 ||  password.length()==0){
            return "Please enter valid details !!";
        }
        else{

            User user = userRepository.findByAccnoAndPassword(accno,password);

            if(user == null){
                return "false";
            }
            else{
                return JwtUtil.generateToken(accno);

            }

        }

        
    }

    

    @GetMapping("/check_balance")
    public ResponseEntity<?> checkBalance(@RequestParam String token) {
        //validate token - send bal

        try{
            String accno_jwt = JwtUtil.validateToken(token);

            User user = userRepository.findByAccno(accno_jwt);


            return ResponseEntity.ok(user.getBalance());


        }
        catch(Exception e){// exceoptions is the parent of all the exceptions - it would havleany exp - it is the main root

            return ResponseEntity.status(401).body("Invalid token !!");

        }

    }

    @GetMapping("/user_details")
    public ResponseEntity<?>  userdetails(@RequestParam String token) {

        // first validate the token then send the response

        // in case of valid token - it would return the accno
        // in case of invalid token - it would return the exception - so we need to hangle the exp in the cathc block

        // as there is a chance for exp - keep the that error code in the try bloack - when ecp occurs then catch block will execute

        // to return dynamic datatypes we would use repository - in frontend based on the status code woudl would take the responsein according datatypes


        try{// make sure that take the accno from teh token but nto from the url , because if u take the accno from the url thne by cahnging the accno inthe url the data would be fetcehed , so if u take teh accno from the token as the attacker cant change the accno in the otken it would remain safe and give the according reseultr
            // as we had thatjava function somewhere - we need to atleast specify the file in which it is parent so that we would search fo rthat file an dcheck for tha tethod in tit
            String accno_jwt = JwtUtil.validateToken(token);

            User user = userRepository.findByAccno(accno_jwt);

            String data =  user.getAccno() +  ","   +  user.getUname() + "," + user.getAge() + "," + user.getCity()  + "," +  user.getPhonenumber() ;

            return ResponseEntity.ok(data);


        }
        catch(Exception e){// exceoptions is the parent of all the exceptions - it would havleany exp - it is the main root

            return ResponseEntity.status(401).body("Invalid token !!");

        }

        

        
    }

    
    @GetMapping("/user_name")
    public ResponseEntity<?>  username(@RequestParam String token) {

        try{
            String accno_jwt = JwtUtil.validateToken(token);

            User user = userRepository.findByAccno(accno_jwt);

            return ResponseEntity.ok(user.getUname() );
        }
        catch(Exception e){ //exception is the parent of all the exceptions 
            return ResponseEntity.status(401).body("Invalid token !!");
        }
    }

    @GetMapping("/user_transactions")
public ResponseEntity<?> user_transactions(
    @RequestParam String token,
    @RequestParam String size,
    @RequestParam String page
) {
        Integer sizee = Integer.parseInt(size);
        Integer pagee = Integer.parseInt(page);
    try {
        String accno_jwt = JwtUtil.validateToken(token);
        Pageable pageable = PageRequest.of(pagee, sizee);
        Page<Transaction> transactions = transactionRepository.findByAccnoOrderByTransIdDesc(accno_jwt, pageable);
        return ResponseEntity.ok(transactions);
    } catch(Exception e) {
        System.out.println("Token error: " + e.getMessage()); // ← add here
        return ResponseEntity.status(401).body("Invalid token !!");
    }
}

    @GetMapping("/validate_pin")
    public ResponseEntity<?> validatepin(@RequestParam String token,@RequestParam String userpin) {

        // first validate the token - if valid - send the response - it case of invalid it would send the exp so keep it in try block

        try{
            if(userpin.length()!=4 || Integer.parseInt(userpin)<0){
                return ResponseEntity.ok("Please enter valid pin !!");
            }
            else{

                String accno_jwt  = JwtUtil.validateToken(token);

                User user = userRepository.findByAccno(accno_jwt);

                String orig_pin = user.getPin();

                if( orig_pin.equals(userpin) ){
                    //the original pin and user entered old pin are same
                    return ResponseEntity.ok("true");
                }
                else{
                    return ResponseEntity.ok("false"); //invalid pin
                }


            }
            

        }
        catch(Exception e){
            // invalid token
            return ResponseEntity.status(401).body("Invalid token !!");
        }
        


        
         
    }


    @PatchMapping("/transfer")
        public ResponseEntity<?> transfer(@RequestBody Map<String, Object> jsonBody) {
            String token = (String) jsonBody.get("token");
            String tar_acc = (String) jsonBody.get("tar_acc");
            Double amt = Double.parseDouble(jsonBody.get("amount").toString());


            try{

                if(tar_acc.length()==0 || amt<=0 ){
                    return ResponseEntity.ok("Please enter valid details !!");
                }
                else{

                    String accno_jwt = JwtUtil.validateToken(token);

                

                    User acc_user = userRepository.findByAccno(accno_jwt);

                    //check whether use had enough balance or not to debit

                    if( acc_user.getBalance() < amt ){
                        return ResponseEntity.ok("Insufficient balance !!");
                    }
                    else{
                        

                        //enough balance

                        // now check whether tar acc exists to transfer money
                        User tar_user = userRepository.findByAccno(tar_acc);

                        if(tar_user!=null){

                            //exists

                            //now check both users are same

                            if(acc_user.getAccno().equals( tar_user.getAccno()   ) ){
                                //both are same users
                                 return ResponseEntity.ok("Self transfer not allowed !!");
                            }
                            else{

                                
                                acc_user.setBalance( acc_user.getBalance() - amt );

                                userRepository.save(acc_user);

                                tar_user.setBalance(tar_user.getBalance() + amt );

                                userRepository.save(tar_user);

                                
                                //transaction is successfull - now create the records
                                // acc_user , tar_user , amt , credit/debit


                                // call the transaction constructor to create the new transaction record
                                Transaction trans1 = new Transaction(acc_user.getAccno(),tar_user.getAccno(),amt,"debit");
                                Transaction trans2 = new Transaction(tar_user.getAccno(),acc_user.getAccno(),amt,"credit");

                                transactionRepository.save(trans1);
                                transactionRepository.save(trans2);


                                return ResponseEntity.ok("Transaction successfull !!");


                            }


                        }
                        else{
                            //not exists
                            return ResponseEntity.ok("Destination account not existing !!");
                        }

                        

                    }


                }
                

            }
            catch(Exception e){// exceoptions is the parent of all the exceptions - it would havleany exp - it is the main root

                return ResponseEntity.status(401).body("Invalid token !!");

            }


        }


    @PatchMapping("/updatepin")
        public  ResponseEntity<?> updatePin(@RequestBody Map<String, Object> jsonBody) {
            String token = (String) jsonBody.get("token");
            String newpin = (String) jsonBody.get("newpin");


            try{


                if(newpin.length()!=4 || Integer.parseInt(newpin)<0){
                    return ResponseEntity.ok("Invalid new-pin !!");
                }
                else{

                    String accno_jwt = JwtUtil.validateToken(token);

                    User user = userRepository.findByAccno(accno_jwt);

                    user.setPin(newpin);
                    userRepository.save(user);

                    return ResponseEntity.ok("Pin updated succesfully !!");
                }
   
            }
            catch(Exception e){
                return ResponseEntity.status(401).body("Invalid token !!");
            }
  
            
        }


    @PatchMapping("/deposit")
        public ResponseEntity<?> deposit(@RequestBody Map<String, Object> jsonBody) {
            String token = (String) jsonBody.get("token");
            Double amt = Double.parseDouble((String) jsonBody.get("amount"));

            // first validate the token - if valid - deposit the money - send response


            try{
                

                if(amt<=0){
                    return ResponseEntity.ok("Invalid amount !!");
                }
                else{
                    String accno_jwt = JwtUtil.validateToken(token);

                    User user = userRepository.findByAccno(accno_jwt);
                    user.setBalance( user.getBalance() + amt );

                    userRepository.save(user);

                    return ResponseEntity.ok("Amount deposited succesfully!!");
                }
                
                
                
            }
            catch(Exception e){
                return ResponseEntity.status(401).body("Invalid token !!");
            }

            
            
        }



    @PatchMapping("/withdrawl")
        public ResponseEntity<?> withdrawl(@RequestBody Map<String, Object> jsonBody) {
            String token = (String) jsonBody.get("token");
            Double amt = Double.parseDouble((String) jsonBody.get("amount"));
            

            // first validate the token - if valid - deposit the money - send response


            try{
               

                //now i had the actual balance and the user requested balance - check whether i had enough bal to withdraw
                if(amt<=0){
                    return ResponseEntity.ok("Invalid amount !!");
                }
                else{
                     String accno_jwt = JwtUtil.validateToken(token);

                    User user = userRepository.findByAccno(accno_jwt);

                    Double bal = user.getBalance();

                    if(bal<amt){

                        return ResponseEntity.ok("Insufficient balance !!");
                    
                    }
                    else{
                        
                        user.setBalance( user.getBalance() - amt );

                        userRepository.save(user);

                        return ResponseEntity.ok("Amount withdrawl successfull !!");
                    }


                }
                
  
                
            }
            catch(Exception e){
                return ResponseEntity.status(401).body("Invalid token !!");
            }

            
            
        }//withdrawl

}