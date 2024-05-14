package com.bkizilkaya.culturelbackend.controller;

import com.bkizilkaya.culturelbackend.dto.filedata.response.FileDataResponseDTO;
import com.bkizilkaya.culturelbackend.service.abstraction.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/images")
public class FileController {
    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping()
    public ResponseEntity<List<FileDataResponseDTO>> getAllFileData() {
        return ResponseEntity.status(HttpStatus.OK).body(storageService.getAll());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImageToFileSystem(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        Long imageId = storageService.saveFile(multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body("Resim dosya yoluna eklendi");
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<String> getFilePathFromStorage(@PathVariable String fileName) throws IOException {
        String imageData = storageService.getFilePathFromStorage(fileName);
        return ResponseEntity.status(HttpStatus.OK).body(imageData);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        storageService.deleteFile(fileId);
        return new ResponseEntity<>("file deleted", HttpStatus.NO_CONTENT);
    }
}
