package com.appclientes.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appclientes.dto.TareaDTO;
import com.appclientes.enums.EstadoTarea;
import com.appclientes.enums.PrioridadTarea;
import com.appclientes.model.Tarea;
import com.appclientes.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

	@Autowired
    private TareaService tareaService;

	@PostMapping("/crear")
    public ResponseEntity<Tarea> createTarea(@RequestBody @Valid TareaDTO tareaDTO, Principal principal) {
        try {
            // Obtén el ID del usuario desde el contexto de seguridad
            Long usuarioId = Long.parseLong(principal.getName()); // Supone que el nombre del principal es el ID del usuario

            Tarea tareaCreada = tareaService.createTarea(tareaDTO, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(tareaCreada);
        } catch (Exception e) {
            // Manejo de errores más específico si es necesario
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("/usuario/{usuarioId}")
    public List<Tarea> getTareasByUsuario(@PathVariable Long usuarioId) {
        return tareaService.getTareasByUsuario(usuarioId);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public List<Tarea> getTareasByProyecto(@PathVariable Long proyectoId, @RequestParam Long usuarioId) {
        return tareaService.getTareasByProyecto(proyectoId, usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Tarea> getTareasByEstado(@PathVariable String estado, @RequestParam Long usuarioId) {
        return tareaService.getTareasByEstado(EstadoTarea.valueOf(estado), usuarioId);
    }

    @GetMapping("/prioridad/{prioridad}")
    public List<Tarea> getTareasByPrioridad(@PathVariable String prioridad, @RequestParam Long usuarioId) {
        return tareaService.getTareasByPrioridad(PrioridadTarea.valueOf(prioridad), usuarioId);
    }

    @PutMapping("/actualizar/{tareaId}")
    public Tarea updateTarea(@PathVariable Long tareaId, @RequestBody TareaDTO tareaDTO, @RequestParam Long usuarioId) {
        return tareaService.updateTarea(tareaId, tareaDTO, usuarioId);
    }

    @DeleteMapping("/eliminar/{tareaId}")
    public void deleteTarea(@PathVariable Long tareaId, @RequestParam Long usuarioId) {
        tareaService.deleteTarea(tareaId, usuarioId);
    }

}
