package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AdminUserPkmLimitException extends ResponseException{

    public AdminUserPkmLimitException(){
        super("You can't have more than 10 pokemons as an admin!");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.name = UserAlreadyExistException.class.getName();
        this.description = "User tried to add more pokemon than allowed.";
    }
}
