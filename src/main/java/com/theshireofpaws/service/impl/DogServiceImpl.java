package com.theshireofpaws.service.impl;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
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
        return dogMapper.toResponse(findDog(id));
    }

    @Override
    public DogResponse createDog(DogRequest request) {
        Dog dog = dogMapper.toEntity(request);

        if (dog.getStatus() == null) {
            dog.setStatus(DogStatus.AVAILABLE);
        }

        return dogMapper.toResponse(dogRepository.save(dog));
    }

    @Override
    public DogResponse updateDog(UUID id, DogRequest request) {
        Dog dog = findDog(id);
        dogMapper.updateFromRequest(request, dog);
        return dogMapper.toResponse(dogRepository.save(dog));
    }

    @Override
    public void deleteDog(UUID id) {
        dogRepository.delete(findDog(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DogResponse> filterDogs(DogStatus status, String name, DogGender gender, DogSize size, Pageable pageable) {
        return dogRepository.findByFilters(status, name, gender, size, pageable)
            .map(dogMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(DogStatus status) {
        return dogRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return dogRepository.count();
    }

    private Dog findDog(UUID id) {
        return dogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", id));
    }
}
