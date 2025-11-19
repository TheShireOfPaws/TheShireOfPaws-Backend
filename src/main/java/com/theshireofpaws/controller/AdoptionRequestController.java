package com.theshireofpaws.controller;

import com.theshireofpaws.dto.request.AdoptionRequestRequest;
import com.theshireofpaws.dto.request.UpdateAdoptionStatusRequest;
import com.theshireofpaws.dto.response.AdoptionRequestResponse;
import com.theshireofpaws.entity.enums.AdoptionStatus;
import com.theshireofpaws.service.interfaces.AdoptionRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/adoption-requests")
public class AdoptionRequestController {
    
    private final AdoptionRequestService requestService;
    
    public AdoptionRequestController(AdoptionRequestService requestService) {
        this.requestService = requestService;
    }
    
    @GetMapping
    public ResponseEntity<Page<AdoptionRequestResponse>> getAllRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), sort);
        return ResponseEntity.ok(requestService.getAllRequests(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionRequestResponse> getRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<AdoptionRequestResponse>> filterRequests(
            @RequestParam(required = false) AdoptionStatus status,
            @RequestParam(required = false) UUID dogId,
            @RequestParam(required = false) String requesterName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), 
                                          Sort.by("createdAt").descending());
        
        return ResponseEntity.ok(requestService.filterRequests(status, dogId, requesterName, pageable));
    }
    
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<Page<AdoptionRequestResponse>> getRequestsByDog(
            @PathVariable UUID dogId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), 
                                          Sort.by("createdAt").descending());
        
        return ResponseEntity.ok(requestService.getRequestsByDog(dogId, pageable));
    }
    
    @PostMapping
    public ResponseEntity<AdoptionRequestResponse> createRequest(
            @Valid @RequestBody AdoptionRequestRequest request) {
        AdoptionRequestResponse newRequest = requestService.createRequest(request);
        return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<AdoptionRequestResponse> updateRequestStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAdoptionStatusRequest request) {
        
        AdoptionRequestResponse updatedRequest = requestService.updateRequestStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedRequest);
    }
    
    @GetMapping("/stats/count-by-status")
    public ResponseEntity<Long> countByStatus(@RequestParam AdoptionStatus status) {
        return ResponseEntity.ok(requestService.countByStatus(status));
    }
}