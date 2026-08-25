package service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.RabbitMQConfig;
import repository.UserRepository;
import model.User;

@Service
public class ResetAttemptsConsumer {

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveResetTask(String accno) {
        try {
            Thread.sleep(45000); // ← wait 45 seconds (match your availableAt time)
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // reset after wait
        User user = userRepository.findByAccno(accno);
        if(user != null) {
            user.setFailureAttempts(0);
            user.setAvailableAt(null);
            userRepository.save(user);
            System.out.println("Reset attempts for: " + accno);
        }
    }
}