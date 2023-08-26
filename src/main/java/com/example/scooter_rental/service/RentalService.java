package com.example.scooter_rental.service;

import com.example.scooter_rental.model.Rental;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RentalService {

    Rental save(Rental e);

    Rental find(Long id);

    List<Rental> findByUSerId(Long id, boolean isActive, PageRequest request);

    Rental returnScooter(Long id);

    List<Rental> getOverdueRentals();
}
