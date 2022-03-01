package com.demon.slayer.pokemonapi.repositories;

import java.util.Optional;

import com.demon.slayer.pokemonapi.models.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UsuarioRepository extends JpaRepository<Usuario, String> {
	

	@Query("From Usuario where usuario=:usuario ")
	Optional<Usuario> findByUsuario(@Param("usuario") String usuario);
	 

	
	
}


