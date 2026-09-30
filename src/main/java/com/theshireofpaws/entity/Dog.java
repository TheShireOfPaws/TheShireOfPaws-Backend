package com.theshireofpaws.entity;

import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.entity.enums.DogTrait;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "dogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String story;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DogGender gender;  
    
    @Column(nullable = false)
    private Integer age;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DogSize size;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DogStatus status = DogStatus.AVAILABLE;
    
    @Column(name = "adopted_by")
    private String adoptedBy;
    
    @ElementCollection(targetClass = DogTrait.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "dog_traits", joinColumns = @JoinColumn(name = "dog_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "trait", nullable = false)
    @Builder.Default
    private Set<DogTrait> traits = new HashSet<>();
    
    // Photos shown after photoUrl (the main one), in order
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dog_photos", joinColumns = @JoinColumn(name = "dog_id"))
    @OrderColumn(name = "position")
    @Column(name = "url", nullable = false, length = 500)
    @Builder.Default
    private List<String> extraPhotoUrls = new ArrayList<>();
    
    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AdoptionRequest> adoptionRequests = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = DogStatus.AVAILABLE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}