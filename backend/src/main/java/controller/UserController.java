package controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.EmailUtil;
import com.example.demo.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.UserService;

import java.time.LocalTime;
import java.util.Map;

import model.Email;
import model.User;

import repository.UserRepository;
import repository.EmailRepository;


@RestController
public class UserController {

   @Autowired
    private UserRepository userRepository;

	@Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserService userService;

	@Autowired
    private EmailUtil emailUtil;

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
					return "true";
				} 
				else {
					return "false";
				}
			}
		}
	}

}