package com.bkizilkaya.culturelbackend.utils;

import com.bkizilkaya.culturelbackend.dto.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.model.Artwork;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GenericUtil {
    public static Artwork artworkMapper(ArtworkCreateDTO artworkCreateDTO){
        Artwork artwork = new Artwork();
        artwork.setContent(artworkCreateDTO.getContent());
        artwork.setId(artworkCreateDTO.getId());
        artwork.setTitle(artworkCreateDTO.getTitle());
        artwork.setCreateDate(LocalDateTime.now());
        artwork.setParentId(artworkCreateDTO.getParentId());
        artwork.setAuthorId(artworkCreateDTO.getAuthorId());
        artwork.setImages(artworkCreateDTO.getImages());

        return artwork;
    }
    public static ArtworkCreateDTO artworkMapperForCreate(Artwork artwork){
        return ArtworkCreateDTO.builder()
                .Id(artwork.getId())
                .createDate(LocalDateTime.now())
                .authorId(artwork.getAuthorId())
                .content(artwork.getContent())

                .title(artwork.getTitle())
                .images(artwork.getImages())
                .build();
    }
    public static ArtworkResponseDTO artworkMapperForResponse(Artwork artwork){
        return ArtworkResponseDTO.builder()
                .Id(artwork.getId())
                .authorId(artwork.getAuthorId())
                .images(artwork.getImages())
                .content(artwork.getContent())
                .createDate(LocalDateTime.now())
                .title(artwork.getTitle())
                .build();
    }
    public static ArtworkResponseDTO artworkMapperForResponse(ArtworkCreateDTO artworkCreateDTO){
        return ArtworkResponseDTO.builder()
                .Id(artworkCreateDTO.getId())
                .createDate(artworkCreateDTO.getCreateDate())
                .authorId(artworkCreateDTO.getAuthorId())
                .content(artworkCreateDTO.getContent())
                .images(artworkCreateDTO.getImages())
                .title(artworkCreateDTO.getTitle())
                .build();
    }
}
