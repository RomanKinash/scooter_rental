package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Scooter;
import com.example.scooter_rental.repository.ScooterRepository;
import com.example.scooter_rental.service.ScooterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScooterServiceImplTest {
    private ScooterRepository scooterRepository;
    private ScooterService scooterService;

    @BeforeEach
    public void setUp() {
        scooterRepository = Mockito.mock(ScooterRepository.class);
        scooterService = new ScooterServiceImpl(scooterRepository);
    }

    @Test
    public void testAddCar() {
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setModel("Pro2");
        scooter.setBrand("Xiaomi");
        scooter.setType(Scooter.ScooterType.ELECTRIC);
        scooter.setInventory(3);
        scooter.setDailyFee(BigDecimal.valueOf(20));
        when(scooterRepository.save(scooter)).thenReturn(scooter);

        Scooter savedScooter = scooterService.add(scooter);
        assertNotNull(savedScooter);
        assertEquals(scooter.getId(), savedScooter.getId());
        assertEquals(scooter.getModel(), savedScooter.getModel());
        assertEquals(scooter.getBrand(), savedScooter.getBrand());
        assertEquals(scooter.getInventory(), savedScooter.getInventory());
        assertEquals(scooter.getDailyFee(), savedScooter.getDailyFee());
        assertEquals(scooter.getType(), savedScooter.getType());

        verify(scooterRepository, times(1)).save(scooter);
    }


    @Test
    public void getAllCars_ok() {
        List<Scooter> scooters = new ArrayList<>();
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setModel("Pro2");
        scooter.setBrand("Xiaomi");
        scooter.setType(Scooter.ScooterType.ELECTRIC);
        scooter.setInventory(3);
        scooter.setDailyFee(BigDecimal.valueOf(20));
        scooters.add(scooter);

        when(scooterRepository.findAll()).thenReturn(scooters);
        List<Scooter> actual = scooterService.getAll();
        assertNotNull(actual);
        assertEquals(scooters, actual);
        verify(scooterRepository, times(1)).findAll();
    }

    @Test
    public void deleteCar_ok() {
        Long scooterId = 1L;
        scooterService.delete(scooterId);
        verify(scooterRepository, times(1)).deleteById(scooterId);
    }

    @Test
    public void getCarByIdNotFound_notOk() {
        Long scooterId = 1L;
        when(scooterRepository.findById(scooterId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> scooterService.get(scooterId));
        verify(scooterRepository, times(1)).findById(scooterId);
    }

    @Test
    public void testGetCarById() {
        Long scooterId = 1L;
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setModel("Pro2");
        scooter.setBrand("Xiaomi");
        scooter.setType(Scooter.ScooterType.ELECTRIC);
        scooter.setInventory(3);
        scooter.setDailyFee(BigDecimal.valueOf(20));
        when(scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));

        Scooter actual = scooterService.get(scooterId);
        assertNotNull(actual);
        assertEquals(scooter.getId(), actual.getId());
        assertEquals(scooter.getModel(), actual.getModel());
        assertEquals(scooter.getBrand(), actual.getBrand());
        assertEquals(scooter.getType(), actual.getType());
        assertEquals(scooter.getInventory(), actual.getInventory());
        assertEquals(scooter.getDailyFee(), actual.getDailyFee());
        verify(scooterRepository, times(1)).findById(scooterId);
    }

    @Test
    public void updateCar_ok() {
        Long scooterId = 1L;
        Scooter existingScooter = new Scooter();
        existingScooter.setId(1L);
        existingScooter.setModel("Pro2");
        existingScooter.setBrand("Xiaomi");
        existingScooter.setType(Scooter.ScooterType.ELECTRIC);
        existingScooter.setInventory(3);
        existingScooter.setDailyFee(BigDecimal.valueOf(20));
        when(scooterRepository.findById(scooterId)).thenReturn(Optional.of(existingScooter));

        Scooter updatedScooter = new Scooter();
        updatedScooter.setId(1L);
        updatedScooter.setModel("5");
        updatedScooter.setBrand("Acer");
        updatedScooter.setType(Scooter.ScooterType.ELECTRIC);
        updatedScooter.setInventory(2);
        updatedScooter.setDailyFee(BigDecimal.valueOf(50));
        when(scooterRepository.save(any(Scooter.class))).thenReturn(updatedScooter);

        Scooter result = scooterService.update(scooterId, updatedScooter);
        assertNotNull(result);
        assertEquals(updatedScooter.getId(), result.getId());
        assertEquals(updatedScooter.getModel(), result.getModel());
        assertEquals(updatedScooter.getBrand(), result.getBrand());
        assertEquals(updatedScooter.getType(), result.getType());
        assertEquals(updatedScooter.getInventory(), result.getInventory());
        assertEquals(updatedScooter.getDailyFee(), result.getDailyFee());

        verify(scooterRepository, times(1)).findById(scooterId);
        verify(scooterRepository, times(1)).save(any(Scooter.class));
    }
}