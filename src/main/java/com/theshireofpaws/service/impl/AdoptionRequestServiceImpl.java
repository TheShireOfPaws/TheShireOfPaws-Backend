package com.theshireofpaws.service.impl;

import com.theshireofpaws.dto.request.AdoptionRequestRequest;
import com.theshireofpaws.dto.response.AdoptionRequestResponse;
import com.theshireofpaws.entity.AdoptionRequest;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.AdoptionStatus;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.exception.BadRequestException;
import com.theshireofpaws.exception.ResourceNotFoundException;
import com.theshireofpaws.mapper.AdoptionRequestMapper;
import com.theshireofpaws.repository.AdoptionRequestRepository;
import com.theshireofpaws.repository.DogRepository;
import com.theshireofpaws.service.interfaces.AdoptionRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AdoptionRequestServiceImpl implements AdoptionRequestService {
    
    private final AdoptionRequestRepository requestRepository;
    private final DogRepository dogRepository;
    private final AdoptionRequestMapper requestMapper;
    
    public AdoptionRequestServiceImpl(AdoptionRequestRepository requestRepository,
                                     DogRepository dogRepository,
                                     AdoptionRequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.dogRepository = dogRepository;
        this.requestMapper = requestMapper;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequestResponse> getAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable)
            .map(requestMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AdoptionRequestResponse getRequestById(UUID id) {
        AdoptionRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption Request", "id", id));
        
        return requestMapper.toResponse(request);
    }
    
    @Override
    public AdoptionRequestResponse createRequest(AdoptionRequestRequest request) {
        Dog dog = dogRepository.findById(request.getDogId())
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", request.getDogId()));
        
        if (dog.getStatus() == DogStatus.ADOPTED) {
            throw new BadRequestException("This dog has already been adopted");
        }
        
        AdoptionRequest adoptionRequest = requestMapper.toEntity(request);
        adoptionRequest.setDog(dog);
        adoptionRequest.setStatus(AdoptionStatus.IN_PROCESS);
        
        if (dog.getStatus() == DogStatus.AVAILABLE) {
            dog.setStatus(DogStatus.IN_PROCESS);
            dogRepository.save(dog);
        }
        
        AdoptionRequest savedRequest = requestRepository.save(adoptionRequest);
        return requestMapper.toResponse(savedRequest);
    }
    
    @Override
    public AdoptionRequestResponse updateRequestStatus(UUID id, AdoptionStatus status) {
        AdoptionRequest request = requestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption Request", "id", id));
        
        AdoptionStatus oldStatus = request.getStatus();
        request.setStatus(status);
        
        Dog dog = request.getDog();
        
        if (status == AdoptionStatus.APPROVED) {
            dog.setStatus(DogStatus.ADOPTED);
            
            dog.setAdoptedBy(request.getRequesterName());
            
            requestRepository.findByDogAndStatus(dog, AdoptionStatus.IN_PROCESS)
                .stream()
                .filter(r -> !r.getId().equals(id))
                .forEach(r -> r.setStatus(AdoptionStatus.DENIED));
            
        } else if (status == AdoptionStatus.DENIED && oldStatus == AdoptionStatus.IN_PROCESS) {
            long pendingCount = requestRepository.findByDogAndStatus(dog, AdoptionStatus.IN_PROCESS)
                .stream()
                .filter(r -> !r.getId().equals(id))
                .count();
            
            if (pendingCount == 0) {
                dog.setStatus(DogStatus.AVAILABLE);
                dog.setAdoptedBy(null);  
            }
        }
        
        dogRepository.save(dog);
        AdoptionRequest updatedRequest = requestRepository.save(request);
        
        return requestMapper.toResponse(updatedRequest);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequestResponse> filterRequests(AdoptionStatus status, UUID dogId, 
                                                       String requesterName, Pageable pageable) {
        return requestRepository.findByFilters(status, dogId, requesterName, pageable)
            .map(requestMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequestResponse> getRequestsByDog(UUID dogId, Pageable pageable) {
        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", dogId));
        
        return requestRepository.findByDog(dog, pageable)
            .map(requestMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AdoptionStatus status) {
        return requestRepository.countByStatus(status);
    }
}