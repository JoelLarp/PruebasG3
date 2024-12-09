package com.appclientes.service;

import java.util.List;
import java.util.Optional;

import com.appclientes.dto.TareaDTO;
import com.appclientes.enums.EstadoTarea;
import com.appclientes.enums.PrioridadTarea;
import com.appclientes.model.Tarea;

public interface TareaService {
	
	Tarea createTarea(TareaDTO tareaDTO, Long usuarioId);
    List<Tarea> getTareasByUsuario(Long usuarioId);
    List<Tarea> getTareasByProyecto(Long proyectoId, Long usuarioId);
    List<Tarea> getTareasByEstado(EstadoTarea estado, Long usuarioId);
    List<Tarea> getTareasByPrioridad(PrioridadTarea prioridad, Long usuarioId);
    Tarea updateTarea(Long tareaId, TareaDTO tareaDTO, Long usuarioId);
    void deleteTarea(Long tareaId, Long usuarioId);

}
