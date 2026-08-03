package com.nandini.splitwiseclone.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp, int status, String errorCode, String message, String path
){
}
