package com.appclientes.dto;

import java.time.LocalDateTime;

import com.appclientes.enums.EstadoTarea;
import com.appclientes.enums.PrioridadTarea;

import lombok.Data;

@Data
public class TareaDTO {

	private Long id;
    private String descripcion;
    private EstadoTarea estado;
    private PrioridadTarea prioridad;
    private LocalDateTime fechaVencimiento;
    private Long proyectoId;
    private Long usuarioAsignadoId;
    private Long usuarioCreadorId;
    private Long usuarioId;
}
