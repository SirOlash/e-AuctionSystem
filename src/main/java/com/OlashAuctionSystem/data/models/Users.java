package com.OlashAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "user")
public class Users {
    @Id
    private String id;
    private String bvn;
    private String email;
    private String password;
    private Role role;
    private BigDecimal balance = BigDecimal.ZERO;

}
