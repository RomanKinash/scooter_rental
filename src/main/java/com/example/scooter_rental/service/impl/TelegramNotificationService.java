package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.User;
import com.example.scooter_rental.service.NotificationService;
import com.example.scooter_rental.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class TelegramNotificationService extends TelegramLongPollingBot implements NotificationService {
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;
    private final UserService userService;

    public TelegramNotificationService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String userText = message.getText();
            Long chatId = message.getFrom().getId();

            if (userText.equals("/start")) {
                handleStartCommand(chatId);
            } else {
                handleUserInput(chatId, userText);
            }
        }
    }

    public void sendTelegramMessage(User user, String message) {
        sendMessage(SendMessage.builder()
                .chatId(user.getChatId())
                .text(message)
                .build());
    }

    private void handleStartCommand(Long chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Welcome to our CarSharingService bot!\n"
                        + "You'll receive any significant notifications about our cooperation here. "
                        + "To begin, please, enter your email:")
                .build();
        sendMessage(message);
    }

    private void handleUserInput(Long chatId, String userEmail) {
        Optional<User> userOptional = userService.findByEmail(userEmail);
        SendMessage message;

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setChatId(chatId);
            userService.update(user);
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Hello " + userOptional.get().getFirstName() + "!\n"
                            + "We can send you notifications now :) Stay tuned! ;)")
                    .build();
        } else {
            message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Email is invalid.")
                    .build();
        }
        sendMessage(message);
    }

    private void sendMessage(SendMessage message) {
        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't send a message", e);
        }
    }
}
