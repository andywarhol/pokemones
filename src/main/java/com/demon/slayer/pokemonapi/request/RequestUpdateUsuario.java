package com.demon.slayer.pokemonapi.request;

import java.util.List;

import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RequestUpdateUsuario {
	
	private RequestUsuario user;
	
	private RequestEquipo equipo;
	
    public RequestUsuario getUser() {
        return user;
    }
	public RequestEquipo getEquipo() {
		return equipo;
	}
}
