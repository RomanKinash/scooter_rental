package com.example.scooter_rental.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalRequestDto {
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private Long scooterId;
    private Long userId;
}
