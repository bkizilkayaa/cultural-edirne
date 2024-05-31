package com.bkizilkaya.culturelbackend.mapper;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.model.Artwork;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArtworkMapper {
    ArtworkMapper INSTANCE = Mappers.getMapper(ArtworkMapper.class);

    Artwork dtoToEntity(ArtworkCreateDTO artworkCreateDTO);
    ArtworkResponseDTO entityToDto(Artwork artwork);

    ArtworkResponseDTO createDtoToResponseDto(ArtworkCreateDTO artworkCreateDTO);

    ArtworkResponseDTO artworkToResponseDto(Artwork artwork);
}
