package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name ="equipos")
public class Equipo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long idequipo;
	
	@Column(name="entrenador")
	private String entrenador; 
	
	@Column(name="nombreEquipo")
	private String nombreequipo;
	

}
