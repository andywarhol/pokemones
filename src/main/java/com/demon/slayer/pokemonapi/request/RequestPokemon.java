package com.demon.slayer.pokemonapi.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPokemon {
	private String name;
	private List<String> tipos;
	
	
	
	public String getName() {
		return name;
	}
	
	

	public List<String> getTipos() {
		return tipos;
	}
}

