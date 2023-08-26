package com.example.scooter_rental.dto.request;

import com.example.scooter_rental.model.Payment;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    private Payment.PaymentStatus paymentStatus;
    private Payment.PaymentType paymentType;
    private Long rentalId;
    private BigDecimal paymentAmount;
}
