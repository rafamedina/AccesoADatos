package com.paquete.crudUsuario.Services;

import com.paquete.crudUsuario.Models.Usuario;
import com.paquete.crudUsuario.Repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
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

    @Transactional
    public void ultimoLogin(Usuario usuario){
        usuario.setContadorIntentos(0);
      usuarioRepository.save(usuario);
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

    public boolean comprobarPasswordCambioContador(String password, String email){
        Usuario usuario = usuarioRepository.getUsuarioByEmail(email);
        if( passwordEncoder.matches(password, usuario.getPassword())){
            return true;
        } else{
           int nuevofallos = usuario.getContadorIntentos() + 1 ;
           usuario.setContadorIntentos(nuevofallos);
           if(usuario.getContadorIntentos()==3){
               usuario.setEstado(false);
           }
           usuarioRepository.save(usuario);
           return false;
        }
    }


    public Usuario obtenerUsuarioEmail(String email){
        Usuario usuario = usuarioRepository.getUsuarioByEmail(email);
        return usuario;
    }

    @Transactional
    public void eliminarUsuario(Usuario usuario)
    {
         usuarioRepository.delete(usuario);
    }

    public ArrayList<Usuario> mostrarUsuarios(){
        return usuarioRepository.findAll();
    }



    @Transactional
    public Usuario actualizarUsuario(Usuario usuario){

        if(usuario == null) throw new IllegalArgumentException("Usuario nulo");

        Optional<Usuario> existe = usuarioRepository.findUsuarioById(usuario.getId());

        if(existe.isEmpty()) throw new IllegalStateException("El usuario no existe");

        if (usuario.getPassword().equals(existe.get().getPassword())) {
            return usuarioRepository.save(usuario);
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);

    }

    public boolean existeEmail(String email){
    return usuarioRepository.existsByEmail(email);
    }

    public Optional<Usuario> buscarPorUsername(String username){
        if(StringUtils.hasText(username)){
            Optional<Usuario> usuario = usuarioRepository.findUsuarioByNombreusuario(username);

            return usuario;
        } else {
            throw new IllegalArgumentException("username no introducido");
        }

    }

    public Usuario desactivarUsuario(String email){
        Usuario usuario = null;
        if(StringUtils.hasText(email)){
            if(!existeEmail(email)){
                throw new IllegalStateException("El correo no existe");
            }
             usuario = obtenerUsuarioEmail(email);
            if(usuario.isActivo()){
                usuario.setActivo(false);
            } else {
                throw new IllegalStateException("El usuario ya esta inactivo");
            }
        }
        return  usuarioRepository.save(usuario);
    }


    public boolean cambioDePassword(String passwordNueva, String correo){
        Usuario usuario = usuarioRepository.getUsuarioByEmail(correo);
        String viejaPass = usuario.getPassword();
       usuario.setPassword(passwordEncoder.encode(passwordNueva));
        String nuevaPassHash = usuario.getPassword();
        usuarioRepository.save(usuario);

        if(viejaPass.equals(nuevaPassHash)){
            return false;
        } else {
            return true;
        }

    }


    public Page<Usuario> listarUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }



}
