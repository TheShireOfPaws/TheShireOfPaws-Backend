package com.theshireofpaws.repository;

import com.theshireofpaws.entity.AdoptionRequest;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.AdoptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, UUID> {
    
    Page<AdoptionRequest> findByStatus(AdoptionStatus status, Pageable pageable);
    
    Page<AdoptionRequest> findByDog(Dog dog, Pageable pageable);
    
    List<AdoptionRequest> findByDogAndStatus(Dog dog, AdoptionStatus status);
    
    @Query("SELECT ar FROM AdoptionRequest ar WHERE " +
           "(:status IS NULL OR ar.status = :status) AND " +
           "(:dogId IS NULL OR ar.dog.id = :dogId) AND " +
           "(:requesterName IS NULL OR LOWER(ar.requesterName) LIKE LOWER(CONCAT('%', :requesterName, '%')))")
    Page<AdoptionRequest> findByFilters(
        @Param("status") AdoptionStatus status,
        @Param("dogId") UUID dogId,
        @Param("requesterName") String requesterName,
        Pageable pageable
    );
    
    long countByStatus(AdoptionStatus status);
    
    long countByDog(Dog dog);
}
