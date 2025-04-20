package com.OlashAuctionSystem.dto.response;

import com.OlashAuctionSystem.data.models.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponse {
    private String id;
    private String userName;
    private String email;
    private Role role;
    private String message;
}
