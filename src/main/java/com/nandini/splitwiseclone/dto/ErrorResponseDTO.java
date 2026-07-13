package com.nandini.splitwiseclone.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timeStamp, int status, String errorCode, String message, String path
){
}
