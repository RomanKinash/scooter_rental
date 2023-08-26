package com.example.scooter_rental.service;

import com.example.scooter_rental.model.User;

public interface NotificationService {
    void sendTelegramMessage(User user, String message);
}
