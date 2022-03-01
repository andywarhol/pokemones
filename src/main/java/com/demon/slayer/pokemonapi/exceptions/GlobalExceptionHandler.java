package com.demon.slayer.pokemonapi.exceptions;

import java.util.Date;

import com.demon.slayer.pokemonapi.models.ErrorDetails;
import com.demon.slayer.pokemonapi.response.ResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorDetails> exceptionHandler(ResponseException exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        logger.warn("Exception: "+exception.getName());
        logger.warn("Cause: "+exception.getDescription());
        return new ResponseEntity<>(errorDetails, exception.getStatus());
    }

    @ExceptionHandler(ArgumentException.class)
    public ResponseEntity<ResponseDTO<String>> illegalArgument(ArgumentException ex){
    	ResponseDTO<String> response = new ResponseDTO<String>("The arguments of the request are wrong", ex.getMessage());
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}
