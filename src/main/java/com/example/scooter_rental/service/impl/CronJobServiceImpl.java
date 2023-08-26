package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@EnableScheduling
@AllArgsConstructor
public class CronJobServiceImpl {
    private RentalService rentalService;
    private TelegramNotificationService notificationService;


    @Scheduled(fixedDelay = 50000)
    public void cronJobOverdueRentals() {
        List<Rental> overdueRentals = rentalService.getOverdueRentals();
        overdueRentals.stream()
                .forEach(rental -> notificationService.sendTelegramMessage(rental.getUser(), getMessage(rental)));
    }

    private static String getMessage(Rental rental) {
        LocalDateTime returnDate = rental.getReturnDate();
        return "Overdue report: " +  "\n"
                + "Your rental number: " + rental.getId() + "\n"
                + "Car: " + rental.getScooter().getModel()
                + " " + rental.getScooter().getBrand() + "\n"
                + "Lease start date: " + rental.getRentalDate() + "\n"
                + "Lease end date: " + returnDate + "\n"
                + "Days overdue: " + ChronoUnit.DAYS.between(returnDate, LocalDateTime.now());
    }
}
