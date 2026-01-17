package com.paquete.crudUsuario.DTO;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSesionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String nombre;
    private String email;
}
