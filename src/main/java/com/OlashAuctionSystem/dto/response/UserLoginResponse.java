package com.OlashAuctionSystem.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    private String token;
    private String message;
}
