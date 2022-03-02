package com.demon.slayer.pokemonapi.services;

import java.util.ArrayList;
import java.util.List;

import com.demon.slayer.pokemonapi.exceptions.NoPokemonException;
import com.demon.slayer.pokemonapi.exceptions.NoPokemonNameException;
import com.demon.slayer.pokemonapi.exceptions.ResponseException;
import com.demon.slayer.pokemonapi.exceptions.SamePokemonException;
import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Pokemon;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.demon.slayer.pokemonapi.models.Usuario;
import com.demon.slayer.pokemonapi.repositories.PokemonRepository;
import com.demon.slayer.pokemonapi.repositories.TipoRepository;
import com.demon.slayer.pokemonapi.repositories.UsuarioRepository;
import com.demon.slayer.pokemonapi.request.RequestEquipo;
import com.demon.slayer.pokemonapi.request.RequestPokemon;
import com.demon.slayer.pokemonapi.response.PokemonsResponse;
import com.demon.slayer.pokemonapi.response.ResponsePokemon;
import com.demon.slayer.pokemonapi.response.ResponseTipos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



@Service
public class PokemonService {
	
    @Autowired
    UsuarioRepository userRepo;
	
	@Autowired 
	PokemonRepository pokemonRepository;

   @Autowired 
	TipoRepository tipoRepository;
   @Autowired
   TipoService tipoService;
  
   @Autowired
   EquipoService equipoService;
   
   public Pokemon createPokemon(RequestPokemon req,RequestEquipo reqE) {
	   Pokemon pokemon = new Pokemon();
	   if(this.pokemonIgual(req)!=0) {
		   pokemon=this.pokemonId(this.pokemonIgual(req));
		  equipoService.createEquipo(reqE);
		  Equipo eq= equipoService.obtenerEquipo(reqE.getNombre_equipo(),reqE.getEntrenador());
		  List<Equipo> equipos = new ArrayList<Equipo>();
		  equipos=pokemon.getEquipos();
		  equipos.add(eq);
		  pokemon.setEquipos(equipos);
		   return pokemon;
		   
	   }else {
		   		if(req.getName()==null || req.getName().isBlank()) {
		   			throw new NoPokemonNameException();
		   		}
			   pokemon.setNombre(req.getName());
			   pokemon.setStatus(1);
			   try {
				List<Tipo> tipos = new ArrayList<Tipo>();
				for (String tipo:req.getTipos()) {
					Tipo type = tipoService.findTipoByNombre(tipo);
					tipos.add(type);
				}
				
				equipoService.createEquipo(reqE);
				Equipo eq= equipoService.obtenerEquipo(reqE.getNombre_equipo(),reqE.getEntrenador());
				List<Equipo> equipos = new ArrayList<Equipo>();
				equipos.add(eq);
				pokemon.setTipos(tipos);
				pokemon.setEquipos(equipos);
				return pokemonRepository.save(pokemon);
			   }catch(Exception e) {
				   
				   throw new ResponseException("Error con pokemons", e.getStackTrace().toString(), "Error con pokemon", HttpStatus.INTERNAL_SERVER_ERROR);
			   }
	   }
		   
   }
   
   
   public PokemonsResponse obtenerPokemon(String name) {
	   PokemonsResponse regresar=new PokemonsResponse();
		  List<ResponsePokemon> pokemones =new ArrayList<ResponsePokemon>();
		  for(Pokemon pokemon:pokemonRepository.findByNombre(name)) {
			  ResponsePokemon respuesta =new ResponsePokemon();
			  respuesta.setId(pokemon.getIdpokemon());
			  respuesta.setNombre(pokemon.getNombre());

			  respuesta.setTipos(this.tipos(pokemon).getTipos());
			  pokemones.add(respuesta);
		  }
		  regresar.setListaPokemons(pokemones);
		  return regresar;
	   
   }
   
   public Pokemon pokemonId(long id) {
	   return pokemonRepository.findById(id);
   }
   
   public Long pokemonIgual(RequestPokemon comparar) {
	   PokemonsResponse regresar=new PokemonsResponse();
	   regresar.setListaPokemons(this.obtenerPokemon(comparar.getName()).getListaPokemons());
	   for(ResponsePokemon pokemon:regresar.getListaPokemons()) {
		   if(pokemon.getNombre().equals(comparar.getName())&&
				   this.arreglosIguales(pokemon.getTipos(), comparar.getTipos()))
			   return (pokemon.getId());
	   }
	   return (long)0;
	   
   }
   
   public boolean arreglosIguales(List<String>arreglo1, List<String>arreglo2) {
	   if(arreglo1.size()==arreglo2.size()) {
		   for(String valorar:arreglo1) {
			   if(!(arreglo2.contains(valorar)))
				   return false;
		   }
		   return true;
	   }
	   else
		   return false;
   }
   
   public List<Pokemon> pokemonEquipo(Equipo e){
	   List<Pokemon> listaPokemons=pokemonRepository.findAll();
	   List<Pokemon> pokemonesEquipo=new ArrayList<Pokemon>();
	   for(Pokemon pokemon:listaPokemons) {
		   if(pokemon.getEquipos().contains(e)) {
		  pokemonesEquipo.add(pokemon);
		   }
	   }
	   
	   return pokemonesEquipo;
   }
   public List<Tipo> tipoPkemono(Pokemon p){
	   List<Tipo> listaTipo=tipoRepository.findAll();
	   List<Tipo> tiposPokemon=new ArrayList<Tipo>();
	   for(Tipo tipo:listaTipo) {
		   if(tipo.getPokemons().contains(p)) {
		  tiposPokemon.add(tipo);
		   }
	   }
	   
	   return tiposPokemon;
   }
   
   public ResponseTipos tipos(Pokemon p) {
		  List<String> nombreTipos=new ArrayList<String>();
		  ResponseTipos respuesta=new ResponseTipos();
		  List<Tipo> types =this.tipoPkemono(p);
		  for(Tipo tipo:types)
			  nombreTipos.add(tipo.getNombretipo());
		  respuesta.setTipos(nombreTipos);
		  return respuesta;
	  }

	public void deleteEquipoPokemon(Pokemon pokemon, Equipo equipo){
		pokemon.getEquipos().remove(equipo);
		pokemonRepository.save(pokemon);
	}
	
	public boolean repetidos(List<RequestPokemon>pokemons) {
		List<Integer>repetidos=new ArrayList();
		Integer numero=0 ;
		if(pokemons == null || pokemons.isEmpty()) {
			throw new NoPokemonException();
		}
		for (int i=0; i<pokemons.size();i++) {
			repetidos.add(0);
			for(int j=0; j<pokemons.size();j++) {
				if(pokemons.get(i).getName().equals(pokemons.get(j).getName())) {
					numero=repetidos.get(i);
					repetidos.set(i, ++numero);
				}
			}
		}
		for(Integer number:repetidos)
			if(number!=1)
				return true;
			return false;
	}
	
	public boolean repetidosUsuario(List<Pokemon>pokemons, List<RequestPokemon> req) {
		if(pokemons == null || pokemons.isEmpty()) {
			throw new NoPokemonException();
		}
		
		for (int i=0; i<req.size();i++) {			
			for(int j=0; j<pokemons.size();j++) {
				if(req.get(i).getName().equals(pokemons.get(j).getNombre())) {
					throw new SamePokemonException(); 
				}
			}
		}
		
		return false; 

	}

	
	
}

