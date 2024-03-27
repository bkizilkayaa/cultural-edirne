package com.bkizilkaya.culturelbackend.service.abstraction;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArtworkService {
    ArtworkResponseDTO addArtwork(ArtworkCreateDTO artworkCreateDTO);

    List<ArtworkResponseDTO> getAllArtworks();

    ArtworkResponseDTO getArtworkGivenId(Long artworkId);

    ArtworkResponseDTO updateArtwork(Long id, ArtworkCreateDTO artworkCreateDTO);

    void deleteArtwork(Long id);

    void removeArtworkImageFromArtwork(Long artworkId, Long imageId);

    Page<ArtworkResponseDTO> findPaginated(int pageNo, int pageSize);

}
