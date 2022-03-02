package com.demon.slayer.pokemonapi.repositories;

import java.util.Optional;

import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Pokemon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface EquipoRepository  extends JpaRepository<Equipo, Long>{
	
	@Query("From Equipo where nombreequipo=:nombreequipo AND entrenador=:entrenador")
	Optional<Equipo> findByNombre(@Param("nombreequipo") String nombreequipo,@Param("entrenador") String entrenador);
}
