package com.demon.slayer.pokemonapi.controllers;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import com.demon.slayer.pokemonapi.exceptions.EmailFormatException;
import com.demon.slayer.pokemonapi.exceptions.UserNotFoundException;
import com.demon.slayer.pokemonapi.models.Pokemon;
import com.demon.slayer.pokemonapi.models.Testing;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.demon.slayer.pokemonapi.models.Usuario;

import com.demon.slayer.pokemonapi.repositories.EquipoRepository;
import com.demon.slayer.pokemonapi.repositories.PokemonRepository;
import com.demon.slayer.pokemonapi.repositories.UsuarioRepository;
import com.demon.slayer.pokemonapi.request.RequestAddNewPkmUsuario;
import com.demon.slayer.pokemonapi.request.RequestDeletePkm;
import com.demon.slayer.pokemonapi.request.RequestEquipo;
import com.demon.slayer.pokemonapi.request.RequestLoginUsuario;
import com.demon.slayer.pokemonapi.request.RequestRegister;
import com.demon.slayer.pokemonapi.request.RequestTipo;
import com.demon.slayer.pokemonapi.response.JWTAuthResponse;
import com.demon.slayer.pokemonapi.response.PokemonsResponse;
import com.demon.slayer.pokemonapi.response.ResponseCreate;
import com.demon.slayer.pokemonapi.response.ResponseDTO;
import com.demon.slayer.pokemonapi.response.ResponsePokemon;
import com.demon.slayer.pokemonapi.response.ResponseTipos;
import com.demon.slayer.pokemonapi.response.ResponseUsuario;
import com.demon.slayer.pokemonapi.security.JwtAuthenticationFilter;
import com.demon.slayer.pokemonapi.security.JwtTokenProvider;
import com.demon.slayer.pokemonapi.services.EquipoService;
import com.demon.slayer.pokemonapi.services.PokemonService;
import com.demon.slayer.pokemonapi.services.TipoService;
import com.demon.slayer.pokemonapi.request.RequestUpdateUsuario;
import com.demon.slayer.pokemonapi.request.RequestUsuario;
import com.demon.slayer.pokemonapi.services.UsuarioService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/")
public class UsuarioController {
    
    @Autowired
	UsuarioService usuarioService;
    
    @Autowired
    PokemonService pokemonService;
    
    @Autowired
    TipoService tipoService;
    
    @Autowired
    EquipoService equipoService;
   
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;
   
   @Autowired
   private JwtAuthenticationFilter jwtFilter;

    @Autowired
    UsuarioRepository userRepo;
    
    @Autowired
    PokemonRepository pokemonRepo;
    
    Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
     
    @GetMapping("/saludo")
	public String saludo(){
		return "Hola si funciona";
	}
    @PostMapping("/register")
    public ResponseDTO<String> createUsuario(@Valid @RequestBody RequestRegister datos){
        usuarioService.createUsuario(datos);
        return new ResponseDTO<String>("success","the user was created");
    }
        
    @PutMapping("/update/{username}")
    public ResponseDTO<String> requestUpdateUsuario(@Valid @RequestBody RequestUpdateUsuario datos, @PathVariable String username) {
        logger.warn("datos: "+datos);
        logger.warn("username: "+username);
        return new ResponseDTO<String>("user updated successfully", usuarioService.requestUpdateUsuario(datos, username));
    }
    
    @PostMapping("/addNewPkm/{username}")
    public ResponseDTO<String> requestAddPkmUsuario(@Valid @RequestBody RequestAddNewPkmUsuario datos, @PathVariable String username) {
        logger.warn("datos: "+datos);
        logger.warn("username: "+username);
    	return new ResponseDTO<String>("pokemon added succesfully",usuarioService.requestAddPkmUsuario(datos, username));
    }

    @DeleteMapping("delete/{username}")
    public ResponseDTO<String> borrarUsuario(@PathVariable String username) {
    	userRepo.deleteById(username);
    	String message = "User " + username + "Borrado con exito";
    	ResponseDTO<String> response = new ResponseDTO<String>("deleted",message);
    	return response;
    }

    @DeleteMapping("deletePkm/{username}")
    public ResponseDTO<String> deletePkm(@Valid @RequestBody RequestDeletePkm datos, @PathVariable String username) {
        logger.warn("datos: "+datos);
        logger.warn("username: "+username);
        ResponseDTO<String> response = new ResponseDTO<String>("deleted",usuarioService.pkmDelete(datos, username));
    	return response;
    }
    
    @PostMapping("/login")
    public JWTAuthResponse login(@RequestBody RequestLoginUsuario usuario){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            usuario.getUsuario(), usuario.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);
        return new JWTAuthResponse("Bearer "+token);
    }
	 
	@GetMapping("get_tipos")
	public ResponseTipos getTipos(){
		
		return tipoService.getAllTipos();

	}

    @PostMapping("testing")
    public String testing(@RequestBody Testing testing){
        return "funciono";
    }
	
	@GetMapping("get_pokemons/{username}")
	public PokemonsResponse getByUsuario(@PathVariable String username){
		
		return usuarioService.pokemonesUsuario(username);
	}

    @GetMapping("get_user/{username}")
    public ResponseUsuario getByUsername(@PathVariable String username){
        return usuarioService.buscarUsuario(username);
    }
    

	@GetMapping("/verify-token")
	public ResponseEntity<ResponseDTO<ResponseUsuario>> getUserByToken(@RequestHeader(name = "Authorization") String token) {
		String user =  tokenProvider.getUsernameFromJWT(token.substring(6, token.length()));
		ResponseUsuario userDto = usuarioService.buscarUsuario(user); 
		ResponseDTO<ResponseUsuario> response = new ResponseDTO<ResponseUsuario>("the user by the token is", userDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

    @GetMapping("get_all")
    public List<ResponseUsuario> getAll() {
    	List<Usuario> usuarios = userRepo.findAll();
    	List<ResponseUsuario> responseUser = new ArrayList<ResponseUsuario>();
    	
    	usuarios.stream().forEach(user -> {
    		responseUser.add(new ResponseUsuario(user));
    	});
    	return responseUser;
    }
}

