package com.appclientes.model;

import javax.persistence.*;

import com.appclientes.enums.EstadoTarea;
import com.appclientes.enums.PrioridadTarea;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tarea")
public class Tarea {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;// PENDIENTE, EN_PROGRESO, COMPLETADA
    
    @Enumerated(EnumType.STRING)
    private PrioridadTarea prioridad; // ALTA, BAJA
    
    private LocalDateTime fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "usuario_asignado_id")
    private Usuario usuarioAsignado;

    @ManyToOne
    @JoinColumn(name = "usuario_creador_id", nullable = false)
    private Usuario usuarioCreador;

}
