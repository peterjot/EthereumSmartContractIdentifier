package io.github.peterjot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Slf4j
@ControllerAdvice
class ErrorController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String exception(Throwable throwable) {
        String errorMessage = Optional
                .ofNullable(throwable)
                .map(Throwable::getMessage)
                .orElse("Unknown error");
        log.error("Error=[{}]", errorMessage);
        return "error";
    }
}
