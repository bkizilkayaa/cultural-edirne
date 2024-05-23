package com.bkizilkaya.culturelbackend.job;

import com.bkizilkaya.culturelbackend.service.abstraction.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Log4j2
public class FileCleanupJob {

    @Value("${local.file.path}")
    private String FOLDER_PATH;
    private final StorageService storageService;

    public FileCleanupJob(StorageService storageService) {
        this.storageService = storageService;
    }

    //2 saatte bir 0 0 */2 * * *
    //dakikada bir 0 */1 * * * *
    @Scheduled(cron = "0 0 */2 * * *")
    public void cleanupData() {
        log.warn("FileCleanupJob basladi...");
        List<Long> allFileDataIds = storageService.findUnusedFilesId();
        //allFileDataIds içerisinde kullanılmayan ancak veritabanında mevcut file id ler bulunuyor.
        List<String> allFileDataNames = storageService.findUnusedFilesName();
        //allFileDataNames içerisinde kullanılmayan ancak veritabanında mevcut file nameler bulunuyor.

        if (allFileDataIds.size() == 0) {
            log.info("Dosya yolu ile veritabani senkron halde!");
        } else {
            deleteFilesFromDb(allFileDataIds);
            deleteFilesFromPath(allFileDataNames);
        }

        log.warn("FileCleanupJob bitti");
    }

    public void deleteFilesFromPath(List<String> fileNamesToDelete) {
        log.info("Dosyalar base pathten okunmaya baslaniyor..");
        for (String fileName : fileNamesToDelete) {
            File fileToDelete = new File(FOLDER_PATH + fileName);
            if (fileToDelete.exists()) {
                deleteSingleFile(fileName, fileToDelete);
            } else {
                log.error("Silinecek dosya bulunamadi: " + fileName);
            }
        }
    }

    private static void deleteSingleFile(String fileName, File fileToDelete) {
        if (fileToDelete.delete()) {
            log.info("Dosya basariyla silindi: " + fileName);
        } else {
            log.error("Dosya silinirken bir hata olustu: " + fileName);
        }
    }

    public void deleteFilesFromDb(List<Long> fileIds) {
        log.info("Veritabanindan kullanilmayan dosyalarin kayitlari siliniyor..");
        fileIds.forEach(storageService::deleteFile);
    }
}
