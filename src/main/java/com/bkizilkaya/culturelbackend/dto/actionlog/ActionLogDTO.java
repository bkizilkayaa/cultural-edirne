package com.bkizilkaya.culturelbackend.dto.actionlog;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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

    public String getBeautifyLogDetail() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this.logDetail);
    }
}
