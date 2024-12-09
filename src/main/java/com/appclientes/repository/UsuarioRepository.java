package com.appclientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appclientes.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByNombre(String nombre);
	  boolean existsByNombre(String nombre);
	    boolean existsByEmail(String email);
	    boolean existsByDni(String dni);
	
}
