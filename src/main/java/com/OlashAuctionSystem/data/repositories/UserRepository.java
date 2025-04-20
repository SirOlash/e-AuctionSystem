package com.OlashAuctionSystem.data.repositories;

import com.OlashAuctionSystem.data.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByNIN(String NIN);
    Optional<Users> findByUserName(String userName);
}
