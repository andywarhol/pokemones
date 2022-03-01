package com.demon.slayer.pokemonapi.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ProvisionalUserPkmLimitException extends ResponseException{

    public ProvisionalUserPkmLimitException(){
        super("You can't have more than 5 pokemons as a provisional user!");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.name = UserAlreadyExistException.class.getName();
        this.description = "User tried to add more pokemon than allowed.";
    }
}
