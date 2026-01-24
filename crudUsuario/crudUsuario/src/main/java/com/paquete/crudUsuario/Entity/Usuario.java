package com.paquete.crudUsuario.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "usuario")
@Builder
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
    @JsonFormat(pattern = "dd,MM,yyyy")
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_usuarios",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Roles> roles = new HashSet<>();

    @ToString.Exclude            // <--- IMPORTANTE
    @EqualsAndHashCode.Exclude   // <--- IMPORTANTE
    @ManyToOne(fetch = FetchType.LAZY) // Lazy carga el departamento solo cuando lo pides
    @JoinColumn(name = "id_departamento") // ESTA ES LA CLAVE FORÁNEA EN TU TABLA
    private Departamento departamento;

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


    public Usuario(Long id, String nombre, String apellidos, String nombreusuario, String email, boolean activo, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
}
