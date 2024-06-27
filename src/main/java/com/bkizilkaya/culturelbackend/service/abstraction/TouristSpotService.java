package com.bkizilkaya.culturelbackend.service.abstraction;

import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.dto.spot.request.TouristSpotCreateDTO;
import com.bkizilkaya.culturelbackend.dto.spot.response.TouristSpotResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TouristSpotService {
    TouristSpotResponseDTO addSpot(TouristSpotCreateDTO touristSpotCreateDto);

    List<TouristSpotResponseDTO> getAllTouristicSpot();

    TouristSpotResponseDTO getSpotById(Long spotId);

    TouristSpotResponseDTO updateSpot(Long spotId, TouristSpotCreateDTO touristSpotCreateDto);

    void deleteSpot(Long spotId);

    void removeSpotImageFromSpot(Long spotId, Long imageId);

    Long addImageToSpot(Long spotId, MultipartFile file);

    Page<TouristSpotResponseDTO> findPaginated(int pageNo, int pageSize);

    Page<TouristSpotResponseDTO> searchSpotsPaginated(String title, int pageNo, int pageSize);
}
