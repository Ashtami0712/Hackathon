package com.simplogics.base.utils;

import com.simplogics.base.dto.FrameworkBaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<FrameworkBaseResponse> getStatusOkResponseEntity(Object dto, String messageCode) {
        return ResponseEntity.status(HttpStatus.OK).body(FrameworkBaseResponse.builder()
                .status(true)
                .data(dto)
                .hasErrors(false)
                .message(Translator.translateToLocale(messageCode))
                .errors(null)
                .build());
    }

    public static ResponseEntity<FrameworkBaseResponse> getStatusCreatedResponseEntity(Object dto, String messageCode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(FrameworkBaseResponse.builder()
                .status(true)
                .data(dto)
                .hasErrors(false)
                .message(messageCode)
                .errors(null)
                .build());
    }

}
