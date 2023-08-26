package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.User;
import com.example.scooter_rental.repository.UserRepository;
import com.example.scooter_rental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Couldn't find user with id " + id));
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User.Role role) {
        User user = get(id);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
