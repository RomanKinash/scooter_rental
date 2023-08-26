package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.Scooter;
import com.example.scooter_rental.repository.ScooterRepository;
import com.example.scooter_rental.service.ScooterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ScooterServiceImpl implements ScooterService {
    private final ScooterRepository scooterRepository;

    @Override
    public Scooter add(Scooter scooter) {
        return scooterRepository.save(scooter);
    }

    @Override
    public Scooter get(Long id) {
        return scooterRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("Can't find scooter with id " + id));
    }

    @Override
    public List<Scooter> getAll() {
        return scooterRepository.findAll();
    }

    @Override
    public Scooter update(Long id, Scooter scooter) {
        Scooter scooterDB = get(id);
        scooterDB.setModel(scooter.getModel());
        scooterDB.setBrand(scooter.getBrand());
        scooterDB.setInventory(scooter.getInventory());
        scooterDB.setDailyFee(scooter.getDailyFee());
        scooterDB.setType(scooter.getType());
        return scooterRepository.save(scooterDB);
    }

    @Override
    public void delete(Long id) {
        scooterRepository.deleteById(id);
    }
}
