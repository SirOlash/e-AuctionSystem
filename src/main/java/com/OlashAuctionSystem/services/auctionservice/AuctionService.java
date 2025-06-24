package com.OlashAuctionSystem.services.auctionservice;

import com.OlashAuctionSystem.data.models.*;
import com.OlashAuctionSystem.data.repositories.AuctionRepository;
import com.OlashAuctionSystem.data.repositories.BidRepository;
import com.OlashAuctionSystem.data.repositories.ProductRepository;
import com.OlashAuctionSystem.data.repositories.UserRepository;
import com.OlashAuctionSystem.dto.request.CreateAuctionRequest;
import com.OlashAuctionSystem.dto.request.PlaceBidRequest;
import com.OlashAuctionSystem.dto.response.AuctionPageResponseDto;
import com.OlashAuctionSystem.dto.response.BidResponseDto;
import com.OlashAuctionSystem.dto.response.CreateAuctionResponse;
import com.OlashAuctionSystem.exceptions.*;
import com.OlashAuctionSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionService implements IAuctionActivities{

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private BidRepository bidRepository;

    public CreateAuctionResponse createAndStartAuction(String jwtToken, CreateAuctionRequest auctionRequest){
        String token;
        if (jwtToken.startsWith("Bearer")) {
            token = jwtToken.substring(7);
        } else {
            token = jwtToken;
        }
        String email = jwtUtil.extractSubject(token);
        Users admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
        if (admin.getRole() != Role.ADMIN) {
            throw new DenyAccessException("You do not have permission to start auction");
        }

        Product product = productRepository.findById(auctionRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setProductStatus(ProductStatus.LIVE_BIDDING);
        productRepository.save(product);

        LocalDateTime now = LocalDateTime.now();
        Auction auction = new Auction();
        auction.setProductId(product.getId());
        auction.setStartingPrice(product.getStartingPrice());
        auction.setCurrentPrice(product.getStartingPrice());
        auction.setHighestBidderId(null);
        auction.setStartTime(now);
        auction.setEndTime(now.plusMinutes(auctionRequest.getDurationMinutes()));
        auction.setStatus(AuctionStatus.RUNNING);

        Auction createdAuction = auctionRepository.save(auction);

        return CreateAuctionResponse.builder()
                .auctionId(createdAuction.getId())
                .productId(createdAuction.getProductId())
                .productUrl(product.getImageUrl())
                .startingPrice(createdAuction.getStartingPrice())
                .currentPrice(createdAuction.getCurrentPrice())
                .highestBidderId(null)
                .startTime(createdAuction.getStartTime())
                .endTime(createdAuction.getEndTime())
                .status(createdAuction.getStatus().name())
                .build();

    }

    private void endAuction(Auction auction, Product product){
        auction.setStatus(AuctionStatus.ENDED);
        auctionRepository.save(auction);

        if (auction.getStartingPrice().compareTo(auction.getCurrentPrice()) == 0) {
            product.setProductStatus(ProductStatus.PRODUCT_RETURNED);
        }
        else{
            product.setProductStatus(ProductStatus.AUCTIONED);
        }
        productRepository.save(product);
    }

    public AuctionPageResponseDto joinAuction(String jwtToken, String auctionId){
        String token;
        if (jwtToken.startsWith("Bearer")) {
            token = jwtToken.substring(7);
        } else {
            token = jwtToken;
        }
        String email = jwtUtil.extractSubject(token);
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

//        if (user.getRole() == Role.BUYER) {
//            throw new DenyAccessException("Only Buyers can join this Auction");
//        }

        Auction currentAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));
        Product product = productRepository.findById(currentAuction.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (LocalDateTime.now().isAfter(currentAuction.getEndTime())){
            endAuction(currentAuction, product);
            throw new AuctionClosedException("Sorry this Auction has ended");
        }

        List<Bid> allBids = bidRepository.findByAuctionIdOrderByAmountDesc(auctionId);
        int limit = Math.min(allBids.size(), 3);
        List<Bid> topBids = allBids.subList(0, limit);

        return AuctionPageResponseDto.builder()
                .auctionId(currentAuction.getId())
                .productId(product.getId())
                .currentPrice(currentAuction.getCurrentPrice())
                .topBids(topBids)
                .endTime(currentAuction.getEndTime())
                .build();
    }

    public BidResponseDto placeBid(String jwtToken, String auctionId, PlaceBidRequest bidRequest){
        String token;
        if (jwtToken.startsWith("Bearer")) {
            token = jwtToken.substring(7);
        } else {
            token = jwtToken;
        }
        String email = jwtUtil.extractSubject(token);
        Users bidder = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (bidder.getRole() != Role.BUYER) {
            throw new DenyAccessException("You do not have permission to start auction");
        }

        Auction currentAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));
        Product product = productRepository.findById(currentAuction.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        LocalDateTime now = LocalDateTime.now();
        if (currentAuction.getStatus() != AuctionStatus.RUNNING || now.isAfter(currentAuction.getEndTime())) {
            endAuction(currentAuction,product);
            throw new AuctionClosedException("Auction has ended");
        }

        if (bidRequest.getAmount().compareTo(currentAuction.getCurrentPrice()) <= 0){
            throw new InvalidBidException("Bid must exceed current price: " + currentAuction.getCurrentPrice());
        }

        Bid bid = new Bid();
        bid.setAuctionId(auctionId);
        bid.setBidderId(bidder.getId());
        bid.setAmount(bidRequest.getAmount());
        bid.setTimestamp(now);
        Bid savedBid = bidRepository.save(bid);
        currentAuction.setCurrentPrice(bidRequest.getAmount());
        currentAuction.setHighestBidderId(bidder.getId());
        auctionRepository.save(currentAuction);

        return BidResponseDto.builder()
                .auctionId(savedBid.getAuctionId())
                .bidderId(savedBid.getBidderId())
                .bidAmount(savedBid.getAmount())
                .timestamp(savedBid.getTimestamp())
                .build();

    }
}
