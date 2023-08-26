package com.example.scooter_rental.security;

import com.example.scooter_rental.model.User;
import com.example.scooter_rental.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

class CustomUserDetailsServiceTest {
    private UserService userService;
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        String email = "Bob@gmail.com";
        User alice = new User();
        alice.setEmail(email);
        alice.setPassword("987654321");
        alice.setRole(User.Role.USER);
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(alice));

        UserDetails actual = customUserDetailsService.loadUserByUsername(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getUsername());
        Assertions.assertEquals("987654321", actual.getPassword());
    }

    @Test
    void loadUserByUsername_userNotFound() {
        String email = "Bob@gmail.com";
        User alice = new User();
        alice.setEmail(email);
        alice.setPassword("987654321");
        alice.setRole(User.Role.USER);
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(alice));

        try {
            customUserDetailsService.loadUserByUsername("Alir@gmail.com");
        } catch (Exception e) {
            Assertions.assertEquals("We couldn't find a user with the email address: "
                    + "Alir@gmail.com", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive Exception");
    }
}