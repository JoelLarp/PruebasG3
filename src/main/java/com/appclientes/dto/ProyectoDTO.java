package com.appclientes.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.appclientes.model.Proyecto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {
	
	private Long id;
    private String nombre;
    private String descripcion;
    private Long usuarioCreadorId;
    private List<ColaboradorDTO> colaboradores;

    // Constructor para convertir un objeto Proyecto en un ProyectoDTO
    public ProyectoDTO(Proyecto proyecto) {
        this.id = proyecto.getId();
        this.nombre = proyecto.getNombre();
        this.descripcion = proyecto.getDescripcion();
        this.usuarioCreadorId = proyecto.getUsuarioCreador().getId();
        this.colaboradores = proyecto.getColaboradores() != null
                ? proyecto.getColaboradores().stream()
                    .map(usuario -> new ColaboradorDTO(usuario.getId(), usuario.getNombre()))
                    .collect(Collectors.toList())
                : new ArrayList<>(); // Si no hay colaboradores, devuelve una lista vacía
    }
}
