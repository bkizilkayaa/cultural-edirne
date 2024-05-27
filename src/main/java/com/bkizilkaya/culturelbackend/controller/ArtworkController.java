package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.artwork.request.ArtworkCreateDTO;
import com.bkizilkaya.culturelbackend.dto.artwork.response.ArtworkResponseDTO;
import com.bkizilkaya.culturelbackend.dto.filedata.response.FileDataResponseDTO;
import com.bkizilkaya.culturelbackend.service.concrete.ArtworkServiceImpl;
import org.springframework.data.domain.Page;
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
@RequestMapping("/artworks")
public class ArtworkController {
    private final ArtworkServiceImpl artworkService;

    public ArtworkController(ArtworkServiceImpl artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping
    public ResponseEntity<List<ArtworkResponseDTO>> getAllArtworks() {
        return new ResponseEntity<>(artworkService.getAllArtworks(), OK);
    }

    @GetMapping("/page/{pageNum}")
    public ResponseEntity<Page<ArtworkResponseDTO>> getArtworkPaginated(@PathVariable int pageNum) {
        Page<ArtworkResponseDTO> paginated = artworkService.findPaginated(pageNum, 5);
        return new ResponseEntity<>(paginated, OK);
    }

    @GetMapping("/{artworkId}/images")
    public ResponseEntity<List<FileDataResponseDTO>> getArtworkImage(@PathVariable Long artworkId) {
        List<FileDataResponseDTO> fileDataList = artworkService.getArtworkGivenId(artworkId).getFileData();
        return new ResponseEntity<>(fileDataList, OK);
    }

    @PostMapping("/{artworkId}/images")
    public ResponseEntity<String> addImageToArtwork(@PathVariable Long artworkId, @RequestParam("image") MultipartFile file) {
        Long artworkImagesArtworkId = artworkService.addImageToArtwork(artworkId, file);
        String returnMessage = "Artworke basariyla resim eklendi";
        return new ResponseEntity<>(returnMessage, OK);
    }

    @PostMapping
    public ResponseEntity<ArtworkResponseDTO> addArtwork(@RequestBody ArtworkCreateDTO newArtwork) {
        ArtworkResponseDTO artworkResponseDTO = artworkService.addArtwork(newArtwork);
        return new ResponseEntity<>(artworkResponseDTO, CREATED);
    }

    @PostMapping("/save-multiple")
    public ResponseEntity<String> addArtworkList(@RequestBody List<ArtworkCreateDTO> newArtworkList) {
        for (ArtworkCreateDTO artwork : newArtworkList) {
            artworkService.addArtwork(artwork);
        }
        return new ResponseEntity<>("Objects created successfully", CREATED);
    }

    @GetMapping("/{artwork_id}")
    public ResponseEntity<ArtworkResponseDTO> getArtworkForGivenId(@PathVariable Long artwork_id) {
        ArtworkResponseDTO artworkFromDb = artworkService.getArtworkGivenId(artwork_id);
        return new ResponseEntity<>(artworkFromDb, OK);
    }

    @PutMapping("/{artworkId}")
    public ResponseEntity<ArtworkResponseDTO> updateArtwork(@PathVariable Long artworkId, @RequestBody ArtworkCreateDTO artworkCreateDTO) {
        ArtworkResponseDTO artworkResponseDTO = artworkService.updateArtwork(artworkId, artworkCreateDTO);
        return new ResponseEntity<>(artworkResponseDTO, OK);
    }

    @DeleteMapping("/{artworkId}")
    public ResponseEntity<Void> deleteArtwork(@PathVariable Long artworkId) {
        artworkService.deleteArtwork(artworkId);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @DeleteMapping("/{artworkId}/images/{imageId}")
    public ResponseEntity<String> removeArtworkImageFromArtwork(@PathVariable Long artworkId, @PathVariable Long imageId) {
        artworkService.removeArtworkImageFromArtwork(artworkId, imageId);
        return ResponseEntity.status(NO_CONTENT).body("Artwork image removed from artwork successfully");
    }

}
