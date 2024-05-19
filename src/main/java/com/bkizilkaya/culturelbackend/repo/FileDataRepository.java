package com.bkizilkaya.culturelbackend.repo;

import com.bkizilkaya.culturelbackend.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(String fileName);

    @Query("SELECT fd.Id FROM FileData fd LEFT JOIN fd.artworkImages ai LEFT JOIN fd.touristSpotImages ti ON fd.Id = ti.Id WHERE ai.Id IS NULL AND ti.Id IS NULL")
    Optional<List<Long>> findUnusedFilesId();

    @Query("SELECT fd.name FROM FileData fd LEFT JOIN fd.artworkImages ai LEFT JOIN fd.touristSpotImages ti ON fd.Id = ti.Id WHERE ai.Id IS NULL AND ti.Id IS NULL")
    Optional<List<String>> findUnusedFilesName();
}
