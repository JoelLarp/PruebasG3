package com.appclientes.dto;

import com.appclientes.model.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
	
	private Long id;
    private String nombre;
    private String email;
    private String password;
    private String dni;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
    }

}
