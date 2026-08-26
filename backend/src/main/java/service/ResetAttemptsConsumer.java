package service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.example.demo.RabbitMQConfig;
import repository.UserRepository;
import model.User;
import com.rabbitmq.client.Channel;
import java.io.IOException;

@Service
public class ResetAttemptsConsumer {

    @Autowired
    private UserRepository userRepository;

   @RabbitListener(queues = RabbitMQConfig.QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void receiveResetTask(String accno, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        User user = userRepository.findByAccno(accno);

        if(user == null){
            System.out.println("No user found for accno: " + accno);
            channel.basicAck(deliveryTag, false); // nothing to do, safe to ack and discard
            return;
        }

        LocalTime availableTime = user.getAvailableAt();

        if(availableTime == null){
            System.out.println("No pending reset needed for: " + accno);
            channel.basicAck(deliveryTag, false); // already reset earlier, safe to ack
            return;
        }

        LocalTime currentTime = LocalTime.now();

        try {
            if(currentTime.isAfter(availableTime) || currentTime.equals(availableTime)) {
                Thread.sleep(0);
            } else {
                long milliseconds = ChronoUnit.MILLIS.between(currentTime, availableTime);
                Thread.sleep(milliseconds);
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            // don't ack — app is likely shutting down, message stays unacked and gets requeued
            return;
        }

        user.setFailureAttempts(0);
        user.setAvailableAt(null);
        userRepository.save(user);
        System.out.println("Reset attempts for: " + accno);

        channel.basicAck(deliveryTag, false); // ← only ack after the DB update actually succeeds
    }
}