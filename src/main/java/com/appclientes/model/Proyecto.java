package com.appclientes.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "proyecto")
public class Proyecto {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "usuario_creador_id", nullable = false)
    @JsonManagedReference
    private Usuario usuarioCreador;

    @ManyToMany
    @JoinTable(
        name = "proyecto_colaborador",
        joinColumns = @JoinColumn(name = "proyecto_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonManagedReference
    private List<Usuario> colaboradores;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    private List<Tarea> tareas;
}
