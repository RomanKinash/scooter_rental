package com.example.scooter_rental.dto.response;

import com.example.scooter_rental.model.Payment;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponseDto {
    private Long id;
    private Payment.PaymentStatus paymentStatus;
    private Payment.PaymentType paymentType;
    private Long rentalId;
    private String paymentUrl;
    private String paymentSessionId;
    private BigDecimal paymentAmount;
}
