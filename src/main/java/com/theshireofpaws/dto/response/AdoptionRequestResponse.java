package com.theshireofpaws.dto.response;

import com.theshireofpaws.entity.enums.AdoptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionRequestResponse {
    
    private UUID id;
    private String requesterName;
    private String requesterEmail;
    private String housingType;
    private Integer householdSize;
    private String motivation;
    private String daytimeLocation;
    private AdoptionStatus status;
    

    private UUID dogId;
    private String dogName;
    private String dogPhotoUrl;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}






