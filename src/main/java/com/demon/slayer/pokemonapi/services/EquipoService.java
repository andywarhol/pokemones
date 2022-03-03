package com.demon.slayer.pokemonapi.services;

import java.util.ArrayList;
import java.util.List;

import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Pokemon;
import com.demon.slayer.pokemonapi.repositories.EquipoRepository;
import com.demon.slayer.pokemonapi.request.RequestEquipo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EquipoService {
	@Autowired
	EquipoRepository equipoRepository;
	
	public Equipo createEquipo (RequestEquipo req) {
		Equipo equipo =new Equipo();
		if(this.obtenerEquipo( req.getNombre_equipo(),req.getEntrenador())==null) {
			equipo.setNombreEquipo(req.getNombre_equipo());
			equipo.setEntrenador(req.getEntrenador());
		   equipoRepository.save(equipo);
		   }
			return equipo;
		
	}
	
	public Equipo obtenerEquipo(String nombre,String entrenador) { 
		return equipoRepository.findByNombre(nombre, entrenador).orElse(null);
	}

	public Equipo obtenerEquipo(RequestEquipo equipo) {
		return equipoRepository.findByNombre(equipo.getNombre_equipo(), equipo.getEntrenador()).orElseGet(() -> createEquipo(equipo));
	}

	public Equipo updateEquipo(Equipo equipoOld, RequestEquipo equipoNew){
		if(equipoNew.getEntrenador() != null && 
				! equipoNew.getEntrenador().isEmpty()) {
			equipoOld.setEntrenador(equipoNew.getEntrenador());
		}
		
		if(equipoNew.getNombre_equipo() != null && 
				!equipoNew.getNombre_equipo().isEmpty()) {
			equipoOld.setNombreEquipo(equipoNew.getNombre_equipo());
		}
		
	
		return equipoRepository.save(equipoOld);
	}
	

	
}
