package com.appclientes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.appclientes.dto.ProyectoDTO;
import com.appclientes.model.Proyecto;
import com.appclientes.model.Usuario;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long>{

	List<Proyecto> findByUsuarioCreadorId(Long usuarioId);
    List<Proyecto> findByColaboradoresId(Long usuarioId);
    
    
    @Query("SELECT p FROM Proyecto p WHERE p.usuarioCreador = :usuario OR :usuario MEMBER OF p.colaboradores")
    List<ProyectoDTO> findByUsuarioCreadorOrColaboradores(@Param("usuario") Usuario usuario);

}
