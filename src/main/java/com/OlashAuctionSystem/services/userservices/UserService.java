package com.OlashAuctionSystem.services.userservices;

import com.OlashAuctionSystem.data.models.Users;
import com.OlashAuctionSystem.data.repositories.UserRepository;
import com.OlashAuctionSystem.dto.request.UserLoginRequest;
import com.OlashAuctionSystem.dto.request.UserRegistrationRequest;
import com.OlashAuctionSystem.dto.response.UserLoginResponse;
import com.OlashAuctionSystem.dto.response.UserRegistrationResponse;
import com.OlashAuctionSystem.exceptions.*;
import com.OlashAuctionSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class UserService implements IUserActivities {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
        String hashedNIN = passwordEncoder.encode(userRegistrationRequest.getNin());
        user.setNIN(hashedNIN);

        user.setUserName(userRegistrationRequest.getUserName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setRole(userRegistrationRequest.getRole());

        String hashedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
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
    public UserLoginResponse loginUser(UserLoginRequest loginRequest){
        Users user = userRepository.findByEmail(loginRequest.getUsernameOrEmail())
                .or(() -> userRepository.findByUserName(loginRequest.getUsernameOrEmail()))
                .orElseThrow(() -> new InvalidCredentialException("Invalid Credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException("Invalid Credentials");
        }

        Map<String, Object> claims = Map.of(
                "id", user.getId(),
                "email",user.getEmail(),
                "username",user.getUserName(),
                "role", user.getRole().name()
        );
        String token = jwtUtil.generateToken(claims, user.getEmail(),user.getRole());

        return UserLoginResponse.builder()
                .token(token)
                .message("User: " + user.getUserName() + " logged in Successfully")
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
