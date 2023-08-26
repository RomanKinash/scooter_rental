package com.example.scooter_rental.repository;

import com.example.scooter_rental.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findPaymentByRentalUserId(Long userId);

    List<Payment> findPaymentByRentalUserEmail(String userEmail);
}
