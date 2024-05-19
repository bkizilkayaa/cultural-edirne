package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.filedata.response.FileDataResponseDTO;
import com.bkizilkaya.culturelbackend.dto.spot.request.TouristSpotCreateDTO;
import com.bkizilkaya.culturelbackend.dto.spot.response.TouristSpotResponseDTO;
import com.bkizilkaya.culturelbackend.service.concrete.TouristSpotServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin
@RequestMapping("/touristic-spots")
public class TouristSpotController {
    private final TouristSpotServiceImpl touristSpotService;

    public TouristSpotController(TouristSpotServiceImpl touristSpotService) {
        this.touristSpotService = touristSpotService;
    }

    @GetMapping()
    public ResponseEntity<List<TouristSpotResponseDTO>> getAllSpots() {
        return ResponseEntity.status(OK).body(touristSpotService.getAllTouristicSpot());
    }

    @GetMapping("/{spotId}/images")
    public ResponseEntity<List<FileDataResponseDTO>> getSpotImages(@PathVariable Long spotId) {
        List<FileDataResponseDTO> fileDataList = touristSpotService.getSpotById(spotId).getFileData();
        return ResponseEntity.status(OK).body(fileDataList);
    }

    @PostMapping("/{spotId}/images")
    public ResponseEntity<String> addImageToSpot(@PathVariable Long spotId, @RequestParam("image") MultipartFile file) {
        Long spotImagesSpotId = touristSpotService.addImageToSpot(spotId, file);
        String returnMessage = "Spota basariyla resim eklendi";
        return ResponseEntity.status(OK).body(returnMessage);
    }

    @PostMapping
    public ResponseEntity<TouristSpotResponseDTO> addSpot(@RequestBody TouristSpotCreateDTO touristSpotCreateDTO) {
        TouristSpotResponseDTO touristSpotResponseDTO = touristSpotService.addSpot(touristSpotCreateDTO);
        return ResponseEntity.status(CREATED).body(touristSpotResponseDTO);
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<TouristSpotResponseDTO> getSpotForGivenId(@PathVariable Long spotId) {
        TouristSpotResponseDTO spotFromDb = touristSpotService.getSpotById(spotId);
        return ResponseEntity.status(OK).body(spotFromDb);
    }

    @PutMapping("/{spotId}")
    public ResponseEntity<TouristSpotResponseDTO> updateSpot(@PathVariable Long spotId, @RequestBody TouristSpotCreateDTO touristSpotCreateDTO) {
        TouristSpotResponseDTO touristSpotResponseDTO = touristSpotService.updateSpot(spotId, touristSpotCreateDTO);
        return ResponseEntity.status(OK).body(touristSpotResponseDTO);
    }

    @DeleteMapping("/{spotId}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long spotId) {
        touristSpotService.deleteSpot(spotId);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @DeleteMapping("/{spotId}/images/{imageId}")
    public ResponseEntity<String> removeSpotImageFromSpot(@PathVariable Long spotId, @PathVariable Long imageId) {
        touristSpotService.removeSpotImageFromSpot(spotId, imageId);
        return ResponseEntity.status(NO_CONTENT).body("Spot image removed from spot successfully");
    }

}
