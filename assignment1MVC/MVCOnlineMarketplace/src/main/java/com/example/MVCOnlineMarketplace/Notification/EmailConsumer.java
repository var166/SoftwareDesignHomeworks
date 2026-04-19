package com.example.MVCOnlineMarketplace.Notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "email-queue")
public class EmailConsumer {

}
