package com.theshireofpaws.config;

import com.theshireofpaws.entity.AdminUser;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.entity.enums.DogTrait;
import com.theshireofpaws.repository.AdminUserRepository;
import com.theshireofpaws.repository.DogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

import static com.theshireofpaws.entity.enums.DogTrait.*;

@Slf4j
@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@theshireofpaws.com";
    private static final String ADMIN_PASSWORD = "admin123";

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
    public void run(String... args) {
        seedAdmin();
        seedDogs();
    }

    private void seedAdmin() {
        if (adminUserRepository.count() > 0) {
            return;
        }
        adminUserRepository.save(AdminUser.builder()
            .email(ADMIN_EMAIL)
            .password(passwordEncoder.encode(ADMIN_PASSWORD))
            .build());
        log.info("Admin user created: {}", ADMIN_EMAIL);
    }

    private void seedDogs() {
        if (dogRepository.count() > 0) {
            return;
        }
        List<Dog> dogs = List.of(
            dog("Rover", "Rover is a friendly and energetic dog who loves to play fetch. He's great with kids and other dogs. Looking for an active family!",
                DogGender.MALE, 3, DogSize.MEDIUM, "photo-1587300003388-59208cc962cb", DogStatus.IN_PROCESS,
                EnumSet.of(ACTIVE, GOOD_WITH_KIDS, GOOD_WITH_DOGS)),
            dog("Moon", "Moon is a calm and gentle soul. She enjoys quiet walks and cuddling on the couch. Perfect for someone looking for a relaxed companion.",
                DogGender.FEMALE, 5, DogSize.LARGE, "photo-1583511655857-d19b40a7a54e", DogStatus.AVAILABLE,
                EnumSet.of(CALM, AFFECTIONATE)),
            dog("Kika", "Kika is a happy senior dog who still has lots of love to give. She's house-trained and would thrive in a peaceful home.",
                DogGender.FEMALE, 8, DogSize.SMALL, "photo-1561037404-61cd46aa615b", DogStatus.ADOPTED,
                EnumSet.of(CALM, AFFECTIONATE, HOUSE_TRAINED)),
            dog("Max", "Max is a playful puppy full of energy. He needs training and lots of exercise. Great for an experienced dog owner!",
                DogGender.MALE, 1, DogSize.LARGE, "photo-1543466835-00a7907e9de1", DogStatus.AVAILABLE,
                EnumSet.of(ACTIVE)),
            dog("Bella", "Bella is a sweet and affectionate dog. She loves attention and being around people. Would make a wonderful family pet.",
                DogGender.FEMALE, 4, DogSize.MEDIUM, "photo-1588943211346-0908a1fb0b01", DogStatus.AVAILABLE,
                EnumSet.of(AFFECTIONATE, GOOD_WITH_KIDS)),
            dog("Charlie", "Charlie is a loyal companion who loves outdoor adventures. He's well-behaved and great on a leash.",
                DogGender.MALE, 6, DogSize.LARGE, "photo-1537151608828-ea2b11777ee8", DogStatus.AVAILABLE,
                EnumSet.of(ACTIVE))
        );
        dogRepository.saveAll(dogs);
        log.info("{} sample dogs created", dogs.size());
    }

    private static Dog dog(String name, String story, DogGender gender, int age, DogSize size,
                           String unsplashId, DogStatus status, EnumSet<DogTrait> traits) {
        return Dog.builder()
            .name(name)
            .story(story)
            .gender(gender)
            .age(age)
            .size(size)
            .photoUrl("https://images.unsplash.com/" + unsplashId + "?w=400")
            .status(status)
            .traits(traits)
            .build();
    }
}
