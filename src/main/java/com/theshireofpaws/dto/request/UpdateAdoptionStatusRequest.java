package com.theshireofpaws.dto.request;

import com.theshireofpaws.entity.enums.AdoptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdoptionStatusRequest {
    
    @NotNull(message = "Status is required")
    private AdoptionStatus status;
}







