package com.bkizilkaya.culturelbackend.dto.actionlog;

import com.bkizilkaya.culturelbackend.configuration.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@JsonPropertyOrder({"id", "action", "tableName", "timestamp", "logDetail", "user"})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActionLogDTO {

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private Long Id;

    private String user;

    private String action;

    private String tableName;

    private LocalDateTime timestamp;
    @Lob
    private String logDetail;
    public String getFormattedDate(LocalDateTime ldt) {
        if (ldt != null) {
            return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "-";
    }
}
