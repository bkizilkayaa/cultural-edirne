package com.bkizilkaya.culturelbackend.dto.spot.response;

import com.bkizilkaya.culturelbackend.configuration.CustomLocalDateTimeSerializer;
import com.bkizilkaya.culturelbackend.dto.filedata.response.FileDataResponseDTO;
import com.bkizilkaya.culturelbackend.model.ZipCode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "content", "zipCode", "authorId", "parentId", "fileData", "createDate", "modifiedDate"})
public class TouristSpotResponseDTO {
    private Long Id;
    private String title;
    private String content;
    private String description;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime modifiedDate;

    private Long authorId;
    private ZipCode zipCode;
    private List<FileDataResponseDTO> fileData;
    private Long parentId;

    public String getFormattedDate(LocalDateTime ldt) {
        if (ldt != null) {
            return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "-";
    }
}
