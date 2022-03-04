package com.demon.slayer.pokemonapi.response;

import com.demon.slayer.pokemonapi.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUsuario {
    
    private String usuario;
    private String rol;
    private ResponseEquipo equipo;

    public ResponseUsuario(Usuario usuario){
        this.usuario = usuario.getUsuario();
        this.rol = usuario.getRol();
        this.equipo = new ResponseEquipo(usuario.getEquipo());
    }
}
