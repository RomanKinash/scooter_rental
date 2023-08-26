package com.example.scooter_rental.service.impl;

import com.example.scooter_rental.model.User;
import com.example.scooter_rental.repository.UserRepository;
import com.example.scooter_rental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void addUser_ok() {
        User userToAdd = new User();
        userToAdd.setId(1L);
        userToAdd.setFirstName("Test");
        userToAdd.setLastName("Test");
        userToAdd.setEmail("test@gmail.com");
        userToAdd.setPassword("test123456789");
        userToAdd.setRole(User.Role.USER);

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(userToAdd.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(userToAdd)).thenReturn(userToAdd);

        User savedUser = userService.add(userToAdd);
        assertNotNull(savedUser);
        assertEquals(userToAdd.getId(), savedUser.getId());
        assertEquals(userToAdd.getFirstName(), savedUser.getFirstName());
        assertEquals(userToAdd.getLastName(), savedUser.getLastName());
        assertEquals(userToAdd.getEmail(), savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(userToAdd.getRole(), savedUser.getRole());

        verify(passwordEncoder, times(1)).encode("test123456789");
        verify(userRepository, times(1)).save(userToAdd);
    }

    @Test
    public void getUserById_ok() {
        Long userId = 1L;
        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("test123456789");
        user.setRole(User.Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User actual = userService.get(userId);
        assertNotNull(actual);
        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getRole(), actual.getRole());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void getUserByIdNotFound_notOk() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.get(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testUpdateUser() {
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setFirstName("Test");
        userToUpdate.setLastName("Test");
        userToUpdate.setEmail("test@gmail.com");
        userToUpdate.setPassword("test123456789");
        userToUpdate.setRole(User.Role.USER);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        User updatedUser = userService.update(userToUpdate);
        assertNotNull(updatedUser);
        assertEquals(userToUpdate.getId(), updatedUser.getId());
        assertEquals(userToUpdate.getFirstName(), updatedUser.getFirstName());
        assertEquals(userToUpdate.getLastName(), updatedUser.getLastName());
        assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
        assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());
        assertEquals(userToUpdate.getRole(), updatedUser.getRole());

        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test
    public void updateUserRole_ok() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Test");
        existingUser.setLastName("Test");
        existingUser.setEmail("test@gmail.com");
        existingUser.setPassword("test123456789");
        existingUser.setRole(User.Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User.Role newRole = User.Role.ADMIN;
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstName("Test");
        updatedUser.setLastName("Test");
        updatedUser.setEmail("test@gmail.com");
        updatedUser.setPassword("test123456789");
        updatedUser.setRole(newRole);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User result = userService.update(userId, newRole);
        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getFirstName(), result.getFirstName());
        assertEquals(existingUser.getLastName(), result.getLastName());
        assertEquals(existingUser.getEmail(), result.getEmail());
        assertEquals(existingUser.getPassword(), result.getPassword());
        assertEquals(newRole, result.getRole());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void findByEmail_ok() {
        String email = "test@gmail.com";
        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("test123456789");
        user.setRole(User.Role.USER);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail(email);
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void findByEmailNotFound_notOk() {
        String email = "nonexistent@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByEmail(email);
        assertFalse(foundUser.isPresent());

        verify(userRepository, times(1)).findByEmail(email);
    }
}