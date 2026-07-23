package controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.EmailUtil;
import com.example.demo.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.UserService;
import service.QRCodeService;

import java.time.LocalTime;
import java.util.Map;

import model.Email;
import model.User;
import model.Employee;
import model.Transaction;

import repository.UserRepository;
import repository.EmailRepository;
import repository.EmployeeRepository;
import repository.TransactionRepository;


import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.http.MediaType;


import java.sql.Timestamp;

@RestController
public class UserController {

   @Autowired
    private UserRepository userRepository;

	@Autowired
    private EmailRepository emailRepository;

	@Autowired
    private EmployeeRepository employeeRepository;

	@Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

	@Autowired
    private EmailUtil emailUtil;

	@Autowired
    private QRCodeService qrCodeService;




	@PostMapping("/generate")
	public ResponseEntity<byte[]> generateQR(@RequestBody Map<String, String> requestBody) {
		try {
			String data = requestBody.get("data");
			if (data == null || data.isEmpty()) {
				return ResponseEntity.badRequest().build();
			}

			byte[] qrImage = qrCodeService.generateQRCodeAsBytes(data, 300);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);

			return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> jsonBody) {

        String uname = (String) jsonBody.get("uname");
        Integer age = Integer.parseInt((String) jsonBody.get("age"));
        String city = (String) jsonBody.get("city");
        String phonenumber = (String) jsonBody.get("phonenumber");
        String password = (String) jsonBody.get("password");
        String email = (String) jsonBody.get("email");

        return userService.register(uname, age, city, phonenumber, password, email);
    } 



    @GetMapping("/failure_authentication")
    public String failure_attempts(@RequestParam String identity, @RequestParam String identity_type) {

        return userService.failure_attempts(identity, identity_type);
    }

    
    
    @GetMapping("/validate_user")
    public String validateuser(@RequestParam String identity, @RequestParam String identity_type, @RequestParam String password) {

        return userService.validateuser(identity, identity_type, password);
    }

	@GetMapping("/validate_emp")
    public String validateemp(@RequestParam String empid, @RequestParam String password) {

        if(empid.length()==0 ||  password.length()==0){
            return "Please enter valid details !!";
        }
        else{

            // first get the user based on the accno or phone number

            // then get the users hash password to compare with cur password

            Employee emp = employeeRepository.findByEmpid(empid);

            
            

            

            if(emp == null){
                return "false";
            }
            else{
                //now the user is existing - check whether the user hashed password matches with the cur password

                String password_db = emp.getPassword();

                if(password.equals(password_db)){
                    //hoo both matched - valid user
                    return JwtUtil.generateToken(emp.getEmpid());
                }
                else{
                    //hoo incorrect password - increment the invalid count
                    return "false";
                }

                

            }

        }

    }

	@GetMapping("/total_bank_balance")
    public String total_bank_balance() {
		
        
		List<User> users = userRepository.findAll();

		int sum = 0;

		for(User user:users){
			sum += user.getBalance();
		}

		return sum+"";
            
    }

	@GetMapping("/bank_transactions")
    public String bank_transactions() {

        
		Timestamp yesterday  =  new Timestamp(System.currentTimeMillis() - 240L * 60 * 60 * 1000);

		List<Transaction> transactions = transactionRepository.findTransactionsAfterTime(yesterday);

		double total_amt = 0;

		double outgoing_amt = 0;

		double incoming_amt = 0;
		
		for(Transaction transaction:transactions){
			String type = transaction.getTransactionType();

			if(type.equals("debit")){
				outgoing_amt += transaction.getAmount();
			}
			else if(type.equals("credit")){
				incoming_amt += transaction.getAmount();
			}

		}
		 total_amt = outgoing_amt+ incoming_amt;

		 System.out.println("total_amt : "+ transactions.size());

		return outgoing_amt+ " "+incoming_amt+" "+ total_amt ;
            
    }

	@GetMapping("/users_count_based_on_account_status")
    public String users_count_based_on_account_status() {

        
		List<User> users = userRepository.findAll();

		int active = 0;

		int inactive = 0;

		for(User user:users){

			//60L -> 60 days
			String accountstatus = user.getAccountStatus();

			if(accountstatus.equals("blocked")){
				inactive++;
			}
			else{
				active++;
			}
		}

		return active+" "+inactive+" "+(active+inactive);
            
    }

	@GetMapping("/is_employee_manager")
	public String is_employee_manager(@RequestParam String emp_token) {
	    
		try{
	        String empid = JwtUtil.validateToken(emp_token);
	       	Employee emp = employeeRepository.findByEmpid(empid);

			String role = emp.getDept();

			System.out.println("role : "+role);

			if(role.equals("manager")){
				//true
				return "true";
			}
			else{
				//false

				return "false";
			}


	    }
	    catch(Exception e){

	        return "false";

	    }
	}

	@GetMapping("/users_data_based_on_emp_role")
	public ResponseEntity<?> users_data_based_on_emp_role(@RequestParam String emp_token) {
		
		try {
			String empid = JwtUtil.validateToken(emp_token);
			Employee emp = employeeRepository.findByEmpid(empid);
			String role = emp.getDept();
			System.out.println("role : " + role);

			List<Object[]> users;

			if(role.equals("manager")) {
				users = userRepository.fetchUsersByManager();
			}
			else if(role.equals("cashier")) {
				users = userRepository.fetchUsersByCashier();
			}
			else {
				users = userRepository.fetchUsersByClerk();
			}

			List<Map<String, Object>> response = new ArrayList<>();

			for(Object[] row : users) {
				Map<String, Object> user = new LinkedHashMap<>();

				if(role.equals("manager")) {
					user.put("userid", row[0]);
					user.put("uname", row[1]);
					user.put("age", row[2]);
					user.put("city", row[3]);
					user.put("accno", row[4]);
					user.put("phonenumber", row[5]);
					user.put("email", row[6]);
					user.put("balance", row[7]);
				}
				else if(role.equals("cashier")) {
					user.put("userid", row[0]);
					user.put("uname", row[1]);
					user.put("accno", row[2]);
					user.put("phonenumber", row[3]);
					user.put("balance", row[4]);
				}
				else {
					user.put("userid", row[0]);
					user.put("uname", row[1]);
					user.put("age", row[2]);
					user.put("city", row[3]);
					user.put("accno", row[4]);
					user.put("phonenumber", row[5]);
					user.put("email", row[6]);
					user.put("balance", row[7]);
					user.put("availableAt", row[8]);
					user.put("lastActiveAt", row[9]);
					user.put("accountStatus", row[10]);
				}

				response.add(user);
			}

			Map<String, Object> finalResponse = new LinkedHashMap<>();
			
			finalResponse.put("users", response);

			return ResponseEntity.ok(finalResponse);

		}
		catch(Exception e) {
    System.out.println("error: " + e.getMessage());
    return ResponseEntity.status(401).body("Invalid token !!");
}
	}

	@GetMapping("/employees_data")
	public ResponseEntity<?>  employees_data(@RequestParam String emp_token) {
	    
		try {
        String empid = JwtUtil.validateToken(emp_token);
        Employee emp = employeeRepository.findByEmpid(empid);

        // only allow manager
        if(!emp.getDept().equals("manager")) {
            return ResponseEntity.status(403).body("Access denied !!");
        }

        List<Object[]> results = employeeRepository.fetchAllEmployeesData();

        List<Map<String, Object>> response = new ArrayList<>();

        for(Object[] result : results) {
            Map<String, Object> empData = new LinkedHashMap<>();
            empData.put("empid", result[0]);
            empData.put("ename", result[1]);
            empData.put("age", result[2]);
            empData.put("salary", result[3]);
            empData.put("dept", result[4]);
            response.add(empData);
        }

        return ResponseEntity.ok(response);
    }
    catch(Exception e) {
        return ResponseEntity.status(401).body("Invalid token !!");
    }
	}

	@GetMapping("/transactions_data")
	public ResponseEntity<?>  transactions_data(@RequestParam String emp_token) {
	    
		try {
        String empid = JwtUtil.validateToken(emp_token);
        Employee emp = employeeRepository.findByEmpid(empid);

        // only allow manager
        if(!emp.getDept().equals("manager")) {
            return ResponseEntity.status(403).body("Access denied !!");
        }

        List<Object[]> results = transactionRepository.fetchAllTransactionsData();

		List<Map<String, Object>> response = new ArrayList<>();

		for(Object[] result : results) {
			Map<String, Object> transData = new LinkedHashMap<>();
			transData.put("trans_id", result[0]);
			transData.put("accno", result[1]);
			transData.put("tar_acc", result[2]);
			transData.put("amount", result[3]);
			transData.put("transaction_type", result[4]);
			transData.put("available_balance", result[5]);
			transData.put("transaction_date", result[6]);
			response.add(transData);
		}
        return ResponseEntity.ok(response);
    }
    catch(Exception e) {
        return ResponseEntity.status(401).body("Invalid token !!");
    }
	}


	@GetMapping("/new_users")
    public String new_users() {

        
		long noofnewusers = userRepository.countUsersJoinedToday();

	
		return noofnewusers+"";
            
    }


    @GetMapping("/check_balance")
	public ResponseEntity<?> checkBalance(@RequestParam String token) {
	    return userService.checkBalance(token);
	}


	@GetMapping("/user_details")
	public ResponseEntity<?>  userdetails(@RequestParam String token) {
	    return userService.userdetails(token);
	}


	@GetMapping("/user_name")
	public ResponseEntity<?>  username(@RequestParam String token) {
	    return userService.username(token);
	}

	@GetMapping("/user_email")
	public ResponseEntity<?> useremail(@RequestParam String token) {
	    return userService.useremail(token);
	}


	@GetMapping("/user_transactions")
	public ResponseEntity<?> user_transactions(@RequestParam String token,@RequestParam String size,@RequestParam String page) {
	    Integer sizee = Integer.parseInt(size);
	    Integer pagee = Integer.parseInt(page);

	    return userService.user_transactions(token, sizee, pagee);
	}


	@GetMapping("/validate_pin")
	public ResponseEntity<?> validatepin(@RequestParam String token,@RequestParam String userpin) {
	    return userService.validatepin(token, userpin);
	}


	@GetMapping("/validate_user_token")
	public ResponseEntity<?> validate_user_token(@RequestParam String token) {
	    return userService.validate_user_token(token);
	}


	@GetMapping("/check_transfered_amount")
	public ResponseEntity<?>  check_transfered_amount(@RequestParam String token) {
	    return userService.check_transfered_amount(token);
	}


	// @PatchMapping("/send_email")
	// public ResponseEntity<?> send_email(@RequestBody Map<String, Object> jsonBody) {
	//     String email = (String) jsonBody.get("email");
	//     String subject = (String) jsonBody.get("subject");
	//     String message = (String) jsonBody.get("message");
	//     // String verifyemail = (String) jsonBody.get("verifyemail");

	//     return userService.send_email(email, subject, message);
	// }


	@PatchMapping("/failure_authentication")
	public void failureauthentication(@RequestBody Map<String, Object> jsonBody) {
	    String identity = (String) jsonBody.get("identity");
	    String identity_type = (String) jsonBody.get("identity_type");

	    userService.failureauthentication(identity, identity_type);
	}


	@PatchMapping("/reset_failure_attempts")
	public void reset_failure_attempts(@RequestBody Map<String, Object> jsonBody) {
	    String identity = (String) jsonBody.get("identity");
	    String identity_type = (String) jsonBody.get("identity_type");

	    userService.reset_failure_attempts(identity, identity_type);
	}


	@PatchMapping("/transfer")
	public ResponseEntity<?> transfer(@RequestBody Map<String, Object> jsonBody) {
	    String token = (String) jsonBody.get("token");
	    String target = (String) jsonBody.get("target");
	    String target_type = (String) jsonBody.get("target_type");
	    Double amt = Double.parseDouble(jsonBody.get("amount").toString());

	    return userService.transfer(token, target, target_type, amt);
	}


	@PatchMapping("/updatepin")
	public  ResponseEntity<?> updatePin(@RequestBody Map<String, Object> jsonBody) {
	    String token = (String) jsonBody.get("token");
	    String newpin = (String) jsonBody.get("newpin");

	    return userService.updatePin(token, newpin);
	}


	@PatchMapping("/update_password")
	public  ResponseEntity<?> update_password(@RequestBody Map<String, Object> jsonBody) {
	    String token = (String) jsonBody.get("token");
	    String oldpass = (String) jsonBody.get("password");
	    String newpass = (String) jsonBody.get("newpass");

	    return userService.update_password(token, oldpass, newpass);
	}


	@PatchMapping("/new_black_list_token")
	public  ResponseEntity<?> new_black_list_tokenn(@RequestBody Map<String, Object> jsonBody) {
	    String token = (String) jsonBody.get("token");

	    return userService.new_black_list_tokenn(token);
	}


	@PatchMapping("/deposit")
	public ResponseEntity<?> deposit(@RequestBody Map<String, Object> jsonBody) {
	    String token = (String) jsonBody.get("token");
	    Double amt = Double.parseDouble((String) jsonBody.get("amount"));

	    return userService.deposit(token, amt);
	}


	@PatchMapping("/withdrawl")
	public ResponseEntity<?> withdrawl(@RequestBody Map<String, Object> jsonBody) {
	    String token = (String) jsonBody.get("token");
	    Double amt = Double.parseDouble((String) jsonBody.get("amount"));

	    return userService.withdrawl(token, amt);
	}

   
    @PatchMapping("/resetpassword")
        public  ResponseEntity<?> resetpassword(@RequestBody Map<String, Object> jsonBody) {
            String useremail = (String) jsonBody.get("email");
            String newpassword = (String) jsonBody.get("newpassword");

            return userService.resetpassword(useremail,newpassword);

        }


	@PatchMapping("/resetpin")
        public  ResponseEntity<?> resetpin(@RequestBody Map<String, Object> jsonBody) {
            String useremail = (String) jsonBody.get("email");
            String newpin = (String) jsonBody.get("newpin");

            return userService.resetpin(useremail,newpin);

        }


	@PatchMapping("/send_otp_no_verification")
        public  String send_otp_no_verification(@RequestBody Map<String, Object> jsonBody) {
            String useremail = (String) jsonBody.get("email");
            

            emailUtil.sendotpp(useremail);

			return "true";

        }

    //end of endpoints

	@PatchMapping("/verify_email_send_otp")
	public String verifyEmailAndSendOtp(@RequestBody Map<String, Object> jsonBody) {
		String useremail = (String) jsonBody.get("email");

		// Check if user exists with that email
		User user = userRepository.findByEmail(useremail);

		if (user == null) {
			return "false";
		}
		else{
			emailUtil.sendotpp(useremail);
			return"true";
		}

		// Send OTP
		
	}


	
	
	@PatchMapping("/verify_user_send_otp")
	public ResponseEntity<?> verify_user_send_otp(@RequestBody Map<String, Object> jsonBody) {
    	String token = (String) jsonBody.get("token");
    // ...


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
	            
				emailUtil.sendotpp(user.getEmail());
	            return ResponseEntity.ok(user.getEmail()+"");
	        }

	    }
	    catch(Exception e){

	        return ResponseEntity.status(401).body("Invalid token !!");

	    }

	}

	@PatchMapping("/verify_otp")
	public String verifyOtp(@RequestBody Map<String, Object> jsonBody) {
		String useremail = (String) jsonBody.get("email");
		String userOtp = (String) jsonBody.get("otp");

		Email emailRecord = emailRepository.findByEmail(useremail);

		if (emailRecord == null) {
			return "false";
		} 
		else {
			String storedOtp = emailRecord.getOtp();
			LocalTime expireAt = emailRecord.getExpireAt();

			if (LocalTime.now().isAfter(expireAt)) {
				return "false";
			} 
			else {
				if (storedOtp.equals(userOtp)) {
					//in case of successfull - we would remove the otp record - because as the verification is successful - he would be a legitimate user
					//as he is legitimate user - so no need to track and inspect his bhavious 
					//only keep the users know are not legitimate so that we can track there behavious based on noof failed requests etc

					//delete the record

					//get the user record then delete it
					

					emailRepository.delete(emailRecord);


					return "true";
				} 
				else {
					return "false";
				}
			}
		}
	}

}