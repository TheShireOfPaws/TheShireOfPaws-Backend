package com.theshireofpaws;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogStatus;
import com.theshireofpaws.exception.ResourceNotFoundException;
import com.theshireofpaws.mapper.DogMapper;
import com.theshireofpaws.repository.DogRepository;
import com.theshireofpaws.service.impl.DogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogServiceTest {
    
    @Mock
    private DogRepository dogRepository;
    
    @Mock
    private DogMapper dogMapper;
    
    @InjectMocks
    private DogServiceImpl dogService;
    
    private Dog testDog;
    private DogRequest dogRequest;
    private DogResponse dogResponse;
    private UUID testId;
    
    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        testDog = Dog.builder()
            .id(testId)
            .name("Test Dog")
            .story("Test story")
            .gender("Male")
            .age(3)
            .size("Medium")
            .photoUrl("test-url")
            .status(DogStatus.AVAILABLE)
            .build();
        
        dogRequest = DogRequest.builder()
            .name("Test Dog")
            .story("Test story")
            .gender("Male")
            .age(3)
            .size("Medium")
            .photoUrl("test-url")
            .build();
        
        dogResponse = DogResponse.builder()
            .id(testId)
            .name("Test Dog")
            .story("Test story")
            .gender("Male")
            .age(3)
            .size("Medium")
            .photoUrl("test-url")
            .status(DogStatus.AVAILABLE)
            .build();
    }
    
    @Test
    void getDogById_ShouldReturnDog_WhenDogExists() {
        // Arrange
        when(dogRepository.findById(testId)).thenReturn(Optional.of(testDog));
        when(dogMapper.toResponse(testDog)).thenReturn(dogResponse);
        
        // Act
        DogResponse result = dogService.getDogById(testId);
        
        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals("Test Dog", result.getName());
        verify(dogRepository, times(1)).findById(testId);
        verify(dogMapper, times(1)).toResponse(testDog);
    }
    
    @Test
    void getDogById_ShouldThrowException_WhenDogNotFound() {
        // Arrange
        when(dogRepository.findById(testId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> dogService.getDogById(testId));
        verify(dogRepository, times(1)).findById(testId);
        verify(dogMapper, never()).toResponse(any());
    }
    
    @Test
    void createDog_ShouldReturnCreatedDog() {
        // Arrange
        when(dogMapper.toEntity(dogRequest)).thenReturn(testDog);
        when(dogRepository.save(any(Dog.class))).thenReturn(testDog);
        when(dogMapper.toResponse(testDog)).thenReturn(dogResponse);
        
        // Act
        DogResponse result = dogService.createDog(dogRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Dog", result.getName());
        verify(dogRepository, times(1)).save(any(Dog.class));
    }
    
    @Test
    void updateDog_ShouldUpdateAndReturnDog_WhenDogExists() {
        // Arrange
        when(dogRepository.findById(testId)).thenReturn(Optional.of(testDog));
        when(dogRepository.save(testDog)).thenReturn(testDog);
        when(dogMapper.toResponse(testDog)).thenReturn(dogResponse);
        
        // Act
        DogResponse result = dogService.updateDog(testId, dogRequest);
        
        // Assert
        assertNotNull(result);
        verify(dogMapper, times(1)).updateFromRequest(dogRequest, testDog);
        verify(dogRepository, times(1)).save(testDog);
    }
    
    @Test
    void deleteDog_ShouldDeleteDog_WhenDogExists() {
        // Arrange
        when(dogRepository.findById(testId)).thenReturn(Optional.of(testDog));
        doNothing().when(dogRepository).delete(testDog);
        
        // Act
        dogService.deleteDog(testId);
        
        // Assert
        verify(dogRepository, times(1)).findById(testId);
        verify(dogRepository, times(1)).delete(testDog);
    }
    
    @Test
    void deleteDog_ShouldThrowException_WhenDogNotFound() {
        // Arrange
        when(dogRepository.findById(testId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> dogService.deleteDog(testId));
        verify(dogRepository, never()).delete(any());
    }
}
