package com.theshireofpaws.service.interfaces;

import com.theshireofpaws.dto.request.AdoptionRequestRequest;
import com.theshireofpaws.dto.response.AdoptionRequestResponse;
import com.theshireofpaws.entity.enums.AdoptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdoptionRequestService {
    
    Page<AdoptionRequestResponse> getAllRequests(Pageable pageable);
    
    AdoptionRequestResponse getRequestById(UUID id);
    
    AdoptionRequestResponse createRequest(AdoptionRequestRequest request);
    
    AdoptionRequestResponse updateRequestStatus(UUID id, AdoptionStatus status);
    
    Page<AdoptionRequestResponse> filterRequests(AdoptionStatus status, UUID dogId, String requesterName, Pageable pageable);
    
    Page<AdoptionRequestResponse> getRequestsByDog(UUID dogId, Pageable pageable);
    
    long countByStatus(AdoptionStatus status);
}