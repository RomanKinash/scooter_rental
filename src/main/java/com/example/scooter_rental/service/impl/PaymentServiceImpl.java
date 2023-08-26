package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Payment;
import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.model.User;
import com.example.scooter_rental.repository.PaymentRepository;
import com.example.scooter_rental.service.PaymentService;
import com.example.scooter_rental.service.RentalService;
import com.example.scooter_rental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final TelegramNotificationService telegramNotificationService;
    private final UserService userService;
    private final RentalService rentalService;

    @Override
    public Payment save(Payment payment) {
        Payment save = paymentRepository.save(payment);
        Rental rental = rentalService.find(payment.getRental().getId());
        User user = userService.get(rental.getUser().getId());
        rental.setUser(user);
        payment.setRental(rental);
        createNotification(payment, user);
        return save;
    }

    private void createNotification(Payment payment, User user) {
        telegramNotificationService.sendTelegramMessage(user, "New payment created:"
                + " Payment ID - " + payment.getId() + "\n" +
                ", User - " + user.getFirstName() + " " + user.getLastName() +
                ", Scooter ID- " + payment.getRental().getScooter().getId() +
                ", Scooter model " + payment.getRental().getScooter().getModel() +
                ", Scooter brand " + payment.getRental().getScooter().getBrand() + "\n" +
                ", Rental ID - " + payment.getRental().getId() +
                ", Amount - " + payment.getPaymentAmount() +
                ", Status - " + payment.getPaymentStatus());
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can`t find payment by id: " + id));
    }

    @Override
    public List<Payment> getByUserEmail(String userEmail) {
        return paymentRepository.findPaymentByRentalUserEmail(userEmail);
    }

    @Override
    public List<Payment> getByUserId(Long userId) {
        return paymentRepository.findPaymentByRentalUserId(userId);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
