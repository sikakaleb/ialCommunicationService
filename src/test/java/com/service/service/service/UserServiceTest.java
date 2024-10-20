package com.service.service.service;

import com.service.service.model.User;
import com.service.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        // Given
        User user1 = new User();
        user1.setId("1");
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId("2");
        user2.setName("Jane Doe");

        List<User> users = Arrays.asList(user1, user2);

        // Mocking repository behavior
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSaveUser() {
        // Given
        User user = new User();
        user.setId("1");
        user.setName("John Doe");

        // When
        userService.saveUser(user);

        // Then
        verify(userRepository, times(1)).save(user);
    }
}
