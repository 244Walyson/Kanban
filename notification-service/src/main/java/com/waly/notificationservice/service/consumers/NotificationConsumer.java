package com.waly.notificationservice.service.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waly.notificationservice.entities.Status;
import com.waly.notificationservice.entities.User;
import com.waly.notificationservice.entities.UserNotification;
import com.waly.notificationservice.repositories.UserNotificationRepository;
import com.waly.notificationservice.repositories.UserRepository;
import com.waly.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final UserNotificationRepository chatUserRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.user.notification.topic}"
    )
    public void consumerMessageNotification(String message) {
        log.info("Chat: " + message);
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UserNotification notification = objectMapper.convertValue(jsonNode, UserNotification.class);

            sendPushNotification(notification);

        } catch (Exception e) {
            log.info("Error while processing notification");
            e.printStackTrace();
        }
    }


    @Transactional
    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.user.chat-created-notification.topic}"
    )
    public void consumerUserConnectionNotification(String message) {
        log.info("Chat: " + message);
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UserNotification notification = objectMapper.convertValue(jsonNode, UserNotification.class);

            User receiver = userRepository.findById(notification.getReceiver().getId()).orElse(null);
            User sender = userRepository.findById(notification.getSender().getId()).orElse(null);

            notification.setStatus(Status.DELIVERED);

            if (sender == null) {
                log.info("user {} not registered to receive notification", notification.getSender().getId());
                log.info("user name {}", notification.getSender().getNickname());
                userRepository.save(notification.getSender());
            }

            if (receiver == null) {
                userRepository.save(notification.getReceiver());
                log.info("user {} not registered to receive notification", notification.getReceiver().getId());
                notification.setStatus(Status.FAILED);
            }

            notification.setTitle("Chat criado");
            notification.setMessage("Um novo chat foi criado com " + notification.getSender().getNickname());
            notification = chatUserRepository.save(notification);

            if (notification.getStatus() == Status.FAILED) {
                return;
            }
            notificationService.sendPushNotification(notification);

        } catch (Exception e) {
            log.info("Error while processing notification");
            e.printStackTrace();
        }
    }


    private void sendPushNotification(UserNotification notification) throws JsonProcessingException {
        User receiver = userRepository.findById(notification.getReceiver().getId()).orElse(null);
        User sender = userRepository.findById(notification.getSender().getId()).orElse(null);

        notification.setStatus(Status.DELIVERED);

        if (sender == null) {
            log.info(new ObjectMapper().writeValueAsString(notification.getSender()));
            sender = userRepository.save(notification.getSender());
        }

        if (receiver == null) {
            receiver = userRepository.save(notification.getReceiver());
            log.info("user {} not registered to receive notification", notification.getReceiver().getId());
            notification.setStatus(Status.FAILED);
        }

        notification.setTitle("Novo pedido de conexão");
        notification.setMessage("Você recebeu um novo pedido de conexão de " + sender.getNickname());
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification = chatUserRepository.save(notification);

        log.info(new ObjectMapper().writeValueAsString(notification));

        if (notification.getStatus() == Status.FAILED) {
            return;
        }

        notificationService.sendPushNotification(notification);
    }


}
