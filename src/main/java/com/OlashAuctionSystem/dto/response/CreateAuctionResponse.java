package com.OlashAuctionSystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateAuctionResponse {
    private String auctionId;
    private String productId;
    private String productUrl;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private String highestBidderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
