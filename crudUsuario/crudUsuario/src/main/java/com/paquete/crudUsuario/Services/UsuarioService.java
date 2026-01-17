package com.paquete.crudUsuario.Services;

import com.paquete.crudUsuario.Models.Usuario;
import com.paquete.crudUsuario.Repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean verificarEmail(String email){
        if(StringUtils.hasText(email)){
            if(usuarioRepository.existsByEmail(email)){
                System.out.println("Ya existe un usuario con ese correo");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public Usuario crearUsuario(Usuario usuario){
    if(usuario == null) {
        throw new IllegalArgumentException("Usuario nulo");
    }
    if(verificarEmail(usuario.getEmail())){
        String passwordhashed = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordhashed);
      return usuarioRepository.save(usuario);
    } else {
        throw new IllegalStateException("El email ya existe");
    }
    }

    public boolean comprobarPassword(String password, String email){
       Usuario usuario = usuarioRepository.getUsuarioByEmail(email);
        return passwordEncoder.matches(password, usuario.getPassword());
    }

    public Usuario obtenerUsuarioEmail(String email){
        Usuario usuario = usuarioRepository.getUsuarioByEmail(email);
        return usuario;
    }

    @Transactional
    public boolean eliminarUsuario(Long id){
        return usuarioRepository.deleteUsuarioById(id);
    }

    public ArrayList<Usuario> mostrarUsuarios(){
        return usuarioRepository.searchAll();
    }

    public Optional<Usuario> mostarUsuarioId(Long id){
        return usuarioRepository.searchUsuarioById(id);
    }


    @Transactional
    public Usuario actualizarUsuario(Usuario usuario){

        if(usuario == null) throw new IllegalArgumentException("Usuario nulo");

        Optional<Usuario> existe = usuarioRepository.searchUsuarioById(usuario.getId());

        if(existe.isEmpty()) throw new IllegalStateException("El usuario no existe");

        return usuarioRepository.save(usuario);

    }

    public boolean existeEmail(String email){
    return usuarioRepository.existsByEmail(email);
    }

}
