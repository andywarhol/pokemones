package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NoPokemonException extends ResponseException{

    public NoPokemonException(){
        super("You need to have at least 1 Pokemon in your team in order to register!");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.name = UserAlreadyExistException.class.getName();
        this.description = "User tried to register without pokemons";
    }
}
