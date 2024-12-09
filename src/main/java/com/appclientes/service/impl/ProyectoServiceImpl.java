package com.appclientes.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.appclientes.dto.ProyectoDTO;
import com.appclientes.dto.UsuarioDTO;
import com.appclientes.dto.ColaboradorDTO;
import com.appclientes.model.Proyecto;
import com.appclientes.model.Usuario;
import com.appclientes.repository.ProyectoRepository;
import com.appclientes.repository.UsuarioRepository;
import com.appclientes.security.UserDetailsImpl;
import com.appclientes.service.ProyectoService;

@Service
public class ProyectoServiceImpl implements ProyectoService{
	
	@Autowired
    private ProyectoRepository proyectoRepository;
	
	@Autowired
    private UsuarioRepository usuarioRepository;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	
	@Override
    public ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO) {
        // Obtener el email del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : null;

        if (email == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Obtener el usuario autenticado de la base de datos
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear la entidad de Proyecto y asociar el usuario
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(proyectoDTO.getNombre());
        proyecto.setDescripcion(proyectoDTO.getDescripcion());
        proyecto.setUsuarioCreador(usuarioAutenticado);

        // Agregar los colaboradores al proyecto si se proporciona una lista válida
        if (proyectoDTO.getColaboradores() != null && !proyectoDTO.getColaboradores().isEmpty()) {
            List<Long> colaboradorIds = proyectoDTO.getColaboradores().stream()
                    .map(ColaboradorDTO::getId)
                    .collect(Collectors.toList());

            List<Usuario> colaboradores = usuarioRepository.findAllById(colaboradorIds);

            if (colaboradores.size() != colaboradorIds.size()) {
                throw new RuntimeException("Algunos colaboradores no existen en la base de datos.");
            }

            proyecto.setColaboradores(colaboradores);
        }

        // Guardar el proyecto en la base de datos
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        // Convertir a DTO y devolver
        return new ProyectoDTO(proyectoGuardado);
    }
	
	
	@Override
    public List<ProyectoDTO> listarProyectosPorUsuario(Usuario usuario) {
		return proyectoRepository.findByUsuarioCreadorOrColaboradores(usuario);
    }
	
	
	public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : null;

        if (email == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Obtener el usuario autenticado de la base de datos
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el proyecto existe y si el usuario es el creador del proyecto
        Optional<Proyecto> proyectoExistente = proyectoRepository.findById(id);
        if (proyectoExistente.isPresent()) {
            Proyecto proyecto = proyectoExistente.get();
            if (!proyecto.getUsuarioCreador().equals(usuarioAutenticado)) {
                throw new RuntimeException("No tienes permiso para actualizar este proyecto");
            }

            // Actualizar los detalles del proyecto
            proyecto.setNombre(proyectoDTO.getNombre());
            proyecto.setDescripcion(proyectoDTO.getDescripcion());

            // Actualizar los colaboradores
            if (proyectoDTO.getColaboradores() != null && !proyectoDTO.getColaboradores().isEmpty()) {
                List<Long> colaboradorIds = proyectoDTO.getColaboradores().stream()
                        .map(ColaboradorDTO::getId)
                        .collect(Collectors.toList());

                List<Usuario> colaboradores = usuarioRepository.findAllById(colaboradorIds);

                if (colaboradores.size() != colaboradorIds.size()) {
                    throw new RuntimeException("Algunos colaboradores no existen en la base de datos.");
                }

                proyecto.setColaboradores(colaboradores);
            }

            // Guardar el proyecto actualizado en la base de datos
            Proyecto proyectoActualizado = proyectoRepository.save(proyecto);

            // Convertir a DTO y devolver
            return new ProyectoDTO(proyectoActualizado);
        } else {
            throw new RuntimeException("Proyecto no encontrado");
        }
    }
	
	
	
	public void eliminarProyecto(Long id) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : null;

        if (email == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Obtener el usuario autenticado de la base de datos
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si el proyecto existe y si el usuario es el creador del proyecto
        Optional<Proyecto> proyectoExistente = proyectoRepository.findById(id);
        if (proyectoExistente.isPresent()) {
            Proyecto proyecto = proyectoExistente.get();
            if (!proyecto.getUsuarioCreador().equals(usuarioAutenticado)) {
                throw new RuntimeException("No tienes permiso para eliminar este proyecto");
            }

            // Eliminar el proyecto de la base de datos
            proyectoRepository.delete(proyecto);
        } else {
            throw new RuntimeException("Proyecto no encontrado");
        }
    }


	@Override
	public Proyecto getProyectoById(Long proyectoId) {
		// Obtener el usuario autenticado
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication != null ? authentication.getName() : null;

	    if (email == null) {
	        throw new RuntimeException("Usuario no autenticado");
	    }

	    // Obtener el usuario autenticado de la base de datos
	    Usuario usuarioAutenticado = usuarioRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    // Buscar el proyecto por ID
	    Proyecto proyecto = proyectoRepository.findById(proyectoId)
	            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

	    // Verificar si el usuario autenticado es el creador o un colaborador del proyecto
	    if (!proyecto.getUsuarioCreador().equals(usuarioAutenticado) &&
	        proyecto.getColaboradores().stream().noneMatch(colaborador -> colaborador.equals(usuarioAutenticado))) {
	        throw new RuntimeException("No tienes permiso para acceder a este proyecto");
	    }

	    return proyecto;
	}
		

}
