package com.example.scooter_rental.service;

import com.example.scooter_rental.model.Payment;

import java.util.List;

public interface PaymentService {
    Payment save(Payment payment);

    Payment getById(Long id);

    List<Payment> getByUserEmail(String userEmail);

    List<Payment> getByUserId(Long userId);

    List<Payment> findAll();
}
