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
    public void scheduleTaskAt(LocalTime targetTime, String accno) {
        LocalTime now = LocalTime.now();
        long delay = Duration.between(now, targetTime).toMillis();

        if (delay < 0) {
            delay = Duration.between(now, targetTime.plusHours(24)).toMillis();
        }

        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    func_after_triggering(accno);   // ✅ calls function
                });
    }

    // function that runs after triggering
    public void func_after_triggering(String accno) {
        //this function is executed - it means the time is completed - now make the attempst and time 0 - so that the user would start attempting

        User user = userRepository.findByAccno(accno);

        //refresh
        user.setFailureAttempts(0);
        user.setAvailableAt(null);

        userRepository.save(user);

        //done we are successfully refreshed the attempts at the scheduled time - now the user would try attempting
    }

}