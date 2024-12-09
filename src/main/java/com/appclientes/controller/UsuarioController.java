package com.appclientes.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.appclientes.dto.UsuarioDTO;
import com.appclientes.service.UsuarioService;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	
	@Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<Object> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            return new ResponseEntity<>(
                    usuarioService.registrarUsuario(usuarioDTO), HttpStatus.CREATED
            );
        } catch (IllegalArgumentException e) {
            // Devuelve un mensaje de error con código 400
            ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/welcome") // Endpoint específico
    public ResponseEntity<Object> bienvenido() {
        return ResponseEntity.ok("Bienvenido al sistema!");
    }

    // Clase estática para la estructura de respuesta de error
    public static class ErrorResponse {
        private String errorType;
        private String message;

        public ErrorResponse(String errorType, String message) {
            this.errorType = errorType;
            this.message = message;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
