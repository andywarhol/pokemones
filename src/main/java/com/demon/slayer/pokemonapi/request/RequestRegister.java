package com.demon.slayer.pokemonapi.request;

import java.util.List;

import javax.validation.constraints.NotNull;




public class RequestRegister {
	
	private RequestEquipo equipo;
	private RequestUsuario user;
	
	@NotNull
	private List<RequestPokemon> pokemons;
	
	public RequestUsuario getUsuario() {
		return user;
	}
	public RequestEquipo getEquipo() {
		return equipo;
	}
	public RequestUsuario getUser() {
		return user;
	}
	public void setUser(RequestUsuario user) {
		this.user = user;
	}
	public List<RequestPokemon> getPokemons() {
		return pokemons;
	}
	public void setPokemons(List<RequestPokemon> pokemons) {
		this.pokemons = pokemons;
	}
	public void setEquipo(RequestEquipo equipo) {
		this.equipo = equipo;
	}
	

}
