package com.paquete.crudUsuario.WebController;


import com.paquete.crudUsuario.Entity.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class LoginController {


    @GetMapping
    public String login() {
        return "indexLogin";
    }

//    @GetMapping("/login")
//    @ResponseBody
//    public Usuario inicioSesion(){
//
//
//        return Usuario;
//    }


}
