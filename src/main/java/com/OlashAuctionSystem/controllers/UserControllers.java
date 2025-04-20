package com.OlashAuctionSystem.controllers;

import com.OlashAuctionSystem.dto.request.UserRegistrationRequest;
import com.OlashAuctionSystem.dto.response.UserRegistrationResponse;
import com.OlashAuctionSystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserControllers {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserRegistrationRequest regRequest) {
        UserRegistrationResponse regResponse = userService.registerUser(regRequest);
        return new ResponseEntity<>(regResponse, HttpStatus.CREATED);
    }
}
