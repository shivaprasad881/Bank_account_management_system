package service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import model.User;
import repository.UserRepository;

@Service
public class SchedulerService {

    @Autowired
    private UserRepository userRepository;

    @Async
    public void scheduleTaskAt(LocalTime targetTime,String task, String[] userdata) {
        LocalTime now = LocalTime.now();
        long delay = Duration.between(now, targetTime).toMillis();

        if (delay < 0) {
            delay = Duration.between(now, targetTime.plusHours(24)).toMillis();
        }

        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                .execute(() -> {

                    if(task.equals("reset_user_login_attempts")){
                        String accno = userdata[0];

                        reset_user_login_attempts(accno);   // ✅ calls function
                    }
                    else if(task.equals("refresh_the_user_session_activity")){

                        String accno = userdata[0];
                        String token = userdata[1];

                        refresh_the_user_session_activity(accno,token);

                    }
                    
                });
    }

    // function that runs after triggering
    public void reset_user_login_attempts(String accno) {
        //this function is executed - it means the time is completed - now make the attempst and time 0 - so that the user would start attempting

        User user = userRepository.findByAccno(accno);

        //refresh
        user.setFailureAttempts(0);
        user.setAvailableAt(null);

        userRepository.save(user);

        //done we are successfully refreshed the attempts at the scheduled time - now the user would try attempting
    }

     public void refresh_the_user_session_activity(String accno,String token) {
        //this function is executed - it means the time is completed - now make the attempst and time 0 - so that the user would start attempting

        User user = userRepository.findByAccno(accno);

        //task :- refresh user activity

        //note : check which token is currently active , if our tasked token is already wiped out ,it means someone already does our task , so we are done , we dont bother about the new token current existing 

        String curr_active_token = user.getCurrentToken();


        if(curr_active_token!=null){

            //there is a task to do  , check whether its our task or not

            if(curr_active_token.equals(token)){
                //hoo its task - we need to do it - refresh it
                user.setCurrentToken(null);
                user.setIsLoggedIn(false);
            }
            // hoo its not our task , its someone else task , so we are done
            
        }
        //hoo no token is active now , we dont have any task to do , we are done without doing any task 

        

        userRepository.save(user);

        //done we are successfully refreshed the attempts at the scheduled time - now the user would try attempting
    }

}