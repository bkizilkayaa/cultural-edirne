package com.bkizilkaya.culturelbackend.dto.artwork.request;

import com.bkizilkaya.culturelbackend.configuration.CustomLocalDateTimeSerializer;
import com.bkizilkaya.culturelbackend.model.FileData;
import com.bkizilkaya.culturelbackend.model.ZipCode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "content", "zipCode", "authorId", "parentId", "fileData", "createDate", "modifiedDate"})
@AllArgsConstructor
public class ArtworkCreateDTO {
    private Long Id;
    private String title;
    private String content;
    private String description;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime modifiedDate;
    private Long authorId;
    private Long parentId;
    private ZipCode zipCode;
    private List<FileData> fileData;
}
