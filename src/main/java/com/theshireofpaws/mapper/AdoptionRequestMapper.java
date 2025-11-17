package com.theshireofpaws.mapper;

import com.theshireofpaws.dto.request.AdoptionRequestRequest;
import com.theshireofpaws.dto.response.AdoptionRequestResponse;
import com.theshireofpaws.entity.AdoptionRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdoptionRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dog", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "requesterFirstName", target = "requesterFirstName")
    @Mapping(source = "requesterLastName", target = "requesterLastName")
    @Mapping(source = "requesterEmail", target = "requesterEmail")
    @Mapping(source = "housingType", target = "housingType")
    @Mapping(source = "householdSize", target = "householdSize")
    @Mapping(source = "motivation", target = "motivation")
    @Mapping(source = "daytimeLocation", target = "daytimeLocation")
    AdoptionRequest toEntity(AdoptionRequestRequest request);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "requesterFirstName", target = "requesterFirstName")
    @Mapping(source = "requesterLastName", target = "requesterLastName")
    @Mapping(source = "requesterEmail", target = "requesterEmail")
    @Mapping(source = "housingType", target = "housingType")
    @Mapping(source = "householdSize", target = "householdSize")
    @Mapping(source = "motivation", target = "motivation")
    @Mapping(source = "daytimeLocation", target = "daytimeLocation")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "dog.id", target = "dogId")
    @Mapping(source = "dog.name", target = "dogName")
    @Mapping(source = "dog.photoUrl", target = "dogPhotoUrl")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    AdoptionRequestResponse toResponse(AdoptionRequest request);
}