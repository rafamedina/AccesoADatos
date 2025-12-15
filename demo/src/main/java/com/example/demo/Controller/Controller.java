package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Controller implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;


    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("\n--- MENU GYM TRACKER ---");
            System.out.println("1. Registrar nuevo usuario");
            System.out.println("2. Login (Probar contraseña)");
            System.out.println("3. Eliminar Usuario ");
            System.out.println("4. Mostrar Usuarios ");
            System.out.println("5. Buscar Usuario por ID ");
            System.out.println("6. Actualizar Usuario");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    probarRegistro();
                    break;
                case "2":
                    probarLogin();
                    break;
                case "3":
                    eliminarUsuario();
                    break;
                case "4":
                    mostrarUsuarios();
                    break;
                case "5":
                    mostrarUsuarioPorID();
                    break;
                case "6":
                    probarActualizar();
                    break;
                case "0":
                    System.out.println("¡A entrenar! Adiós.");
                    return; // Sale del programa (pero el servidor sigue activo)
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
