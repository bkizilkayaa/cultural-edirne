package com.bkizilkaya.culturelbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "FILE_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileData {

    @Id
    @GeneratedValue(generator = "genFileDataSeq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "genFileDataSeq", sequenceName = "SEQ_FILE_DATA", initialValue = 1011, allocationSize = 11)
    private Long Id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @ManyToOne
    @JsonIgnore
    @JoinTable(name = "artwork_images",
            joinColumns = @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_image_id")),
            inverseJoinColumns = @JoinColumn(name = "artwork_id"), foreignKey = @ForeignKey(name = "fk_artwork_id"))
    private Artwork artworkImages;

    @ManyToOne
    @JsonIgnore
    @JoinTable(name = "tourist_spot_images",
            joinColumns = @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "fk_image_id")),
            inverseJoinColumns = @JoinColumn(name = "tourist_spot_id"), foreignKey = @ForeignKey(name = "fk_tourist_spot_id"))
    private TouristSpot touristSpotImages;
}