package com.theshireofpaws.dto.response;

import com.theshireofpaws.entity.enums.DogStatus;
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
public class DogResponse {
    
    private UUID id;
    private String name;
    private String story;
    private String gender;
    private Integer age;
    private String size;
    private String photoUrl;
    private DogStatus status;
    private Integer adoptionRequestsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}






