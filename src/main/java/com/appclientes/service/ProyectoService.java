package com.appclientes.service;

import java.util.List;
import java.util.Optional;

import com.appclientes.dto.ProyectoDTO;
import com.appclientes.model.Proyecto;
import com.appclientes.model.Usuario;

public interface ProyectoService {
    
    
 // Crear un proyecto
    ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO);
    ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO);
    void eliminarProyecto(Long id);
    List<ProyectoDTO> listarProyectosPorUsuario(Usuario usuario);
	Proyecto getProyectoById(Long proyectoId);
    
}


