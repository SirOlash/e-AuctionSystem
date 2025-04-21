package com.OlashAuctionSystem.controllers;

import com.OlashAuctionSystem.data.models.Product;
import com.OlashAuctionSystem.dto.request.CreateProductRequest;
import com.OlashAuctionSystem.dto.response.CreateProductResponse;
import com.OlashAuctionSystem.services.productservices.ProductServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateProductResponse> addProduct(
            @RequestHeader("Authorization") String authHeader,
            @Valid @ModelAttribute CreateProductRequest productRequest){

        CreateProductResponse savedProduct = productServices.addProduct(authHeader, productRequest);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
