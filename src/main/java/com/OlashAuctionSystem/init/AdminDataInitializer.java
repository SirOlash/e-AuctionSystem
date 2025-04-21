package com.OlashAuctionSystem.init;

import com.OlashAuctionSystem.data.models.Role;
import com.OlashAuctionSystem.data.models.Users;
import com.OlashAuctionSystem.data.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AdminDataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminDataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@gmail.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Users admin = new Users();
            admin.setEmail(adminEmail);
            admin.setUserName("Admin");
            admin.setPassword(passwordEncoder.encode("Admin1234"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            log.info("✅ Admin created: {}", adminEmail);
        }
        else {
            log.info("Admin already exists: {}", adminEmail);
        }
    }
}
