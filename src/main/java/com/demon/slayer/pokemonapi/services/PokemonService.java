package com.demon.slayer.pokemonapi.services;

import java.util.ArrayList;
import java.util.List;
import com.demon.slayer.pokemonapi.models.Equipo;
import com.demon.slayer.pokemonapi.models.Pokemon;
import com.demon.slayer.pokemonapi.models.Tipo;
import com.demon.slayer.pokemonapi.repositories.PokemonRepository;
import com.demon.slayer.pokemonapi.repositories.TipoRepository;
import com.demon.slayer.pokemonapi.request.RequestEquipo;
import com.demon.slayer.pokemonapi.request.RequestPokemon;
import com.demon.slayer.pokemonapi.response.PokemonsResponse;
import com.demon.slayer.pokemonapi.response.ResponsePokemon;
import com.demon.slayer.pokemonapi.response.ResponseTipos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PokemonService {
	

	
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
				   
				   return null;
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


}

