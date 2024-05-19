package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.mapper.ArtworkMapper;
import com.bkizilkaya.culturelbackend.model.ActionEnum;
import com.bkizilkaya.culturelbackend.model.Artwork;
import com.bkizilkaya.culturelbackend.model.FileData;
import com.bkizilkaya.culturelbackend.model.TableName;
import com.bkizilkaya.culturelbackend.repo.ArtworkRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.ArtworkService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkRepository artworkRepository;
    private final FileDataServiceImpl fileDataService;
    private final ActionLogServiceImpl actionLogService;


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
        actionLogService.createLog(ActionEnum.ADD, TableName.ARTWORKS);
        return ArtworkMapper.INSTANCE.artworkToResponseDto(artwork);
    }

    @Override
    public ArtworkResponseDTO getArtworkGivenId(Long artworkId) {
        Artwork artworkById = getArtworkById(artworkId);
        return ArtworkMapper.INSTANCE.artworkToResponseDto(artworkById);
    }

    @Override
    public ArtworkResponseDTO updateArtwork(Long id, ArtworkCreateDTO artworkCreateDTO) {
        Artwork artworkFromDb = getArtworkById(id);
        updateArtworkField(artworkCreateDTO, artworkFromDb);

        artworkRepository.save(artworkFromDb);
        actionLogService.createLog(ActionEnum.UPDATE, TableName.ARTWORKS);
        return ArtworkMapper.INSTANCE.artworkToResponseDto(artworkFromDb);
    }

    @Override
    public void deleteArtwork(Long id) {
        Artwork artworkFromDb = getArtworkById(id);
        artworkRepository.deleteById(id);
        actionLogService.createLog(ActionEnum.DELETE, TableName.ARTWORKS);
    }

    @Transactional
    public Long addImageToArtwork(Long artworkId, MultipartFile multipartFile) {
        try {
            Long fileId = fileDataService.saveFile(multipartFile);
            FileData fileData = fileDataService.findById(fileId);
            Artwork artwork = getArtworkById(artworkId);

            fileData.setArtworkImages(artwork);
            artwork.getFileData().add(fileData);
            actionLogService.createLog(ActionEnum.ADD, TableName.ARTWORK_IMAGES);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return artworkId;
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
        actionLogService.createLog(ActionEnum.DELETE, TableName.ARTWORK_IMAGES);
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
        Artwork artworkFromDb = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new NotFoundException(Artwork.class));
        actionLogService.createLog(ActionEnum.GET, TableName.ARTWORKS);
        return artworkFromDb;
    }

    private void updateArtworkField(ArtworkCreateDTO artworkCreateDTO, Artwork artworkFromDb) {
        artworkFromDb.setTitle(artworkCreateDTO.getTitle());
        artworkFromDb.setDescription(artworkCreateDTO.getDescription());
        artworkFromDb.setContent(artworkCreateDTO.getContent());
        artworkFromDb.setAuthorId(artworkCreateDTO.getAuthorId());
        artworkFromDb.setParentId(artworkCreateDTO.getParentId());
        artworkFromDb.setModifiedDate(LocalDateTime.now());
    }
}