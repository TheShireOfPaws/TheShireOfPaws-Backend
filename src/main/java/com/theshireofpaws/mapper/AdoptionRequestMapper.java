package com.theshireofpaws.mapper;

import com.theshireofpaws.dto.request.AdoptionRequestRequest;
import com.theshireofpaws.dto.response.AdoptionRequestResponse;
import com.theshireofpaws.entity.AdoptionRequest;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AdoptionRequestMapper {
    
    @Mapping(source = "dog.id", target = "dogId")
    @Mapping(source = "dog.name", target = "dogName")
    @Mapping(source = "dog.photoUrl", target = "dogPhotoUrl")
    AdoptionRequestResponse toResponse(AdoptionRequest adoptionRequest);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dog", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AdoptionRequest toEntity(AdoptionRequestRequest request);
}
