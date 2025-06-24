package com.OlashAuctionSystem.services.productservices;

import com.OlashAuctionSystem.data.models.Product;
import com.OlashAuctionSystem.data.models.Role;
import com.OlashAuctionSystem.data.models.ProductStatus;
import com.OlashAuctionSystem.data.models.Users;
import com.OlashAuctionSystem.data.repositories.ProductRepository;
import com.OlashAuctionSystem.data.repositories.UserRepository;
import com.OlashAuctionSystem.dto.request.CreateProductRequest;
import com.OlashAuctionSystem.dto.response.CreateProductResponse;
import com.OlashAuctionSystem.exceptions.DenyAccessException;
import com.OlashAuctionSystem.exceptions.UserNotFoundException;
import com.OlashAuctionSystem.services.filestorageservice.FileStorageService;
import com.OlashAuctionSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServices implements IProductActivities{
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorage;

    @Override
    public CreateProductResponse addProduct(String jwtToken, CreateProductRequest productRequest){
        String token = jwtToken.startsWith("Bearer") ? jwtToken.substring(7) : jwtToken;
        String email = jwtUtil.extractSubject(token);
        Users seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Seller Not Found"));
        if (seller.getRole() != Role.SELLER) {
            throw new DenyAccessException("Only sellers can post products");
        }

        String imageUrl = fileStorage.store(productRequest.getImage());

        Product product = new Product();
        product.setName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setStartingPrice(productRequest.getStartingPrice());
        product.setImageUrl(imageUrl);
        product.setProductStatus(ProductStatus.PENDING);

        Product savedProduct = productRepository.save(product);

        return CreateProductResponse.builder()
                .productId(savedProduct.getId())
                .productName(savedProduct.getName())
                .productDescription(savedProduct.getDescription())
                .productCategory(savedProduct.getCategory())
                .startingPrice(savedProduct.getStartingPrice())
                .imageUrl(imageUrl)
                .productStatus(savedProduct.getProductStatus())
                .message("You have Successfully added a product")
                .build();
    }
}
