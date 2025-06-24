package com.OlashAuctionSystem.services;

import com.OlashAuctionSystem.dto.request.UserRegistrationRequest;
import com.OlashAuctionSystem.dto.response.UserRegistrationResponse;
import com.OlashAuctionSystem.exceptions.DuplicateEmailException;
import com.OlashAuctionSystem.exceptions.DuplicateUserNameException;
import com.OlashAuctionSystem.exceptions.IncorrectPasswordException;
import com.OlashAuctionSystem.services.userservices.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    private UserRegistrationResponse regResponse;

    @BeforeEach
    void setUp() {
        userService.deleteAll();

        UserRegistrationRequest newUser = new UserRegistrationRequest();
        newUser.setNin("12345678912");
        newUser.setUserName("Olash");
        newUser.setEmail("emmy@gmail.com");
        newUser.setPassword("password");
        newUser.setConfirmPassword("password");

        regResponse = userService.registerUser(newUser);

    }
    @AfterEach
    void tearDown() {
        userService.deleteAll();
    }

    @Test
    void testUserCanBeRegistered() {
        assertNotNull(regResponse);
        assertEquals(1, userService.count());
        assertEquals("Olash", regResponse.getUserName());
        assertEquals("emmy@gmail.com", regResponse.getEmail());
    }

    @Test
    void testDuplicateEmailExceptionIsThrownIfUserRegistersWithSameEmail() {
        assertNotNull(regResponse);

        UserRegistrationRequest user2 = new UserRegistrationRequest();
        user2.setNin("12345678913");
        user2.setUserName("Ola");
        user2.setEmail("emmy@gmail.com");
        user2.setPassword("password");
        user2.setConfirmPassword("password");

        assertThrows(DuplicateEmailException.class, () -> userService.registerUser(user2));

    }

    @Test
    void testDuplicateUserNameExceptionIsThrownIfUserRegistersWithSameEmail() {
        assertNotNull(regResponse);

        UserRegistrationRequest user2 = new UserRegistrationRequest();
        user2.setNin("12345678913");
        user2.setUserName("Olash");
        user2.setEmail("olash@gmail.com");
        user2.setPassword("password");
        user2.setConfirmPassword("password");

        assertThrows(DuplicateUserNameException.class, () -> userService.registerUser(user2));

    }

    @Test
    void testIncorrectPasswordExceptionIsThrownIfConfirmPasswordDoesNotMatch() {
        assertNotNull(regResponse);

        UserRegistrationRequest user2 = new UserRegistrationRequest();
        user2.setNin("12345678913");
        user2.setUserName("Ola");
        user2.setEmail("olash@gmail.com");
        user2.setPassword("password");
        user2.setConfirmPassword("wrongpassword");

        assertThrows(IncorrectPasswordException.class, () -> userService.registerUser(user2));

    }

}