package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.exception.AuthenticationException;
import com.example.scooter_rental.model.User;
import com.example.scooter_rental.service.AuthenticationService;
import com.example.scooter_rental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService, passwordEncoder);
    }

    @Test
    public void register_ok() {
        String email = "test@gmail.com";
        String firstName = "Test";
        String lastName = "Test";
        String password = "secretPassword";

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPassword(password);
        newUser.setRole(User.Role.USER);

        when(userService.add(any(User.class))).thenReturn(newUser);

        User registeredUser = authenticationService.register(email, firstName, lastName, password);

        assertNotNull(registeredUser);
        assertEquals(email, registeredUser.getEmail());
        assertEquals(firstName, registeredUser.getFirstName());
        assertEquals(lastName, registeredUser.getLastName());
        assertEquals(password, registeredUser.getPassword());
        assertEquals(User.Role.USER, registeredUser.getRole());

        verify(userService, times(1)).add(any(User.class));
    }

    @Test
    public void login_ok() throws AuthenticationException {
        String email = "test@gmail.com";
        String password = "secretPassword";
        String hashedPassword = "hashedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);

        User loggedInUser = authenticationService.login(email, password);
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
        assertEquals(hashedPassword, loggedInUser.getPassword());

        verify(userService, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, hashedPassword);
    }

    @Test
    public void loginInvalidEmail_notOk() {
        String email = "test@gmail.com";
        String password = "secretPassword";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () -> authenticationService.login(email, password));

        verify(userService, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    public void testLoginInvalidPassword() {
        String email = "test@gmail.com";
        String password = "secretPassword";
        String hashedPassword = "hashedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(false);
        assertThrows(AuthenticationException.class, () -> authenticationService.login(email, password));

        verify(userService, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, hashedPassword);
    }
}