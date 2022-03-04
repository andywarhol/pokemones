package com.demon.slayer.pokemonapi.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RequestEquipo {
	
	@NotNull
	@Size(min=2, message="Wrong input")
	private String entrenador;
	@JsonProperty("nombre_equipo")

	@NotNull
	@Size(min=2, message="Wrong input")
	private String nombreEquipo;
  
	public String getEntrenador() {
		return entrenador;
	}
    public String getNombre_equipo() {
        return nombreEquipo;
    }
    public String getNombreequipo() {
        return null;
    }

}
