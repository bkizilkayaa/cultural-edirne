package com.bkizilkaya.culturelbackend.repo;

import com.bkizilkaya.culturelbackend.model.Artwork;
import com.bkizilkaya.culturelbackend.model.TouristSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {

    Page<TouristSpot> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
