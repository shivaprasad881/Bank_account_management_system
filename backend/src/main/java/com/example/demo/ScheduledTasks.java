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

   
}