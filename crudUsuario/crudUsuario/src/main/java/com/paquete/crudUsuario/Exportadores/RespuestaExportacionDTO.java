package com.paquete.crudUsuario.Exportadores;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Entity.Usuario;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class RespuestaExportacionDTO {
    // Estos campos serán las claves del JSON
    private Map<String, String> informacion;
    private List<UsuarioSesionDTO> usuarios;
    private Object resumenGlobal; // Puede ser un objeto vacío o null

    // Constructor de ayuda
    public RespuestaExportacionDTO(List<UsuarioSesionDTO> usuarios) {
        this.usuarios = usuarios;
        this.informacion = Map.of(
            "nombre", "Usuarios_Registrados",
            "fecha", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        this.resumenGlobal = Collections.emptyMap();
    }
}