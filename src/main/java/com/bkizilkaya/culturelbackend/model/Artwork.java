package com.bkizilkaya.culturelbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "artworks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private Long parentId;
    @Column
    private LocalDateTime createDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Image> images;

    @Column
    private Long authorId;

    @JsonManagedReference
    public List<Image> getImages(){
        return images;
    }

}
