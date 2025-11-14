package com.theshireofpaws.mapper;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.Dog;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DogMapper {
    
    @Mapping(target = "adoptionRequestsCount", expression = "java(dog.getAdoptionRequests() != null ? dog.getAdoptionRequests().size() : 0)")
    DogResponse toResponse(Dog dog);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adoptionRequests", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Dog toEntity(DogRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "adoptionRequests", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(DogRequest request, @MappingTarget Dog dog);
}
