package com.example.scooter_rental.service;

import com.example.scooter_rental.model.Scooter;

import java.util.List;

public interface ScooterService {
    Scooter add(Scooter scooter);

    Scooter get(Long id);

    List<Scooter> getAll();

    public Scooter update(Long id, Scooter scooter);

    void delete(Long id);
}
