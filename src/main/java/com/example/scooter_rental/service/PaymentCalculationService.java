package com.example.scooter_rental.service;

import com.example.scooter_rental.model.Payment;

import java.math.BigDecimal;

public interface PaymentCalculationService {
    BigDecimal calculatePaymentAmount(Payment payment);

    BigDecimal calculateFineAmount(Payment payment);
}
