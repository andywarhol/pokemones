package com.demon.slayer.pokemonapi.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseDTO<T> {

	private String message;
	private T response;
	
	public ResponseDTO(String message, T response) {
		this.message=message;
		this.response=response;
	}
}
