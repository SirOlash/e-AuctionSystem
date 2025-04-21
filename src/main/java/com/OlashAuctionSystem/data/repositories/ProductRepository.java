package com.OlashAuctionSystem.data.repositories;

import com.OlashAuctionSystem.data.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}
