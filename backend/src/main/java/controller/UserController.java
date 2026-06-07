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

import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


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




    @GetMapping("/failure_authentication")
        public String failure_attempts(@RequestParam String accno) {
            
             User user = userRepository.findByAccno(accno);

            if(user==null){
                return "user acc not existing !!";
            }
            else{
                if( user.getFailureAttempts() >= 3 ){
                    //attempts exhausted - check time when they would be available

                    Duration diff = Duration.between( LocalTime.now(),user.getAvailableAt());

                    long sec = diff.toSeconds();

                    if(sec<0){
                        //hoo the time got ended - now iam free - i got bail
                        user.setFailureAttempts(0);
                        user.setAvailableAt( null);
                        userRepository.save(user);
                        return "Hoo, now u can try attempting !!";
                    }
                    else{
                        return "Please try after "+sec+" seconds !!";
                    }
  
                }
                else{
                    return "";
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
        catch(Exception e){

            return ResponseEntity.status(401).body("Invalid token !!");

        }

    }

    @GetMapping("/user_details")
    public ResponseEntity<?>  userdetails(@RequestParam String token) {


        try{
            String accno_jwt = JwtUtil.validateToken(token);

            User user = userRepository.findByAccno(accno_jwt);

            String data =  user.getAccno() +  ","   +  user.getUname() + "," + user.getAge() + "," + user.getCity()  + "," +  user.getPhonenumber() ;

            return ResponseEntity.ok(data);


        }
        catch(Exception e){

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
                   
                    return ResponseEntity.ok("true");
                }
                else{
                    return ResponseEntity.ok("false");
                }
            }
        }
        catch(Exception e){
            // invalid token
            return ResponseEntity.status(401).body("Invalid token !!");
        }
 
    }

    @GetMapping("/validate_user_token")
    public ResponseEntity<?> validate_user_token(@RequestParam String token) {

        // first validate the token - if valid - send the response - it case of invalid it would send the exp so keep it in try block

        try{

                String accno_jwt  = JwtUtil.validateToken(token);

                //the token is live - now check whether it is in user's block list or not

                User user = userRepository.findByAccno(accno_jwt);

                String black_list_string = user.getInvalidJwtTokens();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode black_list = mapper.readTree(black_list_string);

                for (JsonNode node : black_list) {
                    String cur_token = node.asText();

                    // now compare with our token

                    if(token.equals(cur_token)){
                        //hoo they are equal - which means user trying login with the expired token - we need to reject the request
                        return ResponseEntity.ok("reject");
                    }
                    

                }

                // we compared our token with all the black list tokens - it is not matched with anyone - so it is compareltely valid - send proceed response
                

                return ResponseEntity.ok("accept");
    
            
        }
        catch(Exception e){
            // invalid token - this is token normally invalid due to expiry/format/ext
            return ResponseEntity.status(401).body("Invalid token !!");
        }
 
    }





    @PatchMapping("/failure_authentication")
        public void failureauthentication(@RequestBody Map<String, Object> jsonBody) {
            String accno = (String) jsonBody.get("accno");
            
             User user = userRepository.findByAccno(accno);

            user.setFailureAttempts( user.getFailureAttempts() + 1    );

            if(user.getFailureAttempts() >= 3){
                // limit reached - now assign a time - so after that time the attempts would be avaiable

                user.setAvailableAt( LocalTime.now().plusSeconds(20) );
            }

            userRepository.save(user);

            
        }

    @PatchMapping("/reset_failure_attempts")
        public void reset_failure_attempts(@RequestBody Map<String, Object> jsonBody) {
            String accno = (String) jsonBody.get("accno");
            
             User user = userRepository.findByAccno(accno);

            user.setFailureAttempts( 0 );
            user.setAvailableAt( null);
            userRepository.save(user);

            
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
                                Double accuser_available_bal = acc_user.getBalance();

                                userRepository.save(acc_user);

                                tar_user.setBalance(tar_user.getBalance() + amt );
                                Double taruser_available_bal = tar_user.getBalance();

                                userRepository.save(tar_user);

                                
                                //transaction is successfull - now create the records
                                // acc_user , tar_user , amt , credit/debit


                                // call the transaction constructor to create the new transaction record
                                Transaction trans1 = new Transaction(acc_user.getAccno(),tar_user.getAccno(),amt,"debit",accuser_available_bal);
                                Transaction trans2 = new Transaction(tar_user.getAccno(),acc_user.getAccno(),amt,"credit",taruser_available_bal);

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

    @PatchMapping("/new_black_list_token")
        public  ResponseEntity<?> new_black_list_tokenn(@RequestBody Map<String, Object> jsonBody) {
            String token = (String) jsonBody.get("token");
            
            try{
                String accno_jwt = JwtUtil.validateToken(token);

                //its a valid token - add it to user's black list

                User user = userRepository.findByAccno(accno_jwt);


                String old_black_list_string = user.getInvalidJwtTokens();


                ObjectMapper mapper = new ObjectMapper();
                List<String> tokenList = mapper.readValue(old_black_list_string, new TypeReference<List<String>>() {});

                tokenList.add(token);
                user.setLogoutCount( user.getLogoutCount() + 1  );


                List<String> new_tokenList = new ArrayList<>();

                // now we had the token list in the form of strings - check for expirty of each token string - remove in case of expiry

                for(String cur_token : tokenList){
                    if(!JwtUtil.isTokenExpired(cur_token) ){
                        //hoo its a valid token - if need to retain it - add to our new list
                        new_tokenList.add(cur_token);


                        //when we are removing a token from the list - decrement the count to stay balanced
                        
                    }
                    else{
                        //hoo its already expired - no use of keeping it - ignore it to add to new list 

                        //decrease the count - as we are ignoring this token
                        user.setLogoutCount( user.getLogoutCount() - 1  );

                    }
                    
                }

                //now we had all the valid tokens in the new list - add then to db



                String updated_black_list_string = mapper.writeValueAsString(new_tokenList);

                //update in the object by using setters (we had the update string - just replace with the existing string)

                
                user.setInvalidJwtTokens(updated_black_list_string);

                // hoo add the changes to the database 
                
                userRepository.save(user);

                // finally we got the current list - added the cur token - update the list - changes done on database - token got added to the list

                return ResponseEntity.ok("Token added to black_list succesfully !!");
                
   
            }
            catch(Exception e){
                // its already invalid token - no need to add it to the black_list
                // incase of invalid token - now need to check and remove the expired tokens 
                System.out.println("Logout error: " + e.getMessage());
                return ResponseEntity.status(401).body("Invalid token !!");
            }
  
            
        }

    @PatchMapping("/deposit")
        public ResponseEntity<?> deposit(@RequestBody Map<String, Object> jsonBody) {
            String token = (String) jsonBody.get("token");
            Double amt = Double.parseDouble((String) jsonBody.get("amount"));

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
           
            try{
               

                
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


    //end of endpoints

}