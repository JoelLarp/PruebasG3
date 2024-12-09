package com.appclientes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appclientes.enums.EstadoTarea;
import com.appclientes.enums.PrioridadTarea;
import com.appclientes.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long>{

	List<Tarea> findByUsuarioAsignado_Id(Long usuarioId);
    List<Tarea> findByUsuarioCreador_Id(Long usuarioId);
    List<Tarea> findByProyecto_Id(Long proyectoId);
    List<Tarea> findByEstado(EstadoTarea estado);
    List<Tarea> findByPrioridad(PrioridadTarea prioridad);
    List<Tarea> findByUsuarioCreador_IdOrUsuarioAsignado_Id(Long usuarioId, Long usuarioAsignadoId);
}
