package com.theshireofpaws.controller;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.service.interfaces.DogService;
import com.theshireofpaws.util.PageRequests;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
        return ResponseEntity.ok(dogService.getAllDogs(PageRequests.of(page, size, sortBy, sortDir)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogResponse> getDogById(@PathVariable UUID id) {
        return ResponseEntity.ok(dogService.getDogById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<DogResponse>> filterDogs(
            @RequestParam(required = false) DogStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) DogGender gender,
            @RequestParam(required = false) DogSize size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int pageSize) {
        return ResponseEntity.ok(
            dogService.filterDogs(status, name, gender, size, PageRequests.newestFirst(page, pageSize)));
    }

    @PostMapping
    public ResponseEntity<DogResponse> createDog(@Valid @RequestBody DogRequest request) {
        return new ResponseEntity<>(dogService.createDog(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DogResponse> updateDog(@PathVariable UUID id, @Valid @RequestBody DogRequest request) {
        return ResponseEntity.ok(dogService.updateDog(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable UUID id) {
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(Map.of(
            "rescued", dogService.count(),
            "adopted", dogService.countByStatus(DogStatus.ADOPTED),
            "available", dogService.countByStatus(DogStatus.AVAILABLE)
        ));
    }

    @GetMapping("/stats/count-by-status")
    public ResponseEntity<Long> countByStatus(@RequestParam DogStatus status) {
        return ResponseEntity.ok(dogService.countByStatus(status));
    }
}
