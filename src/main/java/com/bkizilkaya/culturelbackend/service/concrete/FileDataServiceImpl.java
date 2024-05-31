package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.dto.filedata.response.FileDataResponseDTO;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.exception.ValidationException;
import com.bkizilkaya.culturelbackend.mapper.FileDataMapper;
import com.bkizilkaya.culturelbackend.model.FileData;
import com.bkizilkaya.culturelbackend.repo.FileDataRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.StorageService;
import com.bkizilkaya.culturelbackend.utils.ImageValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FileDataServiceImpl implements StorageService {
    @Value("${local.file.path}")
    private String FOLDER_PATH;
    private final FileDataRepository fileDataRepository;
    private final ImageValidator imageValidator;
    private final PathServiceImpl pathService;

    public FileDataServiceImpl(FileDataRepository fileDataRepository, ImageValidator imageValidator, PathServiceImpl pathService) {
        this.fileDataRepository = fileDataRepository;
        this.imageValidator = imageValidator;
        this.pathService = pathService;
    }

    @Override
    public List<FileDataResponseDTO> getAll() {
        List<FileData> fileDataListFromDb = fileDataRepository.findAll();
        return fileDataListFromDb.stream().map(FileDataMapper.INSTANCE::fileDataToResponseDto).collect(Collectors.toList());
    }

    @Override
    public Long saveFile(MultipartFile multiPartFile) throws IOException {
        validateImage(multiPartFile);

        String fileName = pathService.generateFileName(multiPartFile);
        String basePath = System.getProperty("user.dir") + FOLDER_PATH;

        checkFilePathAndCreateFoldersIfNotExists(basePath);

        Long fileId = saveFileDataToDatabase(multiPartFile, fileName);
        transferFile(multiPartFile, fileName, basePath);
        return fileId;
    }

    private static void transferFile(MultipartFile multiPartFile, String fileName, String basePath) throws IOException {
        File file = new File(basePath, fileName);
        multiPartFile.transferTo(file);
    }

    private static void checkFilePathAndCreateFoldersIfNotExists(String basePath) {
        File folder = new File(basePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public String getFilePathFromStorage(String fileName) {
        FileData fileData = findByName(fileName);
        return System.getProperty("user.dir") + FOLDER_PATH + fileData.getName();
    }

    private Long saveFileDataToDatabase(MultipartFile multiPartFile, String fileName) {
        pathService.getFileExtension(multiPartFile.getOriginalFilename());
        FileData fileData = fileDataRepository.save(FileData.builder()
                .name(fileName)
                .createDate(LocalDateTime.now())
                .type(multiPartFile.getContentType()).build());
        return fileData.getId();
    }

    @Override
    public FileData findByName(String fileName) {
        return fileDataRepository.findByName(fileName)
                .orElseThrow(() -> new NotFoundException(FileData.class, fileName));
    }

    @Override
    public void deleteFile(Long fileId) {
        FileData fileDataFromDb = findById(fileId);
        fileDataRepository.deleteById(fileId);
    }

    @Override
    public List<Long> findUnusedFilesId() {
        return fileDataRepository.findUnusedFilesId().orElseThrow(() -> new RuntimeException("an error occurred when fetching data from db"));
    }

    @Override
    public List<String> findUnusedFilesName() {
        return fileDataRepository.findUnusedFilesName().orElseThrow(() -> new RuntimeException("an error occurred when fetching data from db"));
    }

    protected FileData findById(Long fileId) {
        return fileDataRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException(FileData.class));
    }

    private void validateImage(MultipartFile multiPartFile) {
        if (!imageValidator.isImage(multiPartFile)) {
            throw new ValidationException("Eklenen dosyayı kontrol ediniz!");
        }
        if (!imageValidator.isFileSizeValid(multiPartFile)) {
            throw new ValidationException("Maksimum dosya boyutuna ulaşıldı : 10MB+");
        }
    }
}
