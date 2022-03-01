package com.demon.slayer.pokemonapi.models;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.demon.slayer.pokemonapi.request.RequestEquipo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name ="equipos")
public class Equipo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idequipo;
	
	@Column(name="entrenador")
	private String entrenador; 
	
	@Column(name="nombreEquipo")
	private String nombreequipo;
	
	@OneToOne(mappedBy = "equipo", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
	private Usuario usuario;
	
	@ManyToMany(mappedBy = "equipos",  fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Pokemon> pokemons =new ArrayList<>();
	


	public void setNombreEquipo(String nombre_equipo) {
		this.nombreequipo=nombre_equipo;
		
	}

	public void setEntrenador(String entrena) {
		this.entrenador=entrena;
		
	}

	public void setPokemons(List<Pokemon> list) {
		this.pokemons=list;
	}

	public String getNombreequipo() {
		return nombreequipo;
	}

	public Equipo(RequestEquipo data) {
		this.entrenador = data.getEntrenador();
		this.nombreequipo = data.getNombre_equipo();
		
		
	}
	
    public List<Pokemon> getPokemons() {
        return pokemons;
    }

	public String getEntrenador() {
		return entrenador;
	}

	


}
