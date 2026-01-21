package com.paquete.crudUsuario.WebController;


import com.paquete.crudUsuario.DTO.LoginRequest;
import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Entity.Usuario;
import com.paquete.crudUsuario.Services.RolesService;
import com.paquete.crudUsuario.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolesService rolesService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UsuarioSesionDTO> inicioSesion(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(loginRequest.getEmail());
        if (usuarioOpt.isPresent()) {
            if (passwordEncoder.matches(loginRequest.getPassword(), usuarioOpt.get().getPassword())) {
                String rol = usuarioOpt.get().getRoles().iterator().next().getNombreRol();
                UsuarioSesionDTO usuarioInfo = new UsuarioSesionDTO(usuarioOpt.get().getId(),
                        usuarioOpt.get().getNombre(),usuarioOpt.get().getApellidos(), usuarioOpt.get().getNombreusuario(),
                        usuarioOpt.get().getEmail(), rol, usuarioOpt.get().getFechaCreacion());

                session.setAttribute("usuarioLogueado", usuarioInfo);

                return ResponseEntity.ok(usuarioInfo);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/killSession" )
    public String matarSesion( HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
