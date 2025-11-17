package com.theshireofpaws.config;

import com.theshireofpaws.entity.AdminUser;
import com.theshireofpaws.repository.AdminUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@TestConfiguration
public class TestDataLoader {
    
    private final AdminUserRepository adminUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public TestDataLoader(AdminUserRepository adminUserRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostConstruct
    public void loadData() {
        if (adminUserRepository.count() == 0) {
            AdminUser testAdmin = AdminUser.builder()
                .email("admin@theshireofpaws.com")
                .password(passwordEncoder.encode("admin123"))
                .build();
            
            adminUserRepository.save(testAdmin);
            System.out.println(" Test admin user created");
        }
    }
}