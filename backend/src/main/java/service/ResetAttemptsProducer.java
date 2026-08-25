package service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.RabbitMQConfig;

@Service
public class ResetAttemptsProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendResetTask(String accno) {
        System.out.println("Producer sending reset task for: " + accno);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY,
            accno  // message = accno to reset
        );
    }
}