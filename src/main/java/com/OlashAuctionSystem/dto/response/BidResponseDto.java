package com.OlashAuctionSystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BidResponseDto {
    private String auctionId;
    private String bidderId;
    private BigDecimal bidAmount;
    private LocalDateTime timestamp;
}
