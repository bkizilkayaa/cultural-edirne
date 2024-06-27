package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.mapper.ArtworkMapper;
import com.bkizilkaya.culturelbackend.model.ActionEnum;
import com.bkizilkaya.culturelbackend.model.Artwork;
import com.bkizilkaya.culturelbackend.model.FileData;
import com.bkizilkaya.culturelbackend.model.TableNameEnum;
import com.bkizilkaya.culturelbackend.repo.ArtworkRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.ArtworkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkRepository artworkRepository;
    private final FileDataServiceImpl fileDataService;
    private final ActionLogServiceImpl actionLogService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ArtworkServiceImpl(ArtworkRepository artworkRepository, FileDataServiceImpl fileDataService, ActionLogServiceImpl actionLogService) {
        this.artworkRepository = artworkRepository;
        this.fileDataService = fileDataService;
        this.actionLogService = actionLogService;
    }

    @Override
    public List<ArtworkResponseDTO> getAllArtworks() {
        List<Artwork> allArtworks = artworkRepository.findAll();
        return allArtworks.stream().map(ArtworkMapper.INSTANCE::artworkToResponseDto).collect(Collectors.toList());
    }


    @Override
    public ArtworkResponseDTO addArtwork(ArtworkCreateDTO artworkCreateDTO) {
        if (artworkCreateDTO.getParentId() != null) {
            getArtworkById(artworkCreateDTO.getParentId());
        }

        Artwork artwork = ArtworkMapper.INSTANCE.dtoToEntity(artworkCreateDTO);
        artworkRepository.save(artwork);
        String mappedObject = getJsonObject(artwork);
        actionLogService.createLog(ActionEnum.ADD, TableNameEnum.ARTWORKS, mappedObject);
        return ArtworkMapper.INSTANCE.artworkToResponseDto(artwork);
    }

    @Override
    public ArtworkResponseDTO getArtworkGivenId(Long artworkId) {
        Artwork artworkById = getArtworkById(artworkId);
        return ArtworkMapper.INSTANCE.artworkToResponseDto(artworkById);
    }

    @Override
    @Transactional
    public ArtworkResponseDTO updateArtwork(Long oldArtworkId, ArtworkCreateDTO newArtworkDto) {
        Artwork artworkFromDb = getArtworkById(oldArtworkId);
        updateArtworkField(newArtworkDto, artworkFromDb);

        artworkRepository.save(artworkFromDb);
        String mappedObject = getJsonObject(artworkFromDb);
        actionLogService.createLog(ActionEnum.UPDATE, TableNameEnum.ARTWORKS, mappedObject);
        return ArtworkMapper.INSTANCE.artworkToResponseDto(artworkFromDb);
    }

    @Override
    public void deleteArtwork(Long id) {
        Artwork artworkFromDb = getArtworkById(id);
        artworkRepository.deleteById(id);
        String mappedObject = getJsonObject(artworkFromDb);
        actionLogService.createLog(ActionEnum.DELETE, TableNameEnum.ARTWORKS, mappedObject);
    }

    @Transactional
    public Long addImageToArtwork(Long artworkId, MultipartFile multipartFile) {
        try {
            Long fileId = fileDataService.saveFile(multipartFile);
            FileData fileData = fileDataService.findById(fileId);
            Artwork artwork = getArtworkById(artworkId);

            fileData.setArtworkImages(artwork);
            artwork.getFileData().add(fileData);

            String mappedObject = getJsonObject(artwork);
            actionLogService.createLog(ActionEnum.ADD, TableNameEnum.ARTWORK_IMAGES, mappedObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return artworkId;
    }

    @Override
    public Page<ArtworkResponseDTO> searchArtworksPaginated(String title, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Artwork> artworks = artworkRepository.findByTitleContainingIgnoreCase(title, pageable);
        List<ArtworkResponseDTO> artworkDTOs = artworks.stream()
                .map(ArtworkMapper.INSTANCE::entityToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(artworkDTOs, pageable, artworks.getTotalElements());
    }

    @Override
    @Transactional
    public void removeArtworkImageFromArtwork(Long artworkId, Long imageId) {
        Artwork artwork = getArtworkById(artworkId);
        FileData fileDataFromDb = fileDataService.findById(imageId);
        if (isArtworkHaveNoImageWithGivenImageId(imageId, artwork)) {
            throw new NotFoundException(FileData.class);
        } else {
            artwork.setFileData(artwork.getFileData()
                    .stream()
                    .filter(fileData -> !fileData.getId().equals(imageId))
                    .collect(Collectors.toList()));

            fileDataFromDb.setArtworkImages(null);
        }
        String mappedObject = getJsonObject(artwork);
        actionLogService.createLog(ActionEnum.DELETE, TableNameEnum.ARTWORK_IMAGES, mappedObject);
    }

    private boolean isArtworkHaveNoImageWithGivenImageId(Long imageId, Artwork artwork) {
        return artwork.getFileData().stream().noneMatch(fileData -> fileData.getId().equals(imageId));
    }

    @Override
    public Page<ArtworkResponseDTO> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.artworkRepository.findAll(pageable).map(ArtworkMapper.INSTANCE::artworkToResponseDto);
    }

    protected Artwork getArtworkById(Long artworkId) {
        return artworkRepository.findById(artworkId).orElseThrow(() -> new NotFoundException(Artwork.class));
    }

    private void updateArtworkField(ArtworkCreateDTO artworkCreateDTO, Artwork artworkFromDb) {
        artworkFromDb.setTitle(artworkCreateDTO.getTitle());
        artworkFromDb.setDescription(artworkCreateDTO.getDescription());
        artworkFromDb.setContent(artworkCreateDTO.getContent());
        artworkFromDb.setAuthorId(artworkCreateDTO.getAuthorId());
        artworkFromDb.setParentId(artworkCreateDTO.getParentId());
        artworkFromDb.setModifiedDate(LocalDateTime.now());
    }

    private String getJsonObject(Artwork artwork) {
        ArtworkResponseDTO artworkResponseDTO = ArtworkMapper.INSTANCE.entityToDto(artwork);
        String mappedObject;
        try {
            mappedObject = objectMapper.writeValueAsString(artworkResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mappedObject;
    }

}