package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.dto.spot.request.TouristSpotCreateDTO;
import com.bkizilkaya.culturelbackend.dto.spot.response.TouristSpotResponseDTO;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.mapper.TouristSpotMapper;
import com.bkizilkaya.culturelbackend.model.ActionEnum;
import com.bkizilkaya.culturelbackend.model.FileData;
import com.bkizilkaya.culturelbackend.model.TableNameEnum;
import com.bkizilkaya.culturelbackend.model.TouristSpot;
import com.bkizilkaya.culturelbackend.repo.TouristSpotRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.TouristSpotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
public class TouristSpotServiceImpl implements TouristSpotService {
    private final TouristSpotRepository touristSpotRepository;
    private final FileDataServiceImpl fileDataService;
    private final ActionLogServiceImpl actionLogService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public TouristSpotServiceImpl(TouristSpotRepository touristSpotRepository, FileDataServiceImpl fileDataService, ActionLogServiceImpl actionLogService) {
        this.touristSpotRepository = touristSpotRepository;
        this.fileDataService = fileDataService;
        this.actionLogService = actionLogService;
    }


    @Override
    public TouristSpotResponseDTO addSpot(TouristSpotCreateDTO touristSpotCreateDto) {
        if (touristSpotCreateDto.getParentId() != null) {
            TouristSpot parentSpot = getSpotGivenId(touristSpotCreateDto.getParentId());
        }
        TouristSpot touristSpot = TouristSpotMapper.INSTANCE.dtoToEntity(touristSpotCreateDto);
        touristSpotRepository.save(touristSpot);
        String mappedObject = getJsonObject(touristSpot);
        actionLogService.createLog(ActionEnum.ADD, TableNameEnum.TOURIST_SPOT, mappedObject);
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
    @Transactional
    public TouristSpotResponseDTO updateSpot(Long spotId, TouristSpotCreateDTO touristSpotCreateDto) {
        TouristSpot spotFromDb = getSpotGivenId(spotId);

        updateSpotFields(touristSpotCreateDto, spotFromDb);

        touristSpotRepository.save(spotFromDb);
        String mappedObject = getJsonObject(spotFromDb);
        actionLogService.createLog(ActionEnum.UPDATE, TableNameEnum.TOURIST_SPOT, mappedObject);
        return TouristSpotMapper.INSTANCE.entityToResponseDto(spotFromDb);
    }

    private void updateSpotFields(TouristSpotCreateDTO touristSpotCreateDto, TouristSpot spotFromDb) {
        spotFromDb.setContent(touristSpotCreateDto.getContent());
        spotFromDb.setTitle(touristSpotCreateDto.getTitle());
        spotFromDb.setDescription(touristSpotCreateDto.getDescription());
        spotFromDb.setModifiedDate(LocalDateTime.now());
        spotFromDb.setAuthorId(touristSpotCreateDto.getAuthorId());
        spotFromDb.setParentId(touristSpotCreateDto.getParentId());
    }

    @Override
    public void deleteSpot(Long spotId) {
        TouristSpot spotFromDb = getSpotGivenId(spotId);
        touristSpotRepository.deleteById(spotId);
        String mappedObject = getJsonObject(spotFromDb);
        actionLogService.createLog(ActionEnum.DELETE, TableNameEnum.TOURIST_SPOT, mappedObject);
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
        String mappedObject = getJsonObject(touristSpot);
        actionLogService.createLog(ActionEnum.DELETE, TableNameEnum.TOURIST_SPOT_IMAGES, mappedObject);
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
            String mappedObject = getJsonObject(touristSpot);
            actionLogService.createLog(ActionEnum.ADD, TableNameEnum.TOURIST_SPOT_IMAGES, mappedObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return spotId;
    }

    protected TouristSpot getSpotGivenId(Long spotId) {
        return touristSpotRepository.findById(spotId)
                .orElseThrow(() -> new NotFoundException(TouristSpot.class));
    }

    @Override
    public Page<TouristSpotResponseDTO> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.touristSpotRepository.findAll(pageable).map(TouristSpotMapper.INSTANCE::entityToResponseDto);
    }

    @Override
    public Page<TouristSpotResponseDTO> searchSpotsPaginated(String title, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<TouristSpot> spots = touristSpotRepository.findByTitleContainingIgnoreCase(title, pageable);
        List<TouristSpotResponseDTO> spotDtos = spots.stream()
                .map(TouristSpotMapper.INSTANCE::entityToResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(spotDtos, pageable, spots.getTotalElements());
    }

    private String getJsonObject(TouristSpot touristSpot) {
        TouristSpotResponseDTO touristSpotResponseDTO = TouristSpotMapper.INSTANCE.entityToResponseDto(touristSpot);
        String mappedObject;
        try {
            mappedObject = objectMapper.writeValueAsString(touristSpotResponseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mappedObject;
    }
}