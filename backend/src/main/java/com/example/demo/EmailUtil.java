package com.example.demo;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import model.Email;
import repository.EmailRepository;


@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository; 

   

    @Value("${spring.mail.username}")
    private String fromEmail;   // pulled automatically from properties!

    public void sendEmail(String toEmail,String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }

    public void sendotpp(String email){

        //generate otp - store it - send it

        String generated_otp = 1000 + (int)(Math.random() * 9000) + "";

        Email curr_user = emailRepository.findByEmail(email);


        if(curr_user==null){
            //fresh user

            //create the user
            Email new_user = new Email(email, generated_otp, LocalTime.now().plusSeconds(50));

            
            emailRepository.save(new_user);

        }
        else{
            //already present - update the details

            curr_user.setOtp(generated_otp);
            curr_user.setExpireAt( LocalTime.now().plusSeconds(50) );

            emailRepository.save(curr_user);
        }






        String message = "Your OTP is : "+ generated_otp+"\n\nPlease dont share the OTP !!";
        String subject = "Your OTP for Password-Reset";

        sendEmail(email,subject,message);
    }
}