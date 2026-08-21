package com.nandini.ledgersplit.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp, int status, String errorCode, String message, String path
){
}
