package com.appclientes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appclientes.dto.TareaDTO;
import com.appclientes.enums.EstadoTarea;
import com.appclientes.enums.PrioridadTarea;
import com.appclientes.model.Proyecto;
import com.appclientes.model.Tarea;
import com.appclientes.model.Usuario;
import com.appclientes.repository.ProyectoRepository;
import com.appclientes.repository.TareaRepository;
import com.appclientes.repository.UsuarioRepository;
import com.appclientes.service.ProyectoService;
import com.appclientes.service.TareaService;
import com.appclientes.service.UsuarioService;

@Service
public class TareaServiceImpl implements TareaService{
	
	 @Autowired
	    private TareaRepository tareaRepository;

	    @Autowired
	    private UsuarioService usuarioService;

	    @Autowired
	    private ProyectoService proyectoService;
	    
	    @Autowired
	    private UsuarioRepository usuarioRepository;
	    
	    @Autowired
	    private ProyectoRepository proyectoRepository;

	    @Override
	    public Tarea createTarea(TareaDTO tareaDTO, Long usuarioId) {
	        // Validar que el usuario que intenta crear la tarea exista en la base de datos
	        Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	        // Buscar el proyecto al que se quiere asignar la tarea
	        Proyecto proyecto = proyectoRepository.findById(tareaDTO.getProyectoId())
	            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

	        // Verificar si el usuario es el creador del proyecto o un colaborador
	        if (!proyecto.getUsuarioCreador().equals(usuario) && !proyecto.getColaboradores().contains(usuario)) {
	            throw new RuntimeException("El usuario no tiene permisos para crear tareas en este proyecto.");
	        }

	        // Crear la tarea a partir del DTO
	        Tarea tarea = new Tarea();
	        tarea.setDescripcion(tareaDTO.getDescripcion());
	        tarea.setEstado(tareaDTO.getEstado());
	        tarea.setPrioridad(tareaDTO.getPrioridad());
	        tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());
	        tarea.setProyecto(proyecto);

	        // Asignar al usuario como creador de la tarea si es el creador del proyecto
	        // Si es un colaborador, asignar la tarea solo a él mismo
	        if (proyecto.getUsuarioCreador().equals(usuario)) {
	            tarea.setUsuarioCreador(usuario);
	            if (tareaDTO.getUsuarioAsignadoId() != null) {
	                Usuario usuarioAsignado = usuarioRepository.findById(tareaDTO.getUsuarioAsignadoId())
	                    .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));
	                tarea.setUsuarioAsignado(usuarioAsignado);
	            } else {
	                // Si el creador no asigna otro usuario, la tarea es solo de él
	                tarea.setUsuarioAsignado(usuario);
	            }
	        } else {
	            // Si es colaborador, solo puede asignarse la tarea a sí mismo
	            tarea.setUsuarioCreador(usuario);
	            tarea.setUsuarioAsignado(usuario);
	        }

	        // Guardar la tarea en la base de datos
	        return tareaRepository.save(tarea);
	    }


	    @Override
	    public List<Tarea> getTareasByUsuario(Long usuarioId) {
	        return tareaRepository.findByUsuarioCreador_IdOrUsuarioAsignado_Id(usuarioId, usuarioId);
	    }

	    @Override
	    public List<Tarea> getTareasByProyecto(Long proyectoId, Long usuarioId) {
	        Proyecto proyecto = proyectoService.getProyectoById(proyectoId);
	        Usuario usuario = usuarioService.getUsuarioById(usuarioId);

	        if (!proyecto.getUsuarioCreador().equals(usuario) &&
	            proyecto.getColaboradores().stream().noneMatch(col -> col.equals(usuario))) {
	            throw new IllegalArgumentException("El usuario no tiene permiso para ver las tareas de este proyecto.");
	        }

	        return tareaRepository.findByProyecto_Id(proyectoId);
	    }

	    @Override
	    public List<Tarea> getTareasByEstado(EstadoTarea estado, Long usuarioId) {
	        return tareaRepository.findByEstado(estado);
	    }

	    @Override
	    public List<Tarea> getTareasByPrioridad(PrioridadTarea prioridad, Long usuarioId) {
	        return tareaRepository.findByPrioridad(prioridad);
	    }

	    @Override
	    public Tarea updateTarea(Long tareaId, TareaDTO tareaDTO, Long usuarioId) {
	        Optional<Tarea> optionalTarea = tareaRepository.findById(tareaId);
	        if (optionalTarea.isPresent()) {
	            Tarea tarea = optionalTarea.get();
	            Usuario usuario = usuarioService.getUsuarioById(usuarioId);

	            // Verifica que el usuario que actualiza sea el creador o el asignado de la tarea
	            if (!tarea.getUsuarioCreador().equals(usuario) && 
	                (tarea.getUsuarioAsignado() == null || !tarea.getUsuarioAsignado().equals(usuario))) {
	                throw new IllegalArgumentException("El usuario no tiene permiso para actualizar esta tarea.");
	            }

	            tarea.setDescripcion(tareaDTO.getDescripcion());
	            tarea.setEstado(tareaDTO.getEstado());
	            tarea.setPrioridad(tareaDTO.getPrioridad());
	            tarea.setFechaVencimiento(tareaDTO.getFechaVencimiento());

	            return tareaRepository.save(tarea);
	        } else {
	            throw new IllegalArgumentException("Tarea no encontrada.");
	        }
	    }

	    @Override
	    public void deleteTarea(Long tareaId, Long usuarioId) {
	        Optional<Tarea> optionalTarea = tareaRepository.findById(tareaId);
	        if (optionalTarea.isPresent()) {
	            Tarea tarea = optionalTarea.get();
	            Usuario usuario = usuarioService.getUsuarioById(usuarioId);

	            // Verifica que el usuario que elimina sea el creador o el asignado de la tarea
	            if (!tarea.getUsuarioCreador().equals(usuario) && 
	                (tarea.getUsuarioAsignado() == null || !tarea.getUsuarioAsignado().equals(usuario))) {
	                throw new IllegalArgumentException("El usuario no tiene permiso para eliminar esta tarea.");
	            }

	            tareaRepository.delete(tarea);
	        } else {
	            throw new IllegalArgumentException("Tarea no encontrada.");
	        }
	    }

}
