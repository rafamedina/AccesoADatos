package com.paquete.crudUsuario.WebController;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Entity.Usuario;
import com.paquete.crudUsuario.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping()
    public String validarAdmin(HttpSession session){
       Optional<UsuarioSesionDTO> usuario = Optional.ofNullable((UsuarioSesionDTO) session.getAttribute("usuarioLogueado"));
       if(usuario.isEmpty()){
           return "redirect:killSession";
       }
       if (!usuario.get().getRol().equalsIgnoreCase("admin")){
           return "redirect:killSession";
       }

       return "ADMIN/ControllerAdmin";

    }

    @GetMapping("/listarUsuarios")
    public String listarUsuarios(HttpSession session){
        return "ADMIN/listarUsuariosAdmin";
    }

    @GetMapping("/cargarUsuarios")
    @ResponseBody
    public ArrayList<Usuario> crearListaUsuarios(){

        ArrayList<Usuario> lista = usuarioService.mostrarUsuarios();
        return lista;
    }




}
