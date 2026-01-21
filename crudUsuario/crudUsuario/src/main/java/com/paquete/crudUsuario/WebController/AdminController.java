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


    @GetMapping("/cargarUsuarios")
    @ResponseBody
    public ArrayList<UsuarioSesionDTO> crearListaUsuarios() {

        ArrayList<Usuario> lista = usuarioService.mostrarUsuarios();

        // CORRECCIÓN: Inicializamos la lista para que exista en memoria
        ArrayList<UsuarioSesionDTO> lista2 = new ArrayList<>();

        for (Usuario usuario : lista) {

            // PROTECCIÓN ADICIONAL: Evitamos error si el usuario no tiene roles asignados
            String nombreRol = "Sin Rol";
            if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                nombreRol = usuario.getRoles().iterator().next().getNombreRol();
            }

            lista2.add(new UsuarioSesionDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidos(),
                    usuario.getNombreusuario(),
                    usuario.getEmail(),
                    nombreRol, // Usamos la variable protegida
                    usuario.getFechaCreacion(),
                    usuario.isActivo() // Recuerda: en tu DTO se llama 'estado', asegúrate de que coincida
            ));
        }

        return lista2;
    }



}
