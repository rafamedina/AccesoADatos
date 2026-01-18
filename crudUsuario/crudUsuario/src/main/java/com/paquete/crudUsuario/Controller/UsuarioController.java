package com.paquete.crudUsuario.Controller;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Models.Usuario;
import com.paquete.crudUsuario.Services.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.paquete.crudUsuario.Utils.Utiles.*;



@Component
public class UsuarioController implements CommandLineRunner {
    private UsuarioSesionDTO usuarioLogueado;
    private final UsuarioService usuarioService;
    public enum DatosUsuario  {
        NOMBRE,
        APELLIDOS,
        USERNAME,
        EMAIL,
        PASSWORD
    }

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
                       buscarUsuarioPorUsername();
                       break;
                   case "5":
                       actualizarUsuario();
                       break;
                   case "6":
                        borradoLogico();
                       break;
                   case "7":
                       eliminarUsuario();
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
            System.out.println("Que Nombre de usuario quieres asignarle: ");
            String nombreusuario = sc.nextLine();
            System.out.println("Que contraseña quieres asignarle: ");
            String password = sc.nextLine();
            Usuario usuario = new Usuario(nombre,apellidos,nombreusuario,correo,password);
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

                String linea = "↪Nombre: " + usuario.getNombre() + "| Apellidos: " + usuario.getApellidos() + " | Nombre de usuario: " + usuario.getNombreusuario() + " | Correo: " + usuario.getEmail() + " | Estado: " + (usuario.isActivo() ? "Está activo" : "No está activo");

                System.out.println(linea);


            }
        } catch (IllegalStateException e){
            System.out.println(e.getMessage());
        }
    }

    public void buscarUsuarioPorUsername(){
        try{
            System.out.println("Dime que usuario quieres buscar");
            String username = sc.nextLine();
            Optional<Usuario> usuario = usuarioService.buscarPorUsername(username);
            if(usuario.isEmpty()){
            throw new IllegalStateException("El usuario no existe");
            }
            String linea = "↪Nombre: " + usuario.get().getNombre() + "| Apellidos: " + usuario.get().getApellidos() + " | Nombre de usuario: " + usuario.get().getNombreusuario() + " | Correo: " + usuario.get().getEmail() + " | Estado: " + (usuario.get().isActivo() ? "Está activo" : "No está activo");
            System.out.println(linea);
        } catch (IllegalStateException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public void actualizarUsuario(){
        System.out.println("Vamos a proceder con la edición de un usuario");
        System.out.println("Primero introduce el correo");
        String correo = sc.nextLine();
        try{
            if (!correo.contains("@")) {
                throw new IllegalStateException("El correo debe contener un @");
            }
            if(!usuarioService.existeEmail(correo)){
                throw new IllegalStateException("El correo no existe");
            }
            Usuario usuario = usuarioService.obtenerUsuarioEmail(correo);
            System.out.println("Perfecto, el correo existe");
            System.out.println("Estos son los datos de ese usuario: ");
            String linea = "↪Nombre: " + usuario.getNombre() + "| Apellidos: " + usuario.getApellidos() + " | Nombre de usuario: " + usuario.getNombreusuario() + " | Correo: " + usuario.getEmail() + " | Estado: " + (usuario.isActivo() ? "Está activo" : "No está activo");
            System.out.println(linea);
            System.out.println("procedemos a la edición");
            System.out.println("¿Qué quieres editar? Opciones: " + Arrays.toString(DatosUsuario.values()));
            String datos = sc.nextLine();
            DatosUsuario opcion = DatosUsuario.valueOf(datos.toUpperCase());

            switch (opcion){

                case NOMBRE :
                System.out.println("Dime el nuevo nombre");
                String nombreNuevo = sc.nextLine();
                usuario.setNombre(nombreNuevo);
                break;

                case APELLIDOS :
                System.out.println("Dime los nuevos apellidos");
                String apellidosNuevos = sc.nextLine();
                usuario.setApellidos(apellidosNuevos);
                break;

                case USERNAME :
                System.out.println("Dime el nuevo nombre de usuario");
                String usernameNuevo = sc.nextLine();
                usuario.setNombreusuario(usernameNuevo);
                break;

                case EMAIL :
                System.out.println("Dime el nuevo correo");
                String emailNuevo = sc.nextLine();
                usuario.setEmail(emailNuevo);
                break;

                case PASSWORD :
                System.out.println("Dime la nueva contraseña");
                String passwordNueva = sc.nextLine();
                usuario.setPassword(passwordNueva);
                break;

            }
            Usuario usuarioactualizado = usuarioService.actualizarUsuario(usuario);
            if(usuarioactualizado != null){
                System.out.println("Usuario actualizado");
                String linea2 = "↪Nombre: " + usuarioactualizado.getNombre() + "| Apellidos: " + usuarioactualizado.getApellidos() + " | Nombre de usuario: " + usuarioactualizado.getNombreusuario() + " | Correo: " + usuarioactualizado.getEmail() + " | Estado: " + (usuarioactualizado.isActivo() ? "Está activo" : "No está activo");
                System.out.println(linea2);
            }
        } catch (IllegalStateException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public void borradoLogico() {

        System.out.println("Vamos a proceder con la desactivación de un usuario");
        System.out.println("Primero introduce el correo");
        String correo = sc.nextLine();
        try {
            if (!correo.contains("@")) {
                throw new IllegalStateException("El correo debe contener un @");
            }
            if (!usuarioService.existeEmail(correo)) {
                throw new IllegalStateException("El correo no existe");
            }

            Usuario usuario = usuarioService.desactivarUsuario(correo);
            if (usuario == null) {
                throw new IllegalStateException("Problemas al cambiar el estado");
            }

            System.out.println("Estado cambiado a desactivado");
            String linea = "↪Nombre: " + usuario.getNombre() + "| Apellidos: " + usuario.getApellidos() + " | Nombre de usuario: " + usuario.getNombreusuario() + " | Correo: " + usuario.getEmail() + " | Estado: " + (usuario.isActivo() ? "Está activo" : "No está activo");
            System.out.println(linea);


        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarUsuario() {
        System.out.println("Vamos a proceder con la eliminación de un usuario");
        System.out.println("Primero introduce el correo");
        String correo = sc.nextLine();
        try {
            if (!correo.contains("@")) {
                throw new IllegalStateException("El correo debe contener un @");
            }
            if (!usuarioService.existeEmail(correo)) {
                throw new IllegalStateException("El correo no existe");
            }
            Usuario usuario = usuarioService.obtenerUsuarioEmail(correo);
            usuarioService.eliminarUsuario(usuario);

            if(!usuarioService.existeEmail(correo)){
                System.out.println("Usuario Eliminado correctamente");
            } else{
                System.out.println("El usuario no se ha podido eliminar");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

}
