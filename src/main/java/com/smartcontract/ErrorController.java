package com.smartcontract;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {

    private static Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Throwable throwable) {
        LOGGER.error("Exception during execution of application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        LOGGER.error("Error: " + errorMessage);
        return "error";
    }

}