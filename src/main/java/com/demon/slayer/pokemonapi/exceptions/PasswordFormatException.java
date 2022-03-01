package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PasswordFormatException extends ResponseException{

    public PasswordFormatException(){
        super("Formato de contraseña incorrecta");
        this.status = HttpStatus.UNAUTHORIZED;
        this.name = PasswordFormatException.class.getName();
        this.description = "La contraseña debe contar con mayúsculas, minúsculas, números y al menos 1 caracter especial, sin espacios, mínimo 8 caracteres";
    }
}
