package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.dto.spot.request.TouristSpotCreateDTO;
import com.bkizilkaya.culturelbackend.dto.spot.response.TouristSpotResponseDTO;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.mapper.TouristSpotMapper;
import com.bkizilkaya.culturelbackend.model.FileData;
import com.bkizilkaya.culturelbackend.model.TouristSpot;
import com.bkizilkaya.culturelbackend.repo.TouristSpotRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.TouristSpotService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TouristSpotServiceImpl implements TouristSpotService {
    private final TouristSpotRepository touristSpotRepository;
    private final FileDataServiceImpl fileDataService;


    public TouristSpotServiceImpl(TouristSpotRepository touristSpotRepository, FileDataServiceImpl fileDataService) {
        this.touristSpotRepository = touristSpotRepository;
        this.fileDataService = fileDataService;
    }


    @Override
    public TouristSpotResponseDTO addSpot(TouristSpotCreateDTO touristSpotCreateDto) {
        if (touristSpotCreateDto.getParentId() != null) {
            TouristSpot parentSpot = getSpotGivenId(touristSpotCreateDto.getParentId());
        }
        TouristSpot touristSpot = TouristSpotMapper.INSTANCE.dtoToEntity(touristSpotCreateDto);
        touristSpotRepository.save(touristSpot);
        return TouristSpotMapper.INSTANCE.entityToResponseDto(touristSpot);
    }

    @Override
    public List<TouristSpotResponseDTO> getAllTouristicSpot() {
        List<TouristSpot> allSpots = touristSpotRepository.findAll();
        return allSpots.stream().map(TouristSpotMapper.INSTANCE::entityToResponseDto).collect(Collectors.toList());
    }

    @Override
    public TouristSpotResponseDTO getSpotById(Long spotId) {
        TouristSpot touristSpot = getSpotGivenId(spotId);
        return TouristSpotMapper.INSTANCE.entityToResponseDto(touristSpot);
    }

    @Override
    public TouristSpotResponseDTO updateSpot(Long spotId, TouristSpotCreateDTO touristSpotCreateDto) {
        TouristSpot spotFromDb = getSpotGivenId(spotId);

        spotFromDb.setContent(touristSpotCreateDto.getContent());
        spotFromDb.setTitle(touristSpotCreateDto.getTitle());
        spotFromDb.setDescription(touristSpotCreateDto.getDescription());
        spotFromDb.setModifiedDate(LocalDateTime.now());
        spotFromDb.setAuthorId(touristSpotCreateDto.getAuthorId());
        spotFromDb.setParentId(touristSpotCreateDto.getParentId());

        touristSpotRepository.save(spotFromDb);
        return TouristSpotMapper.INSTANCE.entityToResponseDto(spotFromDb);
    }

    @Override
    public void deleteSpot(Long spotId) {
        TouristSpot spotFromDb = getSpotGivenId(spotId);
        touristSpotRepository.deleteById(spotId);
    }

    @Override
    @Transactional
    public void removeSpotImageFromSpot(Long spotId, Long imageId) {
        TouristSpot touristSpot = getSpotGivenId(spotId);
        FileData fileDataFromDb = fileDataService.findById(imageId);
        if (touristSpot.getFileData().stream().noneMatch(fileData -> fileData.getId().equals(imageId))) {
            throw new NotFoundException(FileData.class);
        } else {
            touristSpot.setFileData(touristSpot.getFileData()
                    .stream()
                    .filter(fileData -> !fileData.getId().equals(imageId))
                    .collect(Collectors.toList()));

            fileDataFromDb.setTouristSpotImages(null);
        }
    }

    @Override
    @Transactional
    public Long addImageToSpot(Long spotId, MultipartFile file) {
        try {
            Long fileId = fileDataService.saveFile(file);
            FileData fileData = fileDataService.findById(fileId);
            TouristSpot touristSpot = getSpotGivenId(spotId);

            fileData.setTouristSpotImages(touristSpot);
            touristSpot.getFileData().add(fileData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return spotId;
    }

    protected TouristSpot getSpotGivenId(Long spotId) {
        return touristSpotRepository.findById(spotId)
                .orElseThrow(() -> new NotFoundException(TouristSpot.class));
    }
}