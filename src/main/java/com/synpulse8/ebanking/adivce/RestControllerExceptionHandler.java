package com.synpulse8.ebanking.adivce;

import com.synpulse8.ebanking.enums.Status;
import com.synpulse8.ebanking.exceptions.base.SynpulseSystemException;
import com.synpulse8.ebanking.exceptions.base.SynpulseSystemRuntimeException;
import com.synpulse8.ebanking.response.dto.ErrorDto;
import com.synpulse8.ebanking.response.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice(basePackages = {"com.synpulse8.ebanking"})
public class RestControllerExceptionHandler {
    @ExceptionHandler(value = {SynpulseSystemRuntimeException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleSynpulseSystemRuntimeException(SynpulseSystemRuntimeException ex, WebRequest request) {
        return ResponseEntity.ok().body(
                new ResponseDto<>(
                        Status.FAIL,
                        new ErrorDto(ex.getCode(), ex.getMessage())
                )
        );
    }

    @ExceptionHandler(value = {SynpulseSystemException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleSynpulseSystemException(SynpulseSystemException ex, WebRequest request) {
        return ResponseEntity.ok().body(
                new ResponseDto<>(
                        Status.FAIL,
                        new ErrorDto(ex.getCode(), ex.getMessage())
                )
        );
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleException(Exception ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(
                new ResponseDto<>(
                        Status.FAIL,
                        new ErrorDto(
                                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                ex.getMessage())
                )
        );
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDto<ErrorDto>> handleConversion(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                new ResponseDto<>(
                        Status.FAIL,
                        new ErrorDto(
                                HttpStatus.BAD_REQUEST.name(),
                                ex.getMessage())
                )
        );
    }
}
