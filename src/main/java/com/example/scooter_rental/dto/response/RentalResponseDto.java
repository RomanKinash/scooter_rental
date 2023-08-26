package com.example.scooter_rental.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalResponseDto {
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    private Long scooterId;
    private Long userId;
}

