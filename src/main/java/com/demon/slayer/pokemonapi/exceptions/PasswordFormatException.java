package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PasswordFormatException extends ResponseException{

    public PasswordFormatException(){
        super("Formato de contrasena incorrecta");
        this.status = HttpStatus.UNAUTHORIZED;
        this.name = PasswordFormatException.class.getName();
        this.description = "La contrasena debe contar con mayusculas, minusculas, numeros y al menos 1 caracter especial, sin espacios, minimo 8 caracteres";
    }
}
