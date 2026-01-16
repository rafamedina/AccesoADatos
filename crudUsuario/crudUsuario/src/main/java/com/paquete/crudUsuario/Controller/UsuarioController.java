package com.paquete.crudUsuario.Controller;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Models.Usuario;
import com.paquete.crudUsuario.Services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.paquete.crudUsuario.Utils.Utiles.*;



@Component
public class UsuarioController implements CommandLineRunner {
    private UsuarioSesionDTO usuarioLogueado;
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {


       if(menuSesion()){
           while (true) {

               System.out.println("\n--- MENU USUARIO ---");
               System.out.println("1. Registrar nuevo usuario");
               System.out.println("2. Mostrar todos los usuarios");
               System.out.println("3. Eliminar Usuario ");
               System.out.println("4. Buscar Usuarios por id");
               System.out.println("5. Actualizar Usuario");
               System.out.println("0. Salir");
               System.out.print("Elige una opción: ");

               String opcion = sc.nextLine();

               switch (opcion) {
                   case "1":

                       break;
                   case "2":

                       break;
                   case "3":

                       break;
                   case "4":

                       break;
                   case "5":

                       break;
                   case "6":

                       break;
                   case "0":
                       System.out.println("Vuelve pronto.");
                       return; // Sale del programa (pero el servidor sigue activo)
                   default:
                       System.out.println("Opción no válida.");
               }
           }
        }
    }


    public boolean menuSesion() {

        boolean encontrado = false;

        while (!encontrado) {
            System.out.println("\n--- Inicio Sesion ---");
            String correo = pedirNombre("Dime tu correo: ");

            try {
                if (!correo.contains("@")) {
                    throw new IllegalStateException("El correo debe contener un @");
                }
                if (!usuarioService.existeEmail(correo)) {
                    throw new IllegalStateException("El correo no existe");
                }

                String password = pedirNombre("Dime tu contraseña: ");

                if (!StringUtils.hasText(password)) {
                    throw new IllegalStateException("La contraseña no puede estar vacía");
                }

                if (!usuarioService.comprobarPassword(password, correo)) {
                    throw new IllegalStateException("Contraseña incorrecta");
                }
                encontrado = true;
                Usuario usuario = usuarioService.obtenerUsuarioEmail(correo);
                usuarioLogueado = new UsuarioSesionDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail());
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        if(encontrado){
            return true;
        } else {
            return false;
        }
    }
}
