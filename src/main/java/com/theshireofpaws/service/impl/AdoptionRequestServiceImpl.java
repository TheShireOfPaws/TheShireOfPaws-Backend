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

import java.util.List;
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
        return requestMapper.toResponse(findRequest(id));
    }

    @Override
    public AdoptionRequestResponse createRequest(AdoptionRequestRequest request) {
        Dog dog = findDog(request.getDogId());

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

        return requestMapper.toResponse(requestRepository.save(adoptionRequest));
    }

    @Override
    public AdoptionRequestResponse updateRequestStatus(UUID id, AdoptionStatus status) {
        AdoptionRequest request = findRequest(id);
        AdoptionStatus oldStatus = request.getStatus();
        request.setStatus(status);

        Dog dog = request.getDog();

        if (status == AdoptionStatus.APPROVED) {
            dog.setStatus(DogStatus.ADOPTED);
            dog.setAdoptedBy(request.getFullName());

            otherPendingRequests(dog, id).forEach(r -> r.setStatus(AdoptionStatus.DENIED));
        } else if (status == AdoptionStatus.DENIED && oldStatus == AdoptionStatus.IN_PROCESS) {
            if (otherPendingRequests(dog, id).isEmpty()) {
                dog.setStatus(DogStatus.AVAILABLE);
                dog.setAdoptedBy(null);
            }
        }

        dogRepository.save(dog);
        return requestMapper.toResponse(requestRepository.save(request));
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
        return requestRepository.findByDog(findDog(dogId), pageable)
            .map(requestMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AdoptionStatus status) {
        return requestRepository.countByStatus(status);
    }

    private AdoptionRequest findRequest(UUID id) {
        return requestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption Request", "id", id));
    }

    private Dog findDog(UUID id) {
        return dogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dog", "id", id));
    }

    private List<AdoptionRequest> otherPendingRequests(Dog dog, UUID excludedId) {
        return requestRepository.findByDogAndStatus(dog, AdoptionStatus.IN_PROCESS).stream()
            .filter(r -> !r.getId().equals(excludedId))
            .toList();
    }
}
