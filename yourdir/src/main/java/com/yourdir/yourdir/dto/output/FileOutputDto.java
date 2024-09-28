package com.yourdir.yourdir.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public record FileOutputDto(Long id,
                            String name,
                            String content,
                            Long directoryId,
                            @JsonSerialize(using = LocalDateTimeSerializer.class)
                            @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                            LocalDateTime createdAt,
                            @JsonSerialize(using = LocalDateTimeSerializer.class)
                            @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                            LocalDateTime updatedAt) {
}