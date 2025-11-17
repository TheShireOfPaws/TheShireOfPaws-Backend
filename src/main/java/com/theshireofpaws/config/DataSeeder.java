package com.theshireofpaws.config;

import com.theshireofpaws.entity.AdminUser;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogGender;  
import com.theshireofpaws.entity.enums.DogSize;    
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
        // Seed Admin User
        if (adminUserRepository.count() == 0) {
            AdminUser admin = AdminUser.builder()
                .email("admin@theshireofpaws.com")
                .password(passwordEncoder.encode("admin123"))
                .build();
            
            adminUserRepository.save(admin);
            System.out.println("Admin user created: admin@theshireofpaws.com / admin123");
        }
        
        // Seed Dogs
        if (dogRepository.count() == 0) {
            Dog rover = Dog.builder()
                .name("Rover")
                .story("Rover is a friendly and energetic dog who loves to play fetch. He's great with kids and other dogs. Looking for an active family!")
                .gender(DogGender.MALE)        
                .age(3)
                .size(DogSize.MEDIUM)         
                .photoUrl("https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400")
                .status(DogStatus.IN_PROCESS)
                .build();
            
            Dog moon = Dog.builder()
                .name("Moon")
                .story("Moon is a calm and gentle soul. She enjoys quiet walks and cuddling on the couch. Perfect for someone looking for a relaxed companion.")
                .gender(DogGender.FEMALE)      
                .age(5)
                .size(DogSize.LARGE)           
                .photoUrl("https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=400")
                .status(DogStatus.AVAILABLE)
                .build();
            
            Dog kika = Dog.builder()
                .name("Kika")
                .story("Kika is a happy senior dog who still has lots of love to give. She's house-trained and would thrive in a peaceful home.")
                .gender(DogGender.FEMALE)      
                .age(8)
                .size(DogSize.SMALL)           
                .photoUrl("https://images.unsplash.com/photo-1561037404-61cd46aa615b?w=400")
                .status(DogStatus.ADOPTED)
                .build();
            
            Dog max = Dog.builder()
                .name("Max")
                .story("Max is a playful puppy full of energy. He needs training and lots of exercise. Great for an experienced dog owner!")
                .gender(DogGender.MALE)        
                .age(1)
                .size(DogSize.LARGE)          
                .photoUrl("https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=400")
                .status(DogStatus.AVAILABLE)
                .build();
            
            Dog bella = Dog.builder()
                .name("Bella")
                .story("Bella is a sweet and affectionate dog. She loves attention and being around people. Would make a wonderful family pet.")
                .gender(DogGender.FEMALE)     
                .age(4)
                .size(DogSize.MEDIUM)         
                .photoUrl("https://images.unsplash.com/photo-1588943211346-0908a1fb0b01?w=400")
                .status(DogStatus.AVAILABLE)
                .build();
            
            Dog charlie = Dog.builder()
                .name("Charlie")
                .story("Charlie is a loyal companion who loves outdoor adventures. He's well-behaved and great on a leash.")
                .gender(DogGender.MALE)        
                .age(6)
                .size(DogSize.LARGE)          
                .photoUrl("https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?w=400")
                .status(DogStatus.AVAILABLE)
                .build();
            
            dogRepository.save(rover);
            dogRepository.save(moon);
            dogRepository.save(kika);
            dogRepository.save(max);
            dogRepository.save(bella);
            dogRepository.save(charlie);
            
            System.out.println("6 sample dogs created successfully");
        }
    }
}