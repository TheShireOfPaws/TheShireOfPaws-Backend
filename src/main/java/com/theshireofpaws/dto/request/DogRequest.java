package com.theshireofpaws.dto.request;

import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.entity.enums.DogTrait;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 2000, message = "Story cannot exceed 2000 characters")
    private String story;
    
    @NotNull(message = "Gender is required")
    private DogGender gender;  
    
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 30, message = "Age cannot exceed 30 years")
    private Integer age;
    
    @NotNull(message = "Size is required")
    private DogSize size;  
    
    @Size(max = 500, message = "Photo URL cannot exceed 500 characters")
    private String photoUrl;
    
    private DogStatus status;
    
    // null = keep the current traits; empty = remove them all
    private Set<DogTrait> traits;
}