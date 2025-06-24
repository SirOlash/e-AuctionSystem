package com.OlashAuctionSystem.data.repositories;

import com.OlashAuctionSystem.data.models.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BidRepository extends MongoRepository<Bid, String> {

    List<Bid> findByAuctionIdOrderByAmountDesc(String auctionId);
}
