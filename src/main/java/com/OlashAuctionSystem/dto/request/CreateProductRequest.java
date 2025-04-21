package com.OlashAuctionSystem.dto.request;

import com.OlashAuctionSystem.data.models.ProductCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    @NotBlank(message = "ProductName is required!!")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @NotNull(message = "Starting price is required")
    @DecimalMin(value = "1000", message = "Must be at least ₦1,000")
    @Digits(integer = 12, fraction = 2, message = "Up to 2 decimal places allowed")
    private BigDecimal startingPrice;

    @NotNull(message = "Product image is required")
    private MultipartFile image;

}
