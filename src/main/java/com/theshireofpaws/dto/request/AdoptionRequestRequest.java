package com.theshireofpaws.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionRequestRequest {
    
    @NotBlank(message = "Requester name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String requesterName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String requesterEmail;
    
    @NotBlank(message = "Housing type is required")
    @Pattern(regexp = "House|Apartment|Other", message = "Housing type must be House, Apartment, or Other")
    private String housingType;
    
    @NotNull(message = "Household size is required")
    @Min(value = 1, message = "Household size must be at least 1")
    @Max(value = 8, message = "Household size cannot exceed 8")
    private Integer householdSize;
    
    @NotBlank(message = "Motivation is required")
    @Size(min = 50, max = 2000, message = "Motivation must be between 50 and 2000 characters")
    private String motivation;
    
    @Size(max = 1000, message = "Daytime location information cannot exceed 1000 characters")
    private String daytimeLocation;
    
    @NotNull(message = "Dog ID is required")
    private UUID dogId;
}