package com.paquete.crudUsuario.Repositories;

import com.paquete.crudUsuario.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean deleteUsuarioById(String email);
    @Query(value = "SELECT * from usuario", nativeQuery = true)
    ArrayList<Usuario> searchAll();
    @Query(value = "SELECT * from usuario where id = :id", nativeQuery = true)
    Optional<Usuario> searchUsuarioById(Long id);
    boolean existsByEmail(String email);
    @Query(value = "SELECT * from usuario where email = :email", nativeQuery = true)
    Usuario getUsuarioByEmail(String email);
    Optional<Usuario> searchUsuarioByNombreusuario(String nombreusuario);
}



