package com.demon.slayer.pokemonapi.request;

import java.util.List;

import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RequestDeletePkm {
	
	private RequestEquipo equipo;
	
	@JsonProperty("pokemons")
	private List<RequestPokemon> pokemonList;
    
    public List<RequestPokemon>  getPokemonList() {
        return pokemonList;
    }

}
