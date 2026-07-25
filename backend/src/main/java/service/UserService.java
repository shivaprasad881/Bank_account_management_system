package service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.example.demo.EmailUtil;
import com.example.demo.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import model.User;
import repository.TransactionRepository;
import repository.UserRepository;
import util.PasswordUtil;

import java.util.Random;
import model.Transaction;
import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.sql.Timestamp;
import java.sql.Date;


import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EmailUtil emailUtil;

	@Autowired
	private SchedulerService schedulerService;


    
    public ResponseEntity<?> register(String uname, Integer age, String city, String phonenumber, String password, String email) {

        if(uname == null || uname.isEmpty() || city == null || city.isEmpty() || phonenumber == null || phonenumber.isEmpty()  || phonenumber.length()!=10 || password == null || password.isEmpty() || age <= 0   || email == null || email.isEmpty()  ){
            
            return ResponseEntity.status(400).body("Please enter valid details !!");

        }
        else{
            // first check whether user already exists with that phone number

                try {
                    //now instead of storing hte plain password in the database - whichi is exposed ot the admin - we would store its hash

                    //now hash the password

                    String hashed_password = util.PasswordUtil.hashPassword(password);

                    //now store this hashed password in teh db so that even admin cant see the original password

                    User newUser = new User(uname, age, city, phonenumber, hashed_password,email,new Timestamp(System.currentTimeMillis()),new Date(System.currentTimeMillis()));
                    User savedUser = userRepository.save(newUser);

                    String accno = "ACC" + String.format("%08d", savedUser.getUserid());
                    //String pin = String.format("%04d", savedUser.getUserid() % 10000);

                    String pin = 1000 + new Random().nextInt(9000)+"";

                    //store the hashed pin - so that even developer cant see it as plain text
                    //even when the db/backup/logs are leaked - the attacker dont know the actual values of the hash - nouser - safe

                    String hashed_pin = util.PasswordUtil.hashPassword(pin);

					Timestamp cur_time = new Timestamp(System.currentTimeMillis());
					//set the last active at time
					savedUser.setLastActiveAt(cur_time);
					
                    
                    savedUser.setAccno(accno);
                    savedUser.setPin(hashed_pin);
                    userRepository.save(savedUser);

                    return ResponseEntity.ok(accno+","+pin);
                }
                catch(Exception e) {
                    return ResponseEntity.status(409).body("Phone number already registered");
                }

            
        }

        
    }


    public String failure_attempts(String identity, String identity_type) {

        User user;

        if(identity_type.equals("phonenumber")){
            user = userRepository.findByPhonenumber(identity);
        }
        else{
            //accno
            user = userRepository.findByAccno(identity);
        }
        


        if(user==null){
            return "user acc not existing !!";
        }
        else{
            if( user.getFailureAttempts() >= 3 ){
                //attempts exhausted - check time when they would be available

                Duration diff = Duration.between( LocalTime.now(),user.getAvailableAt());

                long sec = diff.toSeconds();

                // if(sec<0){
                //     //hoo the time got ended - now iam free - i got bail
                //     user.setFailureAttempts(0);
                //     user.setAvailableAt( null);
                //     userRepository.save(user);
                //     return "Hoo, now u can try attempting !!";
                // }
                // else{
                    return "Please try after "+sec+" seconds !!";
                //}

            }
            else{
                return "";
            }
        }

        
    }


    public String validateuser(String identity, String identity_type, String password) {

        if(identity.length()==0 || identity_type.length()==0 ||  password.length()==0){
            return "Please enter valid details !!";
        }
        else{

            // first get the user based on the accno or phone number

            // then get the users hash password to compare with cur password

            User user;

            if(identity_type.equals("phonenumber")){
                //phone
                user = userRepository.findByPhonenumber(identity);
            }
            else if(identity_type.equals("account")){
                
                //accno
                user = userRepository.findByAccno(identity);
            }
            else{
                return "false";
            }

            

            

            if(user == null){
                return "false";
            }
            else{
                //now the user is existing - check whether the user hashed password matches with the cur password

                String hashed_password = user.getPassword();

                if(util.PasswordUtil.verifyPassword(password,hashed_password)){
                    //hoo both matched - valid user

					Timestamp cur_time = new Timestamp(System.currentTimeMillis());
					//set the last active at time
					user.setLastActiveAt(cur_time);
					userRepository.save(user);

                    return JwtUtil.generateToken(user.getAccno());
                }
                else{
                    //hoo incorrect password - increment the invalid count
                    return "false";
                }

                

            }

        }
    }



    public ResponseEntity<?> checkBalance(String token) {
	    //validate token - send bal

	    try{
	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        //hoo the token is not in the blacklist - its completely valid token - return the user blance
	        if(is_token_in_blacklist){//reject
	            return ResponseEntity.status(401).body("Unauthorized request !!");
	        }
	        else{
	            String formatted = String.format("%.2f", user.getBalance());
	            
	            return ResponseEntity.ok(formatted);
	        }

	    }
	    catch(Exception e){

	        return ResponseEntity.status(401).body("Invalid token !!");

	    }

	}


	public ResponseEntity<?>  userdetails(String token) {


	    try{

	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        if(is_token_in_blacklist){// reject
	            return ResponseEntity.status(401).body("Unauthorized request !!");
	        }
	        else{//proceed
	            String data =  user.getAccno() +  ","   +  user.getUname() + "," + user.getAge() + "," + user.getCity()  + "," +  user.getPhonenumber() ;

	            return ResponseEntity.ok(data);
	        }

	    }
	    catch(Exception e){

	        return ResponseEntity.status(401).body("Invalid token !!");

	    }

	}


	public ResponseEntity<?>  username(String token) {

	    try{

	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        if(is_token_in_blacklist){// reject
	            return ResponseEntity.status(401).body("Unauthorized request !!");
	        }
	        else{//proceed
	            return ResponseEntity.ok(user.getUname() );
	        }

	    }
	    catch(Exception e){ //exception is the parent of all the exceptions 
	        return ResponseEntity.status(401).body("Invalid token !!");
	    }
	}

	public ResponseEntity<?>  useraccno(String token) {

	    try{

	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        if(is_token_in_blacklist){// reject
	            return ResponseEntity.status(401).body("Unauthorized request !!");
	        }
	        else{//proceed
	            return ResponseEntity.ok(user.getAccno() );
	        }

	    }
	    catch(Exception e){ //exception is the parent of all the exceptions 
	        return ResponseEntity.status(401).body("Invalid token !!");
	    }
	}

	public ResponseEntity<?>  useremail(String token) {

	    try{

	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        if(is_token_in_blacklist){// reject
	            return ResponseEntity.status(401).body("Unauthorized request !!");
	        }
	        else{//proceed
	            return ResponseEntity.ok(user.getEmail() );
	        }

	    }
	    catch(Exception e){ //exception is the parent of all the exceptions 
	        return ResponseEntity.status(401).body("Invalid token !!");
	    }
	}


	public ResponseEntity<?> user_transactions(String token, Integer sizee, Integer pagee) {

	    try {

	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        if(is_token_in_blacklist){// reject
	            return ResponseEntity.status(401).body("Unauthorized request !!");
	        }
	        else{//proceed
	            Pageable pageable = PageRequest.of(pagee, sizee);
	            Page<Transaction> transactions = transactionRepository.findByAccnoOrderByTransIdDesc(accno_jwt, pageable);

	            

	            return ResponseEntity.ok(transactions);
	        }

	    }


	    catch(Exception e) {
	        System.out.println("Token error: " + e.getMessage()); // ← add here
	        return ResponseEntity.status(401).body("Invalid token !!");
	    }


	}


	public ResponseEntity<?> validatepin(String token, String userpin) {

	    // first validate the token - if valid - send the response - it case of invalid it would send the exp so keep it in try block

	    try{
	        if(userpin.length()!=4 || Integer.parseInt(userpin)<0){
	            return ResponseEntity.ok("Please enter valid pin !!");
	        }
	        else{

	            String accno_jwt = JwtUtil.validateToken(token);
	            User user = userRepository.findByAccno(accno_jwt);

	            String black_list_string = user.getInvalidJwtTokens();

	            boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	            if(is_token_in_blacklist){// reject
	                return ResponseEntity.status(401).body("Unauthorized request !!");
	            }
	            else{//proceed
	                
	                //check whether user had enought attempts

	                if(user.getFailureAttempts()<3){
	                    // user had avilable attemps - let his try the pin

	                    String hashed_pin_db = user.getPin();

	                

	                    if(util.PasswordUtil.verifyPassword(userpin,hashed_pin_db)){

	                        //correct credentials - refresh the attemps
	                        user.setFailureAttempts(0);
	                        user.setAvailableAt(null);

	                        userRepository.save(user);
	                    
	                        return ResponseEntity.ok("true");
	                    }
	                    else{
	                        //invlaid credeintila - incremt the count

	                        user.setFailureAttempts( user.getFailureAttempts()+1 );

	                        

	                        if(user.getFailureAttempts()==3){

								LocalTime time = LocalTime.now().plusSeconds(45);
	                           

	                            user.setAvailableAt( time );
								schedulerService.scheduleTaskAt(time,user.getAccno());
	                        }

	                        userRepository.save(user);
	                        return ResponseEntity.ok("Invalid Credentials !!");
	                    }

	                }
	                else{

	                    Duration diff = Duration.between( LocalTime.now(),user.getAvailableAt());

	                    long sec = diff.toSeconds();

	                    
	                    return  ResponseEntity.ok("Please try after "+sec+" seconds !!");

	                }


	                
	                
	            }

	        }
	    }
	    catch(Exception e){
	       
	        return ResponseEntity.status(401).body("Invalid token !!");
	    }

	}


	public ResponseEntity<?> validate_user_token(String token) {

	    // first validate the token - if valid - send the response - it case of invalid it would send the exp so keep it in try block

	    try{

	        String accno_jwt = JwtUtil.validateToken(token);
	        User user = userRepository.findByAccno(accno_jwt);

	        String black_list_string = user.getInvalidJwtTokens();

	        boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	        if(is_token_in_blacklist){// reject
	            return ResponseEntity.ok("reject");
	        }
	        else{//proceed
	            return ResponseEntity.ok("accept");
	        }

	        
	    }
	    catch(Exception e){
	        // invalid token - this is token normally invalid due to expiry/format/ext
	        return ResponseEntity.status(401).body("Invalid token !!");
	    }

	}


	public ResponseEntity<?>  check_transfered_amount(String token) {


        


	    try{

	        String accno = JwtUtil.validateToken(token);

	        

	        // now fetch the transactions records based on the useracc , transactiontype , date

	        // transaction type - > debit (only consider the money which is transfered from the user acc but nt somene transfered in)

	        // date -> only consider the records of last 24 hours , transaction_date should be after (>=)  (date.now()-24 hours ) - this means that the time after the yesterdays current time - which inderectly measn the las t24hours

	        Timestamp yesterday  =  new Timestamp(System.currentTimeMillis() - 24L * 60 * 60 * 1000 );

	        //System.out.println("yesterdays current time is : "+yesterday);
	        Double transfered_amt = transactionRepository.getTotalAmountAfterTime(accno,"debit",yesterday,"self");
	           

	        // System.out.println("Current Java time: " + new Timestamp(System.currentTimeMillis()));
	        // System.out.println("Yesterday time: " + yesterday);
	        // System.out.println("Default timezone: " + java.util.TimeZone.getDefault());

	             return ResponseEntity.ok(transfered_amt);
	        

	    }
	    catch(Exception e){

	    return ResponseEntity.status(401).body("Invalid token !!");

	    }

	}


	


	public void failureauthentication(String identity, String identity_type) {

	        User user;

	        if(identity_type.equals("phonenumber")){
	            user = userRepository.findByPhonenumber(identity);
	        }
	        else{
	            //accno
	            user = userRepository.findByAccno(identity);
	        }
	        
	        

	        user.setFailureAttempts( user.getFailureAttempts() + 1    );

	        if(user.getFailureAttempts() >= 3){
	            // limit reached - now assign a time - so after that time the attempts would be avaiable


					LocalTime time = LocalTime.now().plusSeconds(45);

					
					user.setAvailableAt(time);
					
					schedulerService.scheduleTaskAt(time,user.getAccno());//at that time our execute shsecudular would refresh the attempst - so that user would try attemptling


	        }

	        userRepository.save(user);

	        
	}


	public void reset_failure_attempts(String identity, String identity_type) {
	        
	        User user;

	        if(identity_type.equals("phonenumber")){
	            user = userRepository.findByPhonenumber(identity);
	        }
	        else{
	            //accno
	            user = userRepository.findByAccno(identity);
	        }

	        user.setFailureAttempts( 0 );
	        user.setAvailableAt( null);
	        userRepository.save(user);

	        
	}


	public ResponseEntity<?> transfer(String token, String target, String target_type, Double amt) {

	        try{

	            if(target.length()==0   ||   target_type.length()==0    ||    amt<=0 ){
	                return ResponseEntity.status(400).body("Please enter valid details !!");
	            }
	            else{


	                String accno_jwt = JwtUtil.validateToken(token);
	                User acc_user = userRepository.findByAccno(accno_jwt);

	                String black_list_string = acc_user.getInvalidJwtTokens();

	                boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	                if(is_token_in_blacklist){// reject
	                   return ResponseEntity.status(401).body("Unauthorized request !!");
	                }
	                else{//proceed

	                    if( acc_user.getBalance() < amt ){
	                        return ResponseEntity.status(402).body("Insufficient balance !!");
	                    }
	                    else{
	                        //now user is valid and he had enough bal to transfer - now check whether he exceeded daily transaction limit or not

	                        Timestamp yesterday = new Timestamp(System.currentTimeMillis() - 24L * 60 * 60 * 1000);

	                        
	                        Double transfered_amt = transactionRepository.getTotalAmountAfterTime(accno_jwt,"debit",yesterday,"self");


	                        if( (transfered_amt + amt)>100000  ){
	                            //the limit is exceeding the 1,00,000 -unable to transfer
	                           return ResponseEntity.status(403).body("Amount exceeds daily transaction limit !!");
	                        }
	                        else{
	                            //the amt is within the limit - the user can safely transfer the amount

	                            // find the target user based on the target type
	                            User tar_user;

	                            if(target_type.equals("account")){

	                                //transfering based on account number
	                                tar_user = userRepository.findByAccno(target);

	                            }
	                            else{

	                                //transfering based on phonenumber
	                                tar_user = userRepository.findByPhonenumber(target);
	                            }
	                            

	                            // now check whether tar acc exists to transfer money

	                            if(tar_user!=null){

	                                //exists

	                                //now check both users are same

	                                if(acc_user.getAccno().equals( tar_user.getAccno()   ) ){
	                                    //both are same users
	                                    return ResponseEntity.status(400).body("Self transfer not allowed !!");
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

	                                    emailUtil.sendEmail(acc_user.getEmail(),"Transfer","Rupees "+amt+" debited from XXXXXXX"+accno_jwt.substring(7,11)+" at "+LocalDateTime.now().toString().substring(0,16).replace("T"," "));


	                                    return ResponseEntity.ok("Transaction successfull !!");


	                                }


	                            }
	                            else{
	                                //not exists
	                                return ResponseEntity.status(404).body("Destination account not existing !!");
	                            }


	                        }

	                        

	                        

	                        

	                    }


	                    
	                }//else


	            }
	            

	        }
	        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid token !!");
        }


	}


	public  ResponseEntity<?> updatePin(String token, String newpin) {

	        try{


	            if(newpin.length()!=4 || Integer.parseInt(newpin)<0){
	                return ResponseEntity.ok("Invalid new-pin !!");
	            }
	            else{


	                String accno_jwt = JwtUtil.validateToken(token);
	                User user = userRepository.findByAccno(accno_jwt);

	                String black_list_string = user.getInvalidJwtTokens();

	                boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	                if(is_token_in_blacklist){// reject
	                    return ResponseEntity.status(401).body("Unauthorized request !!");
	                }
	                else{//proceed

	                    String hashed_newpin = util.PasswordUtil.hashPassword(newpin);

	                    user.setPin(hashed_newpin);
	                    userRepository.save(user);

	                    //now also send mail to the user - as it done rare - we need to indicate the user
	                    emailUtil.sendEmail(user.getEmail(),"PIN UPDATION","Pin updated successfully !!");

	                    return ResponseEntity.ok("Pin updated succesfully !!");
	                }

	            }

	        }
	        catch(Exception e){
	            return ResponseEntity.status(401).body("Invalid token !!");
	        }

	        
	}


	public  ResponseEntity<?> update_password(String token, String oldpass, String newpass) {

	        try{


	            if(oldpass.length()==0 || newpass.length()==0 ){
	                return ResponseEntity.ok("please enter valid details");
	            }
	            else{


	                String accno_jwt = JwtUtil.validateToken(token);
	                User user = userRepository.findByAccno(accno_jwt);

	                String black_list_string = user.getInvalidJwtTokens();

	                boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	                if(is_token_in_blacklist){// reject 
	                    return ResponseEntity.status(401).body("Unauthorized request !!");
	                }
	                else{//proceed

	                    //now token is valid - valid user 

	                    //validate the old pass
	                    String old_hashed_pass = user.getPassword();

	                    if(util.PasswordUtil.verifyPassword(oldpass,old_hashed_pass)){
	                        //current password - update pass

	                        String new_hashed_pass = util.PasswordUtil.hashPassword(newpass);

	                        //now update the database with this new hasdhed pass

	                        user.setPassword(new_hashed_pass);

	                        userRepository.save(user);

	                        return ResponseEntity.ok("true");

	                    }
	                    else{
	                        return ResponseEntity.status(401).body("Unauthorized request !!");
	                        
	                    }

	                    
	                    
	                }

	            }

	        }
	        catch(Exception e){
	            return ResponseEntity.status(401).body("Invalid token !!");
	        }

	}


	public  ResponseEntity<?> new_black_list_tokenn(String token) {
	        
	        try{


	            String accno_jwt = JwtUtil.validateToken(token);
	            User user = userRepository.findByAccno(accno_jwt);

	            String black_list_string = user.getInvalidJwtTokens();

	            boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	            if(is_token_in_blacklist){// reject
	                return ResponseEntity.status(401).body("Unauthorized request !!");
	            }
	            else{//proceed

	                ObjectMapper mapper = new ObjectMapper();
	                String old_black_list_string = user.getInvalidJwtTokens();

	                List<String> new_tokenList = JwtUtil.tokenCleanUp(old_black_list_string);
	                new_tokenList.add(token);


	                String updated_black_list_string = mapper.writeValueAsString(new_tokenList);

	                user.setLogoutCount( new_tokenList.size() );
	                user.setInvalidJwtTokens(updated_black_list_string);
	                userRepository.save(user);

	                // finally we got the current list - added the cur token - update the list - changes done on database - token got added to the list

	                return ResponseEntity.ok("Token added to black_list succesfully !!");

	                
	            }


	        }
	        catch(Exception e){
	            // its already invalid token - no need to add it to the black_list
	            // incase of invalid token - now need to check and remove the expired tokens 
	            // System.out.println("Logout error: " + e.getMessage());
	            return ResponseEntity.status(401).body("Invalid token !!");
	        }

	        
	}


	public ResponseEntity<?> deposit(String token, Double amt) {

	        try{
	            

	            if(amt<=0){
	                return ResponseEntity.ok("Invalid amount !!");
	            }
	            else{

	                String accno_jwt = JwtUtil.validateToken(token);
	                User user = userRepository.findByAccno(accno_jwt);

	                String black_list_string = user.getInvalidJwtTokens();

	                boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);



	                if(is_token_in_blacklist){// reject
	                    return ResponseEntity.status(401).body("Unauthorized request !!");
	                }
	                else{//proceed
	                    
	                    user.setBalance( user.getBalance() + amt );

	                    userRepository.save(user);

	                    //hoo the deposit is successfull completed - now we need to create the transactoin record
	                    // useracc , self , amt , "credit" , available bal

	                    Transaction trans = new Transaction(accno_jwt,"self",amt,"credit",user.getBalance());

	                    transactionRepository.save(trans);

	                    return ResponseEntity.ok("Amount deposited succesfully!!");

	                }

	            }
	            
	  
	        }
	        catch(Exception e){
	            return ResponseEntity.status(401).body("Invalid token !!");
	        }

	        
	        
	}


	public ResponseEntity<?> withdrawl(String token, Double amt) {
	   
	        try{
	           

	            
	            if(amt<=0){
	                return ResponseEntity.ok("Invalid amount !!");
	            }
	            else{


	                String accno_jwt = JwtUtil.validateToken(token);
	                User user = userRepository.findByAccno(accno_jwt);

	                String black_list_string = user.getInvalidJwtTokens();

	                boolean is_token_in_blacklist = JwtUtil.isTokenInBlacklist(black_list_string,token);

	                if(is_token_in_blacklist){// reject
	                   return ResponseEntity.status(401).body("Unauthorized request !!");
	                }
	                else{//proceed
	                    
	                    Double bal = user.getBalance();

	                    if(bal<amt){

	                        return ResponseEntity.ok("Insufficient balance !!");
	                    
	                    }
	                    else{
	                        
	                        user.setBalance( user.getBalance() - amt );

	                        userRepository.save(user);

	                        //now withdrawl is successfull - now we need to create the record
	                        // useracc ->  useracc , amt , debit(-withdrawl) , avaiable bal

	                        Transaction trans = new Transaction(accno_jwt,"self",amt,"debit",user.getBalance());

	                        transactionRepository.save(trans);

	                        return ResponseEntity.ok("Amount withdrawl successfull !!");
	                    }

	                }


	            }

	        }
	        catch(Exception e){
	            return ResponseEntity.status(401).body("Invalid token !!");
	        }

	        
	        
	}//withdrawl



    public  ResponseEntity<?> resetpassword(String useremail,String newpassword) {

           

        User user = userRepository.findByEmail(useremail);

        if(user==null){
            return ResponseEntity.ok("false");
        }
        else{
                //hoo the email is valid - update the password

                String new_hashed_password = util.PasswordUtil.hashPassword(newpassword);

                //update

                user.setPassword(new_hashed_password);

                userRepository.save(user);

                return ResponseEntity.ok("true");

        }

    }


	public  ResponseEntity<?> resetpin(String useremail,String newpin) {

           

        User user = userRepository.findByEmail(useremail);

        if(user==null){
            return ResponseEntity.ok("false");
        }
        else{
                //hoo the email is valid - update the pin

                String new_hashed_pin = util.PasswordUtil.hashPassword(newpin);

                //update
                user.setPin(new_hashed_pin);

                userRepository.save(user);

                return ResponseEntity.ok("true");

        }

    }

}
