package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Payment;
import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.model.Scooter;
import com.example.scooter_rental.service.ScooterService;
import com.example.scooter_rental.service.PaymentCalculationService;
import com.example.scooter_rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PaymentCalculationServiceImpl implements PaymentCalculationService {
    private static final double FINE_MULTIPLIER = 1.2;
    private final RentalService rentalService;
    private final ScooterService scooterService;

    @Override
    public BigDecimal calculatePaymentAmount(Payment payment) {
        Rental rental = rentalService.find(payment.getRental().getId());
        Scooter scooter = scooterService.get(rental.getScooter().getId());
        long rentalDuration = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate());
        return scooter.getDailyFee().multiply(BigDecimal.valueOf(rentalDuration));
    }

    @Override
    public BigDecimal calculateFineAmount(Payment payment) {
        Rental rental = rentalService.find(payment.getRental().getId());
        long overdueDays = rental.getActualReturnDate() != null
                ? ChronoUnit.DAYS.between(rental.getReturnDate(), rental.getActualReturnDate()) : 0;
        BigDecimal dailyFee = rental.getScooter().getDailyFee();
        return dailyFee.multiply(BigDecimal.valueOf((overdueDays)
                * FINE_MULTIPLIER)).divide(BigDecimal.valueOf(100), RoundingMode.UP);
    }
}
