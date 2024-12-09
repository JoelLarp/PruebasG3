package com.appclientes.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;


@Data
@Entity
@Table (name ="usuario")
public class Usuario {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false, length = 50)
    private String nombre;
	
	@Column(nullable = false, length = 50, unique = true)
    private String email;
	
	@Column(nullable = false, length = 255)
    private String password;
	
	private String dni;
	
	@OneToMany(mappedBy = "usuarioCreador", cascade = CascadeType.ALL)
	@JsonBackReference
    private List<Proyecto> proyectosCreados;

    @ManyToMany(mappedBy = "colaboradores")
    @JsonBackReference
    private List<Proyecto> proyectosColaborados;

    @OneToMany(mappedBy = "usuarioAsignado", cascade = CascadeType.ALL)
    private List<Tarea> tareasAsignadas;

}
