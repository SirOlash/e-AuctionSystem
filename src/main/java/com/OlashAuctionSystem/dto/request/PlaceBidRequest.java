package com.OlashAuctionSystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlaceBidRequest {
    @NotNull(message = "Bid is Required")
    @DecimalMin(value = "1001", message = "Must be more than ₦1,000")
    @Digits(integer = 12, fraction = 2, message = "Up to 2 decimal places allowed")
    private BigDecimal amount;
}
