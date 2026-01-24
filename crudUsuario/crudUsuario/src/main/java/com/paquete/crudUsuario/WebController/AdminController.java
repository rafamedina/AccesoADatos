package com.paquete.crudUsuario.WebController;
import com.fasterxml.jackson.databind.ObjectMapper;
// Y para el módulo:
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.paquete.crudUsuario.DTO.UsuarioSesionDTO;
import com.paquete.crudUsuario.Entity.Departamento;
import com.paquete.crudUsuario.Entity.Roles;
import com.paquete.crudUsuario.Entity.Usuario;
import com.paquete.crudUsuario.Exportadores.RespuestaExportacionDTO;
import com.paquete.crudUsuario.Services.DepartamentoService;
import com.paquete.crudUsuario.Services.RolesService;
import com.paquete.crudUsuario.Services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.time.format.DateTimeFormatter;
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

    @Autowired
    DepartamentoService departamentoService;

    // Jackson (ObjectMapper) ya suele venir configurado en Spring,
    // pero aquí lo instanciamos para personalizarlo al vuelo.
    private ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            // ESTA ES LA LÍNEA MÁGICA:
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
    public ResponseEntity<?> crearListaUsuarios(HttpSession session) {
        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // CORRECCIÓN: Null check previo y uso de .equals() para comparar el contenido del String
        if (usuarioSesionDTO == null || !"admin".equals(usuarioSesionDTO.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
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
                        usuario.isActivo(), // Recuerda: en tu DTO se llama 'estado', asegúrate de que coincida
                        usuario.getDepartamento().getNombreDepartamento()
                ));
            }

            return ResponseEntity.ok(lista2);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping("/actualizarUsuario")
    @ResponseBody
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioSesionDTO usuarioDTO,HttpSession session) {
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

            Optional<Departamento> departamentoBD = departamentoService.buscarDepartamentoPorNombre(usuarioDTO.getDepartamento());

            if(departamentoBD.isEmpty()) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error con el departamento");}
            if(departamentoBD.isPresent()){

                usuarioExistente.get().setDepartamento(departamentoBD.get());
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
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id,HttpSession session) { // 3. Usa @PathVariable para capturar el {id} de la URL
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
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario,           // Viene en el JSON (Body)
                                          @RequestParam String nombreDepartamento,HttpSession session) {
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
            Optional<Departamento> departamentoBD = departamentoService.buscarDepartamentoPorNombre(nombreDepartamento);

            if(departamentoBD.isEmpty()) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error con el departamento");}
            if(departamentoBD.isPresent()){

                usuario.setDepartamento(departamentoBD.get());
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

    @RequestMapping("/exportarJSON")
    @ResponseBody
    public ResponseEntity<?> exportarJSON(HttpSession session){
        UsuarioSesionDTO usuarioSesionDTO = (UsuarioSesionDTO) session.getAttribute("usuarioLogueado");

        // CORRECCIÓN: Null check previo y uso de .equals() para comparar el contenido del String
        if (usuarioSesionDTO == null || !"admin".equals(usuarioSesionDTO.getRol())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // 1. Obtener datos
            ArrayList<Usuario> listaUsuarios = usuarioService.mostrarUsuarios();

            ArrayList<UsuarioSesionDTO> listadto = new ArrayList<>();

            for (Usuario usuario : listaUsuarios) {

                // PROTECCIÓN ADICIONAL: Evitamos error si el usuario no tiene roles asignados
                String nombreRol = "Sin Rol";
                if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                    nombreRol = usuario.getRoles().iterator().next().getNombreRol();
                }

                listadto.add(new UsuarioSesionDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellidos(),
                        usuario.getNombreusuario(),
                        usuario.getEmail(),
                        nombreRol, // Usamos la variable protegida
                        usuario.getFechaCreacion(),
                        usuario.isActivo(),
                       usuario.getDepartamento().getNombreDepartamento()
                       // Recuerda: en tu DTO se llama 'estado', asegúrate de que coincida
                ));
            }
            // 2. Empaquetarlos en nuestro DTO para que tenga la estructura que te gusta
            // (Si solo quisieras la lista plana, pasarías 'listaUsuarios' directamente al mapper)
            RespuestaExportacionDTO datosExportar = new RespuestaExportacionDTO(listadto);

            // 3. LA MAGIA: Convertir Objeto Java -> Array de Bytes JSON
            byte[] contenidoJson = mapper.writeValueAsBytes(datosExportar);

            // 4. Nombre del archivo
            String nombreArchivo = "usuarios_" + System.currentTimeMillis() + ".json";

            // 5. Descargar
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                    .body(contenidoJson);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
