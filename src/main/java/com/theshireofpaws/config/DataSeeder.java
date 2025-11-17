package com.theshireofpaws.config;

import com.theshireofpaws.entity.AdminUser;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.repository.AdminUserRepository;
import com.theshireofpaws.repository.DogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  
public class DataSeeder implements CommandLineRunner {
    
    private final AdminUserRepository adminUserRepository;
    private final DogRepository dogRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public DataSeeder(AdminUserRepository adminUserRepository,
                     DogRepository dogRepository,
                     BCryptPasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.dogRepository = dogRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Seed Admin Users
        if (adminUserRepository.count() == 0) {
            AdminUser mainAdmin = AdminUser.builder()
                .email("admin@theshireofpaws.com")
                .password(passwordEncoder.encode("admin123"))
                .build();
            
            AdminUser backupAdmin = AdminUser.builder()
                .email("backup@theshireofpaws.com")
                .password(passwordEncoder.encode("backup123"))
                .build();
            
            adminUserRepository.save(mainAdmin);
            adminUserRepository.save(backupAdmin);
            
            System.out.println("✅ 2 admin users created");
        }
        
        // Seed Dogs
        if (dogRepository.count() == 0) {
            Dog rover = Dog.builder()
                .name("Rover")
                .story("Rover is a friendly and energetic dog.")
                .gender("Male")
                .age(3)
                .size("Medium")
                .photoUrl("https://example.com/rover.jpg")
                .status(DogStatus.IN_PROCESS)
                .build();
            
            Dog moon = Dog.builder()
                .name("Moon")
                .story("Moon is a calm and gentle soul.")
                .gender("Female")
                .age(5)
                .size("Large")
                .photoUrl("https://example.com/moon.jpg")
                .status(DogStatus.AVAILABLE)
                .build();
            
            Dog kika = Dog.builder()
                .name("Kika")
                .story("Kika is a happy senior dog.")
                .gender("Female")
                .age(8)
                .size("Small")
                .photoUrl("https://example.com/kika.jpg")
                .status(DogStatus.ADOPTED)
                .build();
            
            dogRepository.save(rover);
            dogRepository.save(moon);
            dogRepository.save(kika);
            
            System.out.println("✅ Sample dogs created");
        }
    }
}