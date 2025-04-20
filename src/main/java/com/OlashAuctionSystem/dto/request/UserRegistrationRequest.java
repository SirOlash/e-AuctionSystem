package com.OlashAuctionSystem.dto.request;

import com.OlashAuctionSystem.data.models.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationRequest {
    @NotBlank(message = "NIN is required!!")
    @Pattern(regexp = "^[0-9]{11}$", message = "NIN must be exactly 11 digits")
    private String nin;

    @NotBlank(message = "UserName is required!!")
    private String userName;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "You need to confirm your password")
    private String confirmPassword;

}
