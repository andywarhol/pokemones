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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.demon.slayer.pokemonapi.request.RequestPokemon;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity

@NoArgsConstructor
@Table(name ="pokemon")
public class Pokemon {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idpokemon;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="status")
	private int status;
	
	@ManyToMany(fetch=FetchType.LAZY ,cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(
		name="pokemon_tipo",
				joinColumns=@JoinColumn(name="idpokemon"),
				inverseJoinColumns= @JoinColumn(name="idTipo") 
	)
	private List<Tipo> tipos =new ArrayList<>();
	
	public Pokemon(RequestPokemon p) {
		this.nombre = p.getName();
	}

    @ManyToMany(cascade= {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(
		name="pokemon_equipo",
				joinColumns=@JoinColumn(name="idpokemon"),
				inverseJoinColumns= @JoinColumn(name="idequipo")
	)
	private List<Equipo> equipos =new ArrayList<>();
	
	public void setNombre(String name) {
		this.nombre=name;
	}
	public void setStatus(int s) {
		this.status=s;
	}
	public void setTipos(List<Tipo> tipos) {
		this.tipos=tipos;
	}

	public void setEquipos(List<Equipo>equipos) {
		this.equipos=equipos;
	}
	public String getNombre() {
		return nombre;
	}
	public List<Tipo> getTipos() {
		return this.getTipos();
	}
	public List<Equipo> getEquipos() {
		return equipos;
	}
    public Long getIdpokemon() {
        return idpokemon;
    }
}