package com.OlashAuctionSystem.controllers;

import com.OlashAuctionSystem.data.models.Bid;
import com.OlashAuctionSystem.dto.request.CreateAuctionRequest;
import com.OlashAuctionSystem.dto.request.PlaceBidRequest;
import com.OlashAuctionSystem.dto.response.AuctionPageResponseDto;
import com.OlashAuctionSystem.dto.response.BidResponseDto;
import com.OlashAuctionSystem.dto.response.CreateAuctionResponse;
import com.OlashAuctionSystem.services.auctionservice.AuctionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/admin")
    public ResponseEntity<CreateAuctionResponse> startAuction(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateAuctionRequest request) {

        CreateAuctionResponse auctionResponse = auctionService.createAndStartAuction(authHeader, request);
        return new ResponseEntity<>(auctionResponse, HttpStatus.CREATED);

    }

    @GetMapping("/{id}/join")
    public AuctionPageResponseDto joinAuction(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String auctionId
    ){
        return auctionService.joinAuction(authHeader, auctionId);
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<BidResponseDto> placeBid(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String auctionId,
            @Valid @RequestBody PlaceBidRequest request){
        BidResponseDto bidResponse = auctionService.placeBid(authHeader, auctionId, request);
        return new ResponseEntity<>(bidResponse, HttpStatus.OK);
    }

}
