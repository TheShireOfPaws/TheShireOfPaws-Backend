package com.theshireofpaws;

import com.theshireofpaws.dto.request.DogRequest;
import com.theshireofpaws.dto.response.DogResponse;
import com.theshireofpaws.entity.Dog;
import com.theshireofpaws.entity.enums.DogGender;
import com.theshireofpaws.entity.enums.DogSize;
import com.theshireofpaws.entity.enums.DogTrait;
import com.theshireofpaws.mapper.DogMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DogMapperTest {

    private final DogMapper dogMapper = Mappers.getMapper(DogMapper.class);

    private DogRequest.DogRequestBuilder baseRequest() {
        return DogRequest.builder()
            .name("Test Dog")
            .gender(DogGender.FEMALE)
            .age(4)
            .size(DogSize.SMALL);
    }

    private Dog dogWithTraits(DogTrait... traits) {
        return Dog.builder()
            .name("Test Dog")
            .gender(DogGender.FEMALE)
            .age(4)
            .size(DogSize.SMALL)
            .traits(EnumSet.of(traits[0], traits))
            .build();
    }

    @Test
    void toEntity_ShouldMapTraits() {
        DogRequest request = baseRequest()
            .traits(Set.of(DogTrait.CALM, DogTrait.GOOD_WITH_KIDS))
            .build();

        Dog dog = dogMapper.toEntity(request);

        assertEquals(Set.of(DogTrait.CALM, DogTrait.GOOD_WITH_KIDS), dog.getTraits());
    }

    @Test
    void toEntity_ShouldLeaveTraitsEmpty_WhenNotSent() {
        Dog dog = dogMapper.toEntity(baseRequest().build());

        assertNotNull(dog.getTraits());
        assertTrue(dog.getTraits().isEmpty());
    }

    @Test
    void updateFromRequest_ShouldKeepTraits_WhenTraitsAreNull() {
        Dog dog = dogWithTraits(DogTrait.ACTIVE);

        dogMapper.updateFromRequest(baseRequest().name("Renamed").build(), dog);

        assertEquals("Renamed", dog.getName());
        assertEquals(Set.of(DogTrait.ACTIVE), dog.getTraits());
    }

    @Test
    void updateFromRequest_ShouldReplaceTraits_WhenSent() {
        Dog dog = dogWithTraits(DogTrait.ACTIVE, DogTrait.GOOD_WITH_DOGS);

        dogMapper.updateFromRequest(baseRequest().traits(Set.of(DogTrait.CALM)).build(), dog);

        assertEquals(Set.of(DogTrait.CALM), dog.getTraits());
    }

    @Test
    void updateFromRequest_ShouldClearTraits_WhenEmptySetSent() {
        Dog dog = dogWithTraits(DogTrait.ACTIVE);

        dogMapper.updateFromRequest(baseRequest().traits(Set.of()).build(), dog);

        assertTrue(dog.getTraits().isEmpty());
    }

    @Test
    void toResponse_ShouldReturnTraitsInDeclarationOrder() {
        Dog dog = dogWithTraits(DogTrait.HOUSE_TRAINED, DogTrait.ACTIVE, DogTrait.CALM);

        DogResponse response = dogMapper.toResponse(dog);

        assertEquals(List.of(DogTrait.ACTIVE, DogTrait.CALM, DogTrait.HOUSE_TRAINED), response.getTraits());
    }

    @Test
    void toEntity_ShouldMapExtraPhotosInOrder() {
        DogRequest request = baseRequest()
            .extraPhotoUrls(List.of("https://img/2.jpg", "https://img/3.jpg"))
            .build();
        
        Dog dog = dogMapper.toEntity(request);
        
        assertEquals(List.of("https://img/2.jpg", "https://img/3.jpg"), dog.getExtraPhotoUrls());
    }
    
    @Test
    void updateFromRequest_ShouldKeepExtraPhotos_WhenNull() {
        Dog dog = dogWithTraits(DogTrait.CALM);
        dog.getExtraPhotoUrls().add("https://img/2.jpg");
        
        dogMapper.updateFromRequest(baseRequest().build(), dog);
        
        assertEquals(List.of("https://img/2.jpg"), dog.getExtraPhotoUrls());
    }
    
    @Test
    void updateFromRequest_ShouldReplaceExtraPhotos_WhenSent() {
        Dog dog = dogWithTraits(DogTrait.CALM);
        dog.getExtraPhotoUrls().add("https://img/old.jpg");
        
        dogMapper.updateFromRequest(baseRequest().extraPhotoUrls(List.of()).build(), dog);
        
        assertTrue(dog.getExtraPhotoUrls().isEmpty());
    }
}
