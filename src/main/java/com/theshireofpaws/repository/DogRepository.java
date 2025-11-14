package com.theshireofpaws.repository;

import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DogRepository extends JpaRepository<Dog, UUID> {
    
    Page<Dog> findByStatus(DogStatus status, Pageable pageable);
    
    Page<Dog> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT d FROM Dog d WHERE " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:gender IS NULL OR d.gender = :gender) AND " +
           "(:size IS NULL OR d.size = :size)")
    Page<Dog> findByFilters(
        @Param("status") DogStatus status,
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("size") String size,
        Pageable pageable
    );
    
    long countByStatus(DogStatus status);
}