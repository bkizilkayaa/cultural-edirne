package com.bkizilkaya.culturelbackend.dto.filedata.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({"id", "name", "type"})
public class FileDataResponseDTO {
    private Long Id;
    private String name;
    private String type;
}
