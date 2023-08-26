package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.repository.ScooterRepository;
import com.example.scooter_rental.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private ScooterRepository scooterRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    void testFindById() {
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        Rental foundRental = rentalService.find(rentalId);

        assertNotNull(foundRental);
        assertEquals(rentalId, foundRental.getId());

        verify(rentalRepository, times(1)).findById(rentalId);
    }

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        boolean isActive = true;
        int page = 0;
        int size = 10;
        PageRequest request = PageRequest.of(page, size);

        List<Rental> activeRentals = Arrays.asList(new Rental(), new Rental(), new Rental());

        when(rentalRepository.findByUserIdAndActualReturnDateIsNull(userId, request))
                .thenReturn(activeRentals);

        List<Rental> foundRentals = rentalService.findByUSerId(userId, isActive, request);

        assertNotNull(foundRentals);
        assertEquals(activeRentals.size(), foundRentals.size());

        verify(rentalRepository, times(1)).findByUserIdAndActualReturnDateIsNull(userId, request);
    }
}