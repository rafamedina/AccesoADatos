package com.paquete.crudUsuario.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "usuario")
public class Usuario {



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellidos", length = 200)
    private String apellidos;

    @Column (name = "nombre_usuario", length = 100, nullable = false, unique = true)
    private String nombreusuario;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password",nullable = false, length = 200)
    private String password;

    @Column(name = "activo")
    private boolean activo = true;

    public Usuario(String nombre, String apellidos,String nombreusuario, String email, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.password = password;
    }
}
