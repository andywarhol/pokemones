package com.demon.slayer.pokemonapi.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.*;

@Getter
@Setter
@Entity
@Table(name="usuarios")

public class Usuario {
	
	@Id
	@Column(name="usuario",length=30, unique=true)
	private String usuario;
	
	@Column(name="rol")
	private String rol;
	
	@Column(name="pass")
	private String password;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "team_id")
	private Equipo equipo;

	public void setEquipo(Equipo equipo) {
		this.equipo=equipo;
	}

	public void setUsuario(String usuario2) {
		this.usuario=usuario2;
		
	}
	
	public void setRol(String rol) {
		this.rol=rol;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	
	


    public Equipo getEquipo() {
        return equipo;
    }

	
}
