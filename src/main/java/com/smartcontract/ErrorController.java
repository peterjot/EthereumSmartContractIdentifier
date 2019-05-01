package com.smartcontract;


import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
class ErrorController {

    private static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String exception(@NonNull Throwable throwable) {
        String errorMessage = Optional
                .ofNullable(throwable)
                .map(Throwable::getMessage)
                .orElse("Unknown error");
        LOGGER.error("Error=[{}]", errorMessage);
        return "error";
    }
}