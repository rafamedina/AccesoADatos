package com.paquete.crudUsuario.Repositories;

import com.paquete.crudUsuario.Models.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.ArrayList;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    @Query(value = "SELECT * from usuario", nativeQuery = true)
    ArrayList<Usuario> findAll();

    @Query(value = "SELECT * from usuario where id = :id", nativeQuery = true)
    Optional<Usuario> findUsuarioById(Long id);

    boolean existsByEmail(String email);
    @Query(value = "SELECT * from usuario where email = :email", nativeQuery = true)

    Usuario getUsuarioByEmail(String email);
    Optional<Usuario> findUsuarioByNombreusuario(String nombreusuario);

    Page<Usuario> findAll(Pageable pageable);
}



