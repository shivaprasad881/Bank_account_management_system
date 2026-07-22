package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import java.sql.Timestamp;
import java.sql.Date;

@Component
public class ScheduledTasks {

    @Autowired
    private UserRepository userRepository;

    

    @Scheduled(cron = "0 0 21 * * SUN", zone = "Asia/Kolkata") // runs every night at 2 AM
    public void cleanupExpiredBlacklistTokens()   throws IOException{

        List<User> allusers = userRepository.findAll();

        for(User user :allusers){

            ObjectMapper mapper = new ObjectMapper();
            String old_black_list_string = user.getInvalidJwtTokens();

            List<String> new_tokenList = JwtUtil.tokenCleanUp(old_black_list_string);

            String updated_black_list_string = mapper.writeValueAsString(new_tokenList);

            user.setLogoutCount( new_tokenList.size() );
            user.setInvalidJwtTokens(updated_black_list_string);
            userRepository.save(user);

        }


    }



   @Scheduled(cron = "0 10 18 * * *", zone = "Asia/Kolkata")
    public void blockInactiveUsersAccounts()   throws IOException{

        List<User> allusers = userRepository.findAll();

        //curtime ---- last_active_time  <  60 days = active

        for(User user :allusers){

            Timestamp lastActiveTime = user.getLastActiveAt();
            Timestamp currTime = new Timestamp(System.currentTimeMillis());

            long diffInMillis = currTime.getTime() - lastActiveTime.getTime();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);

            if(diffInDays>60){
                //the user is inactive - block the account - indicate the user about the action


                user.setAccountStatus("blocked");
                userRepository.save(user);
            }
            //the user is active - serve the services

            
            

        }


    }
   
}