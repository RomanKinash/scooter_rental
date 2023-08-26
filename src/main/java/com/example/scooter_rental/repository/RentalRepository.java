package com.example.scooter_rental.repository;

import com.example.scooter_rental.model.Rental;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserIdAndActualReturnDateIsNull(Long userId, PageRequest request);

    List<Rental> findByUserIdAndActualReturnDateIsNotNull(Long userId, PageRequest request);

    @Query("FROM Rental r " +
            "INNER JOIN FETCH r.scooter " +
            "INNER JOIN FETCH r.user " +
            "WHERE r.actualReturnDate=NULL AND r.returnDate < :localDateTime")
    List<Rental> findAllByActualReturnDateNullAndReturnDateLessThan(LocalDateTime localDateTime);
}
