package com.example.demo;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;
import com.sendgrid.helpers.mail.Mail;

import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import repository.EmailRepository;


@Component
public class EmailUtil {

    @Autowired
    private EmailRepository emailRepository;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public boolean sendEmail(String toEmail, String subject, String msg) {

        try {
            Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(toEmail),
                new Content("text/plain", msg)
            );

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println("SendGrid status: " + response.getStatusCode());

            if (response.getStatusCode() != 202) {
                System.out.println("SendGrid error body: " + response.getBody());
                return false;
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Email error: " + e.getMessage());
            return false;
        }
    }

    public boolean sendotpp(String email) {

        String generated_otp = 1000 + (int)(Math.random() * 9000) + "";

        model.Email curr_user = emailRepository.findByEmail(email);

        if(curr_user == null){
            model.Email new_user = new model.Email(email, generated_otp, LocalTime.now().plusSeconds(50));
            emailRepository.save(new_user);
        }
        else{
            curr_user.setOtp(generated_otp);
            curr_user.setExpireAt(LocalTime.now().plusSeconds(50));
            emailRepository.save(curr_user);
        }

        String message = "Your OTP is : " + generated_otp + "\n\nPlease dont share the OTP !!";
        String subject = "Your OTP for Password-Reset";

        return sendEmail(email, subject, message);
    }
}