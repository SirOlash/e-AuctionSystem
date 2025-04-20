package com.OlashAuctionSystem.services;

import com.OlashAuctionSystem.dto.request.UserRegistrationRequest;
import com.OlashAuctionSystem.dto.response.UserRegistrationResponse;

public interface IUserActivities {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    void deleteAll();
    long count();
}
