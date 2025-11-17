package com.theshireofpaws.service.impl;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.exception.ResourceNotFoundException;
import com.theshireofpaws.mapper.DogMapper;
import com.theshireofpaws.repository.DogRepository;
import com.theshireofpaws.service.interfaces.DogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DogServiceImpl implements DogService {
    
    private final DogRepository dogRepository;
    private final DogMapper dogMapper;
    
    public DogServiceImpl(DogRepository dogRepository, DogMapper dogMapper) {
        this.dogRepository = dogRepository;
        this.dogMapper = dogMapper;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DogResponse> getAllDogs(Pageable pageable) {
        return dogRepository.findAll(pageable)
            .map(dogMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DogResponse getDogById(UUID id) {
        Dog dog = dogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", id));
        
        return dogMapper.toResponse(dog);
    }
    
    @Override
    public DogResponse createDog(DogRequest request) {
        Dog dog = dogMapper.toEntity(request);
        
        if (dog.getStatus() == null) {
            dog.setStatus(DogStatus.AVAILABLE);
        }
        
        Dog savedDog = dogRepository.save(dog);
        return dogMapper.toResponse(savedDog);
    }
    
    @Override
    public DogResponse updateDog(UUID id, DogRequest request) {
        Dog dog = dogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", id));
        
        dogMapper.updateFromRequest(request, dog);
        
        Dog updatedDog = dogRepository.save(dog);
        return dogMapper.toResponse(updatedDog);
    }
    
    @Override
    public void deleteDog(UUID id) {
        Dog dog = dogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", id));
        
        dogRepository.delete(dog);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DogResponse> filterDogs(DogStatus status, String name, String gender, String size, Pageable pageable) {
        return dogRepository.findByFilters(status, name, gender, size, pageable)
            .map(dogMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(DogStatus status) {
        return dogRepository.countByStatus(status);
    }
}
