package com.example.demo.Controller;

import com.example.demo.Models.Empleado;
import com.example.demo.Models.Tarea;
import com.example.demo.Service.EmpleadoService;
import com.example.demo.Service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class Controller implements CommandLineRunner {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private TareaService tareaService;

    static Scanner sc = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("\n--- MENU GYM TRACKER ---");
            System.out.println("1. Buscar un Empleado por ID");
            System.out.println("2. Ver horas de empleado");
            System.out.println("3. Ingresar Nuevas Tareas");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    mostrarEmpleados();
                    break;
                case "2":
                    calcularHoras();
                    break;
                case "3":
                    registrarMasTareas();
                    break;
//                case "4":
//                    mostrarUsuarios();
//                    break;
//                case "5":
//                    mostrarUsuarioPorID();
//                    break;
//                case "6":
//                    probarActualizar();
//                    break;
                case "0":
                    System.out.println("¡A entrenar! Adiós.");
                    return; // Sale del programa (pero el servidor sigue activo)
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
         public void mostrarEmpleados(){

        try{
            System.out.println("Que id quieres buscar");
        long id = sc.nextLong();
        sc.nextLine();
        Optional<Empleado> empleado = empleadoService.buscarEmpleadoPorId(id);
            if (empleado.isEmpty()) {
                System.out.println("El empleado no existe");
            }
            else
                empleado.ifPresent(value -> System.out.println("El empleado :  ID: " + value.getId() + "| Nombre : " + value.getNombre() + "| Departamento : " + value.getDepartamento()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void calcularHoras(){

        try{
            System.out.println("Que id quieres saber las horas");
            long id = sc.nextLong();
            sc.nextLine();
            double horas = tareaService.horasPorEmpleado(id);
            if (horas == 0) {
                System.out.println("El empleado no tiene horas");
            }
            else
            {
                System.out.println("Este Empleado ha trabajado: " + horas + " horas");
            };
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void registrarMasTareas(){
        Tarea tarea;
        System.out.println("De que empleado quieres crear la tarea: (id)");
        long idEmpleado = sc.nextLong();
        sc.nextLine();
        System.out.println("A que proyecto esta as");
        long idProyecto = sc.nextLong();
        sc.nextLine();
        System.out.println("Cuantas horas ha trabajado");
        double horas = sc.nextDouble();
        sc.nextLine();
        tarea = new Tarea(idEmpleado,idProyecto,horas);
        try{
            if(tareaService.insertarTarea(tarea)){
                System.out.println("Tarea registrada correctamente");
            } else {
                System.out.println("No se pudo ingresar");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    }
