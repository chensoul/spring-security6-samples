package com.chensoul.security.spring;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ToString
public class ErrorResponse {
    private final HttpStatus status;

    private final String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    protected ErrorResponse(final String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(final String message, HttpStatus status) {
        return new ErrorResponse(message, status);
    }

    public Integer getStatus() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
