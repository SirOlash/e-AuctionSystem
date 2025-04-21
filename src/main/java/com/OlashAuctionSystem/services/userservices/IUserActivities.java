package com.OlashAuctionSystem.services.userservices;

import com.OlashAuctionSystem.dto.request.UserLoginRequest;
import com.OlashAuctionSystem.dto.request.UserRegistrationRequest;
import com.OlashAuctionSystem.dto.response.UserLoginResponse;
import com.OlashAuctionSystem.dto.response.UserRegistrationResponse;

public interface IUserActivities {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    UserLoginResponse loginUser(UserLoginRequest loginRequest);
    void deleteAll();
    long count();
}
