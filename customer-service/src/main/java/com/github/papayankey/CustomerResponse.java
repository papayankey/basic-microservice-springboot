package com.github.papayankey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record CustomerResponse(Object data, String title, HttpStatus status,
                               @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime timeStamp) {
}
