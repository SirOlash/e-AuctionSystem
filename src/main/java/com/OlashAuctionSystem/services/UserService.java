package com.OlashAuctionSystem.services;

import com.OlashAuctionSystem.data.models.Users;
import com.OlashAuctionSystem.data.repositories.UserRepository;
import com.OlashAuctionSystem.dto.request.UserRegistrationRequest;
import com.OlashAuctionSystem.dto.response.UserRegistrationResponse;
import com.OlashAuctionSystem.exceptions.DuplicateNINException;
import com.OlashAuctionSystem.exceptions.DuplicateEmailException;
import com.OlashAuctionSystem.exceptions.DuplicateUserNameException;
import com.OlashAuctionSystem.exceptions.IncorrectPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService implements IUserActivities {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email is already in use");
        }
        if (userRepository.findByNIN(userRegistrationRequest.getNin()).isPresent()) {
            throw new DuplicateNINException("NIN is already in use");
        }

        if (userRepository.findByUserName(userRegistrationRequest.getUserName()).isPresent()) {
            throw new DuplicateUserNameException("UserName is already taken");
        }

        if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getConfirmPassword())) {
            throw new IncorrectPasswordException("Password does not match");
        }

        Users user = new Users();
        String hashedNIN = encoder.encode(userRegistrationRequest.getNin());
        user.setNIN(hashedNIN);

        user.setUserName(userRegistrationRequest.getUserName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setRole(userRegistrationRequest.getRole());

        String hashedPassword = encoder.encode(userRegistrationRequest.getPassword());
        user.setPassword(hashedPassword);

        user.setBalance(BigDecimal.ZERO);

        Users savedUser = userRepository.save(user);

        return UserRegistrationResponse.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .message("You have Registered Successfully !!!")
                .build();
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public long count() {
        return userRepository.count();
    }
}
