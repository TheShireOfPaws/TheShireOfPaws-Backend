package com.theshireofpaws.entity;

import com.theshireofpaws.entity.enums.AdoptionStatus;
import com.theshireofpaws.entity.enums.HousingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "adoption_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "requester_first_name", nullable = false)
    private String requesterFirstName;
    
    @Column(name = "requester_last_name", nullable = false)
    private String requesterLastName;
    
    @Column(name = "requester_email", nullable = false)
    private String requesterEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "housing_type", nullable = false)
    private HousingType housingType;
    
    @Column(name = "household_size", nullable = false)
    private Integer householdSize;
    
    @Column(columnDefinition = "TEXT")
    private String motivation;
    
    @Column(name = "daytime_location", columnDefinition = "TEXT")
    private String daytimeLocation;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AdoptionStatus status = AdoptionStatus.IN_PROCESS;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = AdoptionStatus.IN_PROCESS;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public String getFullName() {
        return requesterFirstName + " " + requesterLastName;
    }
}