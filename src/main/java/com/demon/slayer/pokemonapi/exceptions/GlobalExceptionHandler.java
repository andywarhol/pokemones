package com.demon.slayer.pokemonapi.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.demon.slayer.pokemonapi.models.ErrorDetails;
import com.demon.slayer.pokemonapi.response.ResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    
    @Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Map<String, String> errores = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error->{
			String fieldName= ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			errores.put(fieldName, message);
		});
		return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
	}

}
