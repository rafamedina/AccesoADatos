package com.paquete.crudUsuario.WebController;


import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller()
@RequestMapping("/control")
public class AccessController {

    @GetMapping()
    public String jugon(HttpSession session){

        UsuarioSesionDTO usuario = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");


        if (usuario != null) {
            if(usuario.getRol().equalsIgnoreCase("admin")){
                return "ADMIN/ControllerAdmin";
            }
            else if (usuario.getRol().equalsIgnoreCase("user")) {
                return "USER/ControllerUser";
            }else{
               return "redirect:/killSession";
            }

        } else{

            return "redirect:/killSession";
        }
    }
}
