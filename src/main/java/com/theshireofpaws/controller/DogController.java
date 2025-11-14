package com.theshireofpaws.controller;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.service.interfaces.DogService;
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
@RequestMapping("/api/dogs")
public class DogController {
    
    private final DogService dogService;
    
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }
    
    @GetMapping
    public ResponseEntity<Page<DogResponse>> getAllDogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), sort);
        return ResponseEntity.ok(dogService.getAllDogs(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DogResponse> getDogById(@PathVariable UUID id) {
        return ResponseEntity.ok(dogService.getDogById(id));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<DogResponse>> filterDogs(
            @RequestParam(required = false) DogStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int pageSize) {
        
        Pageable pageable = PageRequest.of(page, Math.min(pageSize, 50), 
                                          Sort.by("createdAt").descending());
        
        return ResponseEntity.ok(dogService.filterDogs(status, name, gender, size, pageable));
    }
    
    @PostMapping
    public ResponseEntity<DogResponse> createDog(@Valid @RequestBody DogRequest request) {
        DogResponse newDog = dogService.createDog(request);
        return new ResponseEntity<>(newDog, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DogResponse> updateDog(
            @PathVariable UUID id, 
            @Valid @RequestBody DogRequest request) {
        
        DogResponse updatedDog = dogService.updateDog(id, request);
        return ResponseEntity.ok(updatedDog);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable UUID id) {
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/count-by-status")
    public ResponseEntity<Long> countByStatus(@RequestParam DogStatus status) {
        return ResponseEntity.ok(dogService.countByStatus(status));
    }
}