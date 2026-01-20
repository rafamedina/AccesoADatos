package com.paquete.crudUsuario.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Builder
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_rol", nullable = false, length = 100)
    private String nombreRol;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    @ToString.Exclude            // <--- IMPORTANTE
    @EqualsAndHashCode.Exclude   // <--- IMPORTANTE
    private Set<Usuario> usuarios = new HashSet<>();

}

