package com.OlashAuctionSystem.dto.response;

import com.OlashAuctionSystem.data.models.Bid;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AuctionPageResponseDto {
    private String auctionId;
    private String productId;
    private BigDecimal currentPrice;
    private List<Bid> topBids;
    private LocalDateTime endTime;

}
