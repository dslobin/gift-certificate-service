package com.epam.esm.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
class ErrorResponse {
    private ZonedDateTime timestamp;
    private Integer status;
    private String error;
    private Object message;
    private Integer errorCode;
    private String path;
}
