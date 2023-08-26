package com.example.scooter_rental.service;

import com.example.scooter_rental.model.Payment;
import com.stripe.model.checkout.Session;

import java.math.BigDecimal;

public interface StripeService {
    Session createPaymentSession(BigDecimal payment, BigDecimal fine, Payment paymentObject);
}
