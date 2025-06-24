package com.OlashAuctionSystem.services.productservices;

import com.OlashAuctionSystem.dto.request.CreateProductRequest;
import com.OlashAuctionSystem.dto.response.CreateProductResponse;

public interface IProductActivities {
    CreateProductResponse addProduct(String jwtToken, CreateProductRequest productRequest);
}
