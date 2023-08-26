package com.example.scooter_rental.controller;

import com.example.scooter_rental.dto.request.PaymentRequestDto;
import com.example.scooter_rental.dto.response.PaymentResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.Payment;
import com.example.scooter_rental.service.PaymentCalculationService;
import com.example.scooter_rental.service.PaymentService;
import com.example.scooter_rental.service.StripeService;
import com.stripe.model.checkout.Session;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final StripeService stripeService;
    private final PaymentService paymentService;
    private final DtoMapper<PaymentRequestDto, PaymentResponseDto, Payment> paymentDtoMapper;
    private final PaymentCalculationService paymentCalculationService;

    @Operation(summary = "Creating a payment", description = "Creating a payment")
    @PostMapping
    public PaymentResponseDto create(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        Payment payment = paymentDtoMapper.mapToModel(paymentRequestDto);
        BigDecimal moneyToPay = paymentCalculationService.calculatePaymentAmount(payment);
        BigDecimal moneyToFine = paymentCalculationService.calculateFineAmount(payment);
        payment.setPaymentAmount(moneyToPay);
        Session session = stripeService.createPaymentSession(payment.getPaymentAmount(),
                moneyToFine, payment);
        payment.setPaymentSessionId(session.getId());
        payment.setPaymentUrl(session.getUrl());
        payment = paymentService.save(payment);
        return paymentDtoMapper.mapToDto(payment);
    }

    @Operation(summary = "Getting payments by user id",
            description = "Getting payments by user id")
    @GetMapping
    public List<PaymentResponseDto> getByUserId(@RequestParam Long userId) {
        return paymentService.getByUserId(userId).stream()
                .map(paymentDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Getting own payments",
            description = "Getting own payments")
    @GetMapping("/my-payments")
    public List<PaymentResponseDto> findAllMyPayments(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();
        return paymentService.getByUserEmail(userEmail).stream()
                .map(paymentDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get after successful payment", description = "Get after successful payment")
    @GetMapping("/success/{id}")
        public PaymentResponseDto getSucceed(@PathVariable Long id) {
            Payment payment = paymentService.getById(id);
            payment.setPaymentStatus(Payment.PaymentStatus.PAID);
            return paymentDtoMapper.mapToDto(paymentService.save(payment));
        }

    @GetMapping("/cancel/{id}")
    @Operation(summary = "Get after canceling your payment", description = "Get after canceling your payment")
    public PaymentResponseDto getCanceled(@PathVariable Long id) {
        return paymentDtoMapper.mapToDto(paymentService.save(paymentService.getById(id)));
    }
}
