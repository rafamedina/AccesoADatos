package com.paquete.crudUsuario.WebController;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Entity.Roles;
import com.paquete.crudUsuario.Entity.Usuario;
import com.paquete.crudUsuario.Services.RolesService;
import com.paquete.crudUsuario.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolesService rolesService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping()
    public String validarAdmin(HttpSession session) {
        Optional<UsuarioSesionDTO> usuario = Optional.ofNullable((UsuarioSesionDTO) session.getAttribute("usuarioLogueado"));
        if (usuario.isEmpty()) {
            return "redirect:killSession";
        }
        if (!usuario.get().getRol().equalsIgnoreCase("admin")) {
            return "redirect:killSession";
        }
        return "ADMIN/ControllerAdmin";

    }


    @GetMapping("/cargarUsuarios")
    @ResponseBody
// MEJORA: Especificamos el tipo exacto en el ResponseEntity para mayor seguridad
    public ResponseEntity<List<UsuarioSesionDTO>> crearListaUsuarios(HttpSession session) {
        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // CORRECCIÓN: Null check previo y uso de .equals() para comparar el contenido del String
        if (usuarioSesionDTO == null || !"admin".equals(usuarioSesionDTO.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Es recomendable usar la interfaz List en la declaración
        List<Usuario> lista = usuarioService.mostrarUsuarios();
        List<UsuarioSesionDTO> lista2 = new ArrayList<>();

        for (Usuario usuario : lista) {
            // PROTECCIÓN ADICIONAL (Tu lógica original estaba bien aquí)
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
                    nombreRol,
                    usuario.getFechaCreacion(),
                    usuario.isActivo()
            ));
        }

        // CORRECCIÓN: Envolvemos la lista en un ResponseEntity con estado 200 OK
        return ResponseEntity.ok(lista2);
    }

    @PutMapping("/actualizarUsuario")
    @ResponseBody
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioSesionDTO usuarioDTO, HttpSession session) {

        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // CORRECCIÓN: Null check previo y uso de .equals() para comparar el contenido del String
        if (usuarioSesionDTO == null || !"admin".equals(usuarioSesionDTO.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            if (usuarioDTO.getId() == null) {
                return ResponseEntity.badRequest().body("El ID es obligatorio");
            }

            // 1. Recuperamos el usuario de la BBDD
            Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioId(usuarioDTO.getId());

            if (usuarioExistente == null) {
                return ResponseEntity.notFound().build();
            }

            // 2. Actualizamos campos básicos
            usuarioExistente.get().setNombre(usuarioDTO.getNombre());
            usuarioExistente.get().setApellidos(usuarioDTO.getApellidos());
            usuarioExistente.get().setEmail(usuarioDTO.getEmail());

            if (usuarioDTO.getUsername() != null && !usuarioDTO.getUsername().isEmpty()) {
                usuarioExistente.get().setNombreusuario(usuarioDTO.getUsername());
            }

            usuarioExistente.get().setActivo(usuarioDTO.isEstado());

            // 3. ACTUALIZACIÓN DEL ROL (La parte clave)
            String rolNombre = usuarioDTO.getRol(); // Viene "ADMIN" o "USER"

            if (rolNombre != null && !rolNombre.isEmpty()) {
                // Buscamos el objeto Rol en la base de datos
                Optional<Roles> rolEncontrado = rolesService.buscarPorNombre(rolNombre);

                if (rolEncontrado.isPresent()) {
                    // LIMPIAMOS los roles antiguos
                    usuarioExistente.get().getRoles().clear();

                    // AÑADIMOS el nuevo rol
                    usuarioExistente.get().getRoles().add(rolEncontrado.get());
                } else {
                    // Opcional: Si el rol no existe, podrías devolver error
                    System.out.println("Rol no encontrado en BBDD: " + rolNombre);
                }
            }

            // 4. Guardamos
            Usuario usuarioGuardado = usuarioService.actualizarUsuario(usuarioExistente.get());

            return ResponseEntity.ok(usuarioGuardado);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    // 1. Usa @DeleteMapping porque tu JS envía un DELETE
// 2. Usa {id} (sin el símbolo $) para definir la variable de ruta
    @DeleteMapping("/eliminarUsuario/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, HttpSession session) { // 3. Usa @PathVariable para capturar el {id} de la URL
        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // CORRECCIÓN: Null check previo y uso de .equals() para comparar el contenido del String
        if (usuarioSesionDTO == null || !"admin".equals(usuarioSesionDTO.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // Buscamos el usuario (Asumo que tu servicio devuelve Optional)
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioId(id);

            if (usuario.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Eliminamos pasando la entidad (según tu código original)
            usuarioService.eliminarUsuario(usuario.get());

            // Devolvemos OK (200)
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario");
        }
    }


    @PostMapping("/crearUsuario")
    @ResponseBody
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, HttpSession session) {

        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // CORRECCIÓN: Null check previo y uso de .equals() para comparar el contenido del String
        if (usuarioSesionDTO == null || !"admin".equals(usuarioSesionDTO.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // 1. Validaciones básicas (AÑADIMOS LA DEL EMAIL)
            if (usuario.getNombreusuario() == null || usuario.getNombreusuario().isEmpty() ||
                    usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario y contraseña son obligatorios");
            }

            // VALIDACIÓN DE EMAIL
            if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
                return ResponseEntity.badRequest().body("Error: El email no es válido (debe contener '@')");
            }

            // 2. ASIGNAR ROL POR DEFECTO ("USER")
            Optional<Roles> rolUser = rolesService.buscarPorNombre("USER");

            if (rolUser.isPresent()) {
                if (usuario.getRoles() == null) {
                    usuario.setRoles(new HashSet<>());
                }
                usuario.getRoles().add(rolUser.get());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: El rol 'USER' no existe en la base de datos.");
            }

            // 3. Estado y Guardado
            usuario.setActivo(true);
            usuarioService.crearUsuario(usuario);

            return ResponseEntity.ok("Usuario creado exitosamente");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}