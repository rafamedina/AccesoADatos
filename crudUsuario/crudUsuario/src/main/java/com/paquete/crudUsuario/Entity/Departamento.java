package com.paquete.crudUsuario.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departamentos")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_departamento", nullable = false, length = 100)
    private String nombreDepartamento;

    // RELACIÓN UNO A MUCHOS
    // mappedBy = "departamento" -> Se refiere al nombre del atributo en la clase Usuario
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude // IMPORTANTE: Evita bucles infinitos al imprimir logs
    private List<Usuario> usuarios = new ArrayList<>();

    // Ayuda a mantener la coherencia en ambos lados de la relación
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        usuario.setDepartamento(this);
    }

    public void removerUsuario(Usuario usuario) {
        usuarios.remove(usuario);
        usuario.setDepartamento(null);
    }
}
