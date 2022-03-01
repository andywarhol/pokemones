package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class PasswordFormatException extends ResponseException{

    public PasswordFormatException(){
        super("Formato de contrase�a incorrecta");
        this.status = HttpStatus.UNAUTHORIZED;
        this.name = PasswordFormatException.class.getName();
        this.description = "La contrase�a debe contar con may�sculas, min�sculas, n�meros y al menos 1 caracter especial, sin espacios, m�nimo 8 caracteres";
    }
}
