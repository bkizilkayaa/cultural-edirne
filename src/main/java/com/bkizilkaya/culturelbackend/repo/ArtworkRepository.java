package com.bkizilkaya.culturelbackend.repo;

import com.bkizilkaya.culturelbackend.model.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    Page<Artwork> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
