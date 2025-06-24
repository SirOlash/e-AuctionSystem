package com.OlashAuctionSystem.dto.response;

import com.OlashAuctionSystem.data.models.ProductCategory;
import com.OlashAuctionSystem.data.models.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreateProductResponse {
    private String productId;
    private String productName;
    private String productDescription;
    private ProductCategory productCategory;
    private BigDecimal startingPrice;
    private String imageUrl;
    private ProductStatus productStatus;
    private String message;
}
