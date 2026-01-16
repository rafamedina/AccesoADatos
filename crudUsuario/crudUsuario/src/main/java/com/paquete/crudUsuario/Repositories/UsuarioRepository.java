package com.paquete.crudUsuario.Repositories;

import com.paquete.crudUsuario.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    boolean findByEmail(String email);


    boolean deleteUsuarioById(Long id);


    @Query(value = "SELECT * from usuario", nativeQuery = true)
    ArrayList<Usuario> searchAll();

    Optional<Usuario> searchUsuarioById(Long id);

    boolean searchUsuarioByEmail(String email);
    Usuario getUsuarioByEmail(String email);
}



