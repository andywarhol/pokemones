package com.demon.slayer.pokemonapi.exceptions;

public class ArgumentException extends IllegalArgumentException{

	
	private static final long serialVersionUID = 1L;

	public ArgumentException(String message) {
		super(message);
	}
}
