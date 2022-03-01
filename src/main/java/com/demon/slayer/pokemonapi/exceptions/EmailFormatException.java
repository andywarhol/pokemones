package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class EmailFormatException extends ResponseException{

    public EmailFormatException(){
        super("El formato de Email no es válido");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.name = EmailFormatException.class.getName();
        this.description = "Se intento introducir un correo con el fomato incorrecto";
    }
}
