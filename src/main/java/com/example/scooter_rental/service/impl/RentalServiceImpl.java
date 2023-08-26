package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Scooter;
import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.model.User;
import com.example.scooter_rental.repository.RentalRepository;
import com.example.scooter_rental.service.ScooterService;
import com.example.scooter_rental.service.NotificationService;
import com.example.scooter_rental.service.RentalService;
import com.example.scooter_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final ScooterService scooterService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    public Rental save(Rental rental) {
        Scooter scooter = scooterService.get(rental.getScooter().getId());
        rental.setScooter(scooter);
        if (scooter.getInventory() == 0) {
            throw new RuntimeException("Can't decrease scooter inventory: " + rental);
        }
        scooter.setInventory(scooter.getInventory() - 1);
        scooterService.add(scooter);
        Rental createdRental = rentalRepository.save(rental);
        User user = userService.get(createdRental.getUser().getId());
        notificationService.sendTelegramMessage(user, String
                .format("New rental was created.\n"
                                + "Rental info: %s\n"
                                + "User info: %s\n"
                                + "Scooter info: %s", createdRental, user, scooter));
        return createdRental;
}

    @Override
    public Rental find(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can`t find rental with id:" + id));
    }

    @Override
    public List<Rental> findByUSerId(Long id, boolean isActive, PageRequest request) {
        if (isActive) {
            return rentalRepository.findByUserIdAndActualReturnDateIsNull(id, request);
        }
        return rentalRepository.findByUserIdAndActualReturnDateIsNotNull(id, request);
    }

    public Rental returnScooter(Long id) {
        Rental rentalToUpdate = find(id);
        Scooter scooter = rentalToUpdate.getScooter();
        scooter.setInventory(scooter.getInventory() + 1);
        scooterService.update(scooter.getId(), scooter);
        rentalToUpdate.setActualReturnDate(LocalDateTime.now());
        return rentalRepository.save(rentalToUpdate);
    }

    @Override
    public List<Rental> getOverdueRentals() {
        return rentalRepository.findAllByActualReturnDateNullAndReturnDateLessThan(LocalDateTime.now());
    }
}
