package com.paquete.crudUsuario.DTO;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSesionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
//    private String password;
    private String rol;
    private LocalDateTime fechaCreacion;
    private boolean estado;

    public UsuarioSesionDTO(Long id, String nombre, String apellidos, String username, String email, String rol, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
    }
}

