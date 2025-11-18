package com.theshireofpaws.service.interfaces;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
import com.theshireofpaws.entity.enums.DogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DogService {
    
    Page<DogResponse> getAllDogs(Pageable pageable);
    
    DogResponse getDogById(UUID id);
    
    DogResponse createDog(DogRequest request);
    
    DogResponse updateDog(UUID id, DogRequest request);
    
    void deleteDog(UUID id);
    
    Page<DogResponse> filterDogs(DogStatus status, String name, DogGender gender, DogSize size, Pageable pageable);
    
    long countByStatus(DogStatus status);

    long count();
}