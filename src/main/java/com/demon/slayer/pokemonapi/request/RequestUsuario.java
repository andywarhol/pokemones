package com.demon.slayer.pokemonapi.request;

import lombok.Data;

@Data
public class RequestUsuario {
    
    private String usuario;
	private String rol;
    private String password;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	//public String getRol() {
		//return rol;
	//}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
   // private long id_equipo;

  
  
}

