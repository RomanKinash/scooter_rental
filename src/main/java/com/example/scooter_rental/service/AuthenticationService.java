package com.example.scooter_rental.service;

import com.example.scooter_rental.exception.AuthenticationException;
import com.example.scooter_rental.model.User;

public interface AuthenticationService {
    User register(String email, String firstName, String lastName, String password);

    User login(String email, String password) throws AuthenticationException;
}
