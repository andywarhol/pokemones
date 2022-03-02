package com.demon.slayer.pokemonapi.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.demon.slayer.pokemonapi.exceptions.ArgumentException;
import com.demon.slayer.pokemonapi.exceptions.EmailFormatException;
import com.demon.slayer.pokemonapi.exceptions.AdminUserPkmLimitException;
import com.demon.slayer.pokemonapi.exceptions.EmailFormatException;
import com.demon.slayer.pokemonapi.exceptions.NoPokemonNameException;
import com.demon.slayer.pokemonapi.exceptions.PasswordFormatException;
import com.demon.slayer.pokemonapi.exceptions.ProvisionalUserPkmLimitException;
import com.demon.slayer.pokemonapi.exceptions.SamePokemonException;
import com.demon.slayer.pokemonapi.exceptions.TrainerAlreadyExistException;
import com.demon.slayer.pokemonapi.exceptions.UserAlreadyExistException;
import com.demon.slayer.pokemonapi.exceptions.UserNotFoundException;
import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Pokemon;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.demon.slayer.pokemonapi.models.Usuario;
import com.demon.slayer.pokemonapi.repositories.EquipoRepository;
import com.demon.slayer.pokemonapi.repositories.PokemonRepository;
import com.demon.slayer.pokemonapi.repositories.UsuarioRepository;
import com.demon.slayer.pokemonapi.request.RequestAddNewPkmUsuario;
import com.demon.slayer.pokemonapi.request.RequestDeletePkm;
import com.demon.slayer.pokemonapi.request.RequestPokemon;
import com.demon.slayer.pokemonapi.request.RequestRegister;
import com.demon.slayer.pokemonapi.request.RequestUpdateUsuario;
import com.demon.slayer.pokemonapi.request.RequestUsuario;
import java.util.Optional;

import com.demon.slayer.pokemonapi.models.Pokemon;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.demon.slayer.pokemonapi.models.Usuario;
import com.demon.slayer.pokemonapi.repositories.TipoRepository;
import com.demon.slayer.pokemonapi.repositories.UsuarioRepository;
import com.demon.slayer.pokemonapi.request.RequestPokemon;
import com.demon.slayer.pokemonapi.request.RequestRegister;
import com.demon.slayer.pokemonapi.response.PokemonsResponse;
import com.demon.slayer.pokemonapi.response.ResponseCreate;
import com.demon.slayer.pokemonapi.response.ResponsePokemon;
import com.demon.slayer.pokemonapi.response.ResponseUsuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	PokemonService pokemonService;
	@Autowired
	EquipoService equipoService;
	@Autowired
	TipoService tipoService;
	@Autowired
	TipoRepository tipoRepository;

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	PokemonRepository pokemonRepository;
	

	public ResponseCreate createUsuario(RequestRegister registro){
		
		String adminRole="admin";
		String provitionalRole="provisional";
		String userFromInput=registro.getUser().getRol().toString().toLowerCase();
		if(!userFromInput.equalsIgnoreCase(adminRole) && !userFromInput.equalsIgnoreCase(provitionalRole)) {
			throw new ArgumentException("please send valid data, the role is not valid must be admin or provisional");
		}
		
		if(registro.getUsuario().getRol().equals("Provisional") || registro.getUsuario().getRol().equals("provisional") ) {
			if(registro.getPokemons().size()>5) {
				throw new ProvisionalUserPkmLimitException();
			}
		}
		
		if(registro.getUsuario().getRol().equals("Admin") || registro.getUsuario().getRol().equals("admin") ) {
			if(registro.getPokemons().size()>10) {
				throw new AdminUserPkmLimitException();
			}
		}
		
		List<Tipo> type= tipoRepository.findAll();
			
			if(registro.getPokemons().size()<1) {
				throw new ArgumentException("please send valid data, must be at least 1 pokemon");
			}
			if (type.isEmpty()) {
				tipoService.agregarTipos();
			}
		if(this.findByUsuario(registro.getUsuario().getUsuario())==null && registro.getUsuario().getUsuario().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			
			if(registro.getUsuario().getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@$!%*?&_-])[A-Za-z\\d#$@$!%*?&_-]{8,}$")) {
				
				if((!pokemonService.repetidos(registro.getPokemons()))){
					if(equipoService.obtenerEquipo(registro.getEquipo().
							getNombre_equipo(), registro.getEquipo().getEntrenador())==null){
									
							for (RequestPokemon pokemon:registro.getPokemons()) {
								pokemonService.createPokemon(pokemon, registro.getEquipo());
								
								
						}
					
					}else
						throw new TrainerAlreadyExistException();
				} else {
					throw new SamePokemonException();
				}
				
				
				
				Usuario user = new Usuario();
				
				user.setUsuario(registro.getUsuario().getUsuario());
				user.setRol(registro.getUsuario().getRol());
				
				
				user.setPassword(passwordEncoder.encode(registro.getUsuario().getPassword()));
				user.setEquipo(equipoService.obtenerEquipo(registro.getEquipo()));
				
				
				usuarioRepository.save(user);
				return new ResponseCreate("Bien");
			}else {
				throw new PasswordFormatException();
			}

		}
		else {
			if (!registro.getUsuario().getUsuario().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
				throw new EmailFormatException();
			} else {

				throw new UserAlreadyExistException();
			}
			
		}
			
	}
	
	public Usuario findByUsuario(String user) {
		return usuarioRepository.findByUsuario(user).orElse(null);
	}
	
	public String requestAddPkmUsuario(RequestAddNewPkmUsuario datos, String username) {
		
		if(datos.getRol().equalsIgnoreCase("Admin")) {
			Usuario usuario = usuarioRepository.findByUsuario(username).orElseThrow(() -> new UserNotFoundException());
			List<Pokemon> pokemons = new ArrayList<>();
			
			if((!pokemonService.repetidos(datos.getPokemonList()))){
				if(datos.getPokemonList().size() + usuario.getEquipo().getPokemons().size()>10) {
					throw new AdminUserPkmLimitException();
				}
				else {					
						List<Pokemon> pkmUsuario = usuario.getEquipo().getPokemons();
						
						if(!pokemonService.repetidosUsuario(pkmUsuario, datos.getPokemonList())) {
							usuario.setEquipo(equipoService.updateEquipo(usuario.getEquipo(), datos.getEquipo()));
							datos.getPokemonList().forEach(p -> {
								List<Tipo> tipos = new ArrayList<>();						
								logger.info("nombre pokemon: "+p.getName());
								p.getTipos().forEach((t) -> {
									logger.info("buscando tipo: "+t);
									tipos.add(tipoService.findTipoByNombre(t));
								});
								
								Pokemon pokemon = pokemonService.createPokemon(p, datos.getEquipo());
								pokemon.setTipos(tipos);
								pokemon.setNombre(p.getName());
								pokemons.add(pokemon);
								
							});

							usuario.getEquipo().setPokemons(pokemons);
						
							try{
								usuarioRepository.save(usuario);
							} catch(Exception e){
								throw new UserNotFoundException();
							}
								return "Pokemons actualizados";
								
						}
					
	
					}
				
				} 
			
			}else {
				throw new SamePokemonException();
			}
			
					
		if(datos.getRol().equalsIgnoreCase("Provisional")) {
			return "Rol provisional no puede actualizar pokemons";
		} else {
			return "En la lista hay pokemons repetidos";
		}
		
	}

	public String requestUpdateUsuario(RequestUpdateUsuario datos, String username) {
		logger.info("Se llamo la funcion Request update");
		logger.info("Datos: "+datos);
		logger.info("Username: "+username);
		
		if(datos.getPokemonList().size()<1) {
			throw new ArgumentException("please send valid data, must be at least 1 pokemon");
		}
		Usuario usuario = usuarioRepository.findByUsuario(username).orElseThrow(() -> new UserNotFoundException());
		logger.info("usuario: "+usuario);
		
		String userRole = usuario.getRol().toLowerCase();
		String userRequestRole=datos.getUser().getRol().toLowerCase();
		if(!userRequestRole.equals(userRole)) {
			throw new ArgumentException("please send valid data, you cannot change the role");
		}
		
		List<Pokemon> pokemons = new ArrayList<>();
		//usuario.setRol(datos.getUser().getRol());
		usuario.setPassword(datos.getUser().getPassword());
		usuario.setPassword(passwordEncoder.encode(datos.getUser().getPassword()));
		usuario.setEquipo(equipoService.updateEquipo(usuario.getEquipo(), datos.getEquipo()));
		usuario.getEquipo().getPokemons().forEach(p -> pokemonService.deleteEquipoPokemon(p, usuario.getEquipo()));
		datos.getPokemonList().forEach(p -> {
			List<Tipo> tipos = new ArrayList<>();
			logger.info("nombre pokemon: "+p.getName());
			p.getTipos().forEach((t) -> {
				logger.info("buscando tipo: "+t);
				tipos.add(tipoService.findTipoByNombre(t));
			});
			Pokemon pokemon = pokemonService.createPokemon(p, datos.getEquipo());
			pokemon.setTipos(tipos);
			pokemon.setNombre(p.getName());
			pokemons.add(pokemon);
		});
		usuario.getEquipo().setPokemons(pokemons);
		try{
			usuarioRepository.save(usuario);
		} catch(Exception e){
			throw new UserNotFoundException();
		}
		return "Usuario actualizado exitosamente";
/*			
			logger.info("Se llamo la funcion Request update");
			logger.info("Datos: "+datos);
			logger.info("Username: "+username);
			Usuario usuario = usuarioRepository.findByUsuario(username).orElseThrow(() -> new UserNotFoundException());
			logger.info("usuario: "+usuario);
			//List<Pokemon> pokemons = new ArrayList<>();
			//usuario.setRol(datos.getUser().getRol());
			usuario.setPassword(datos.getUser().getPassword());
			usuario.setPassword(passwordEncoder.encode(datos.getUser().getPassword()));
			usuario.setEquipo(equipoService.updateEquipo(usuario.getEquipo(), datos.getEquipo()));
			//usuario.getEquipo().getPokemons().forEach(p -> pokemonService.deleteEquipoPokemon(p, usuario.getEquipo()));
			
			try{
				usuarioRepository.save(usuario);
			} catch(Exception e){
				throw new UserNotFoundException();
			}
			return "Usuario actualizado exitosamente";
*/
    }
	    
	
	public String pkmDelete(RequestDeletePkm datos, String username) {
		
		/*if(datos.getPokemonList().size()<1) {
			throw new ArgumentException("please send valid data, must be at least 1 pokemon");
		}*/
		
		Usuario usuario = usuarioRepository.findByUsuario(username).orElseThrow(() -> new UserNotFoundException());
		logger.info("usuario: "+usuario);
		
		List<Pokemon> pokemons = new ArrayList<>();
		
		usuario.setEquipo(equipoService.updateEquipo(usuario.getEquipo(), datos.getEquipo()));
		usuario.getEquipo().getPokemons().forEach(p -> pokemonService.deleteEquipoPokemon(p, usuario.getEquipo()));
		datos.getPokemonList().forEach(p -> {
			List<Tipo> tipos = new ArrayList<>();
			logger.info("nombre pokemon: "+p.getName());
			p.getTipos().forEach((t) -> {
				logger.info("buscando tipo: "+t);
				tipos.add(tipoService.findTipoByNombre(t));
			});
			Pokemon pokemon = pokemonService.createPokemon(p, datos.getEquipo());
			pokemon.setTipos(tipos);
			pokemon.setNombre(p.getName());
			pokemons.add(pokemon);
		});
		usuario.getEquipo().setPokemons(pokemons);
		try{
			usuarioRepository.save(usuario);
		} catch(Exception e){
			throw new UserNotFoundException();
		}
		
		return "Pokemon deleted"; 
	}
	    
	public PokemonsResponse pokemonesUsuario(String name) {
		Usuario user =usuarioRepository.findByUsuario(name).orElseThrow(() -> new UserNotFoundException());
		PokemonsResponse regresar=new PokemonsResponse();
		List<ResponsePokemon> pokemones =new ArrayList<>();
		for(Pokemon pokemon:pokemonService.pokemonEquipo(user.getEquipo())) {
			ResponsePokemon respuesta =new ResponsePokemon();
			respuesta.setNombre(pokemon.getNombre());
			respuesta.setId(pokemon.getIdpokemon());
			respuesta.setTipos(pokemonService.tipos(pokemon).getTipos());
			pokemones.add(respuesta);
		}
		regresar.setListaPokemons(pokemones);
		return regresar;
	}

	public ResponseUsuario buscarUsuario(String username){
		ResponseUsuario response = new ResponseUsuario(this.findByUsuario(username));
		return response;

	}
	
	    
}
