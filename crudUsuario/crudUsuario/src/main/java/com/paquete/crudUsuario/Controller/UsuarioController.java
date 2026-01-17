package com.paquete.crudUsuario.Controller;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Models.Usuario;
import com.paquete.crudUsuario.Services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

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
               System.out.println("1. Test de conexión");
               System.out.println("2. Crear usuario");
               System.out.println("3. Listar usuarios ");
               System.out.println("4. Buscar por username");
               System.out.println("5. Actualizar Usuario");
               System.out.println("6. Desactivar usuario (borrado lógico)");
               System.out.println("7. Eliminar usuario (borrado físico)");
               System.out.println("0. Salir");
               System.out.print("Elige una opción: ");

               String opcion = sc.nextLine();

               switch (opcion) {
                   case "1":

                       break;
                   case "2":
                       crearUsuario();

                       break;
                   case "3":
                       listarUsuarios();
                       break;
                   case "4":

                       break;
                   case "5":

                       break;
                   case "6":

                       break;
                   case "7":

                       break;
                   case "0":
                       System.out.println("Vuelve pronto.");
                       System.exit(0);
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
            System.out.println("Dime tu correo: ");
            String correo = sc.nextLine();
            try {
                if (!correo.contains("@")) {
                    throw new IllegalStateException("El correo debe contener un @");
                }
                if (!usuarioService.existeEmail(correo)) {
                    throw new IllegalStateException("El correo no existe");
                }

                System.out.println("Dime tu contraseña: ");
                String password = sc.nextLine();
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

    public void crearUsuario(){

        System.out.println("Vamos a proceder con la creación de un nuevo usuario");
        System.out.println("Primero introduce el correo");
        String correo = sc.nextLine();
        try{
            if (!correo.contains("@")) {
                throw new IllegalStateException("El correo debe contener un @");
            }
            if(usuarioService.existeEmail(correo)){
                throw new IllegalStateException("El correo ya existe");
            }
            System.out.println("Perfecto, el correo esta disponible procedemos a la creación");
            String nombre = pedirNombre("Dime el nombre: ");
            String apellidos = pedirNombre("Dime los apellidos: ");
            System.out.println("Que contraseña quieres asignarle: ");
            String password = sc.nextLine();
            Usuario usuario = new Usuario(nombre,apellidos,correo,password);
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            if(usuarioCreado != null){
                System.out.println("Usuario creado con exito, el numero de su id es: " + usuarioCreado.getId());
            }
        } catch (IllegalStateException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public void listarUsuarios(){

        ArrayList<Usuario> lista;
        try{
            lista = usuarioService.mostrarUsuarios();
            if(lista.isEmpty()){
                throw new IllegalStateException("La lista esta vacia");
            }

            System.out.println("↪LISTA DE USUARIOS REGISTRADOS↩");
            for(Usuario usuario : lista){

                String linea = "↪Nombre: " + usuario.getNombre() + "| Apellidos: " + usuario.getApellidos() + " | Correo: " + usuario.getEmail() + " | Estado: " + (usuario.isActivo() ? "Está activo" : "No está activo");

                System.out.println(linea);


            }
        } catch (IllegalStateException e){
            System.out.println(e.getMessage());
        }


    }

}
