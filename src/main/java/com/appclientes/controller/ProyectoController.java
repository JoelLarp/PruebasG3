package com.appclientes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appclientes.dto.ProyectoDTO;
import com.appclientes.model.Proyecto;
import com.appclientes.model.Usuario;

import com.appclientes.repository.UsuarioRepository;
import com.appclientes.service.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
	
	@Autowired
    private ProyectoService proyectoService;
	
	@Autowired
    private UsuarioRepository usuarioRepository; 
	
	/*
	 * @PostMapping("/crear") public ResponseEntity<ProyectoDTO>
	 * crearProyecto(@RequestBody ProyectoDTO proyectoDTO) { // Llama al servicio
	 * para crear el proyecto y devuelve la respuesta ProyectoDTO proyectoCreado =
	 * proyectoService.crearProyecto(proyectoDTO); return
	 * ResponseEntity.ok(proyectoCreado); }
	 */
	
	@PostMapping("/crear")
	public ResponseEntity<ProyectoDTO> crearProyecto(@RequestBody @Valid ProyectoDTO proyectoDTO) {
	    // Llama al servicio para crear el proyecto y devuelve la respuesta
	    ProyectoDTO proyectoCreado = proyectoService.crearProyecto(proyectoDTO);
	    return ResponseEntity.status(HttpStatus.CREATED).body(proyectoCreado);
	}
	
	@GetMapping("/listar")
    public ResponseEntity<List<ProyectoDTO>> listarProyectos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : null;

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<ProyectoDTO> proyectos = proyectoService.listarProyectosPorUsuario(usuarioAutenticado);
        return ResponseEntity.ok(proyectos);
    }

	@PutMapping("/actualizar/{id}")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@PathVariable Long id, @RequestBody @Valid ProyectoDTO proyectoDTO) {
        // Llama al servicio para actualizar el proyecto y devuelve la respuesta
        ProyectoDTO proyectoActualizado = proyectoService.actualizarProyecto(id, proyectoDTO);
        return ResponseEntity.ok(proyectoActualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        // Llama al servicio para eliminar un proyecto y devuelve una respuesta de éxito
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }

    

    

  

    }
