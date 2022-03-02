package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NoPokemonNameException extends ResponseException{

    public NoPokemonNameException(){
        super("Each pokemon MUST HAVE a name, it cannot be blank or empty!");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.name = UserAlreadyExistException.class.getName();
        this.description = "User tried to register without giving name to their pokemon";
    }
}
