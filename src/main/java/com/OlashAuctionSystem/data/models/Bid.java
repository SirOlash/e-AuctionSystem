package com.OlashAuctionSystem.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bids")
public class Bid {
    @Id
    private String id;
    private String auctionId;
    private String bidderId;
    private BigDecimal amount;
    private LocalDateTime timestamp;

}
