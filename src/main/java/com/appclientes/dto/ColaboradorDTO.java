package com.appclientes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColaboradorDTO {
	private Long id;
    private String nombre;

    // Constructor adicional para facilitar la creación de instancias con id y nombre
    public ColaboradorDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
