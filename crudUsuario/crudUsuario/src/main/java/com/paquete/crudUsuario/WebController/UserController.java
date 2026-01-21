package com.paquete.crudUsuario.WebController;


import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Entity.Usuario;
import com.paquete.crudUsuario.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UsuarioService usuarioService;

    @ResponseBody
    @GetMapping("/list") // Puedes cambiar la ruta a "/perfil" si quieres, pero "/list" funciona igual
    public ResponseEntity<UsuarioSesionDTO> pedirUsuario(HttpSession session) {

        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // 1. Si no hay sesión, error 401
        if (usuarioSesionDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

        try {
            Usuario usuario = usuarioService.obtenerUsuarioEmail(usuarioSesionDTO.getEmail());
            UsuarioSesionDTO usuarioMostrar = new UsuarioSesionDTO(usuario.getId(), usuario.getNombre(), usuario.getApellidos(), usuario.getNombreusuario(), usuario.getEmail(), usuario.getRoles().iterator().next().getNombreRol(),usuario.getFechaCreacion(), usuario.isActivo());

            return ResponseEntity.ok(usuarioMostrar);

        } catch (Exception e) {
            System.out.println("Error recuperando usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Borramos el return del final que causaba el 424, ya no hace falta
    }









}
