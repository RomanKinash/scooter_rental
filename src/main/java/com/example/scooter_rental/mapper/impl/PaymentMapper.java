package com.example.scooter_rental.mapper.impl;

import com.example.scooter_rental.dto.request.PaymentRequestDto;
import com.example.scooter_rental.dto.response.PaymentResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.Payment;
import com.example.scooter_rental.model.Rental;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaymentMapper implements DtoMapper<PaymentRequestDto, PaymentResponseDto, Payment> {
    @Override
    public PaymentResponseDto mapToDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentType(payment.getPaymentType());
        dto.setRentalId(payment.getRental().getId());
        dto.setPaymentUrl(payment.getPaymentUrl());
        dto.setPaymentSessionId(payment.getPaymentSessionId());
        dto.setPaymentAmount(payment.getPaymentAmount());
        return dto;
    }

    @Override
    public Payment mapToModel(PaymentRequestDto dto) {
        Payment payment = new Payment();
        payment.setPaymentStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentType(dto.getPaymentType());
        Rental rental = new Rental();
        rental.setId(dto.getRentalId());
        payment.setRental(rental);
        return payment;
    }
}
