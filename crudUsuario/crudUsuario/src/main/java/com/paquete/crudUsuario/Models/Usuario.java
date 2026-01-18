package com.paquete.crudUsuario.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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


    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "contador_intentos", nullable = false)
    private int contadorIntentos = 0;

    @Column(name = "estado", nullable = false)
    private boolean estado = true;



    // ========================================
    // CALLBACKS DEL CICLO DE VIDA
    // ========================================

    /**
     * Se ejecuta automáticamente ANTES de insertar en BBDD.
     * Establece la fecha de creación y actualización.
     */

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Se ejecuta automáticamente ANTES de actualizar en BBDD.
     * Actualiza la fecha de última modificación.
     */
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Usuario(String nombre, String apellidos,String nombreusuario, String email, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.password = password;
    }
}
