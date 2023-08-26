package com.example.scooter_rental.controller;

import com.example.scooter_rental.dto.request.RentalRequestDto;
import com.example.scooter_rental.dto.response.RentalResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/rentals")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Rental", description = "The Rental API. "
        + "Contains all the operations that can be performed on a customer/manager.")
public class RentalController {
    private final RentalService rentalService;
    private final DtoMapper <RentalRequestDto, RentalResponseDto, Rental> rentalMapper;

    @Operation(summary = "Add rental", description = "Add rental")
    @PostMapping
    public RentalResponseDto add(@RequestBody RentalRequestDto rentalRequestDto) {
        Rental createdRental = rentalService.save(rentalMapper.mapToModel(rentalRequestDto));
        return rentalMapper.mapToDto(createdRental);
    }

    @Operation(summary = "Get rental by user and status",
            description = "Get rental by user and status")
    @GetMapping
    public List<RentalResponseDto> getRentalsByUserIdAndStatus(
            @RequestParam(name = "user_id") Long id,
            @RequestParam(name = "is_active") boolean isActive,
            @RequestParam(defaultValue = "20") Integer count,
            @RequestParam(defaultValue = "0") Integer page) {
        PageRequest pageRequest = PageRequest.of(page, count);
        return rentalService.findByUSerId(id, isActive, pageRequest)
                .stream()
                .map(rentalMapper::mapToDto)
                .toList();
    }

    @Operation(summary = "Get rental by rental id", description = "Get rental by rental id")
    @GetMapping("/{rentalId}")
    public RentalResponseDto get(@PathVariable Long rentalId) {
        return rentalMapper.mapToDto(rentalService.find(rentalId));
    }

    @Operation(summary = "Set actual return date ", description = "Set actual return date ")
    @PostMapping("/return/{id}")
    public RentalResponseDto returnCar(@PathVariable Long id) {
        return rentalMapper.mapToDto(rentalService.returnScooter(id));
    }
}
