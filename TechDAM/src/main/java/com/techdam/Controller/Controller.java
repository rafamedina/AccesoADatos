package com.techdam.Controller;
import static com.techdam.Utiles.Utiles.*;
import static com.techdam.Controller.EmpleadoController.*;
import static com.techdam.Controller.ProyectoController.*;
import java.util.Scanner;
public class Controller {
    public void controllerMenuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            opcion = MenuPrincipal();
            switch (opcion){
                case 1:
                    controllerMenuProyecto(); break;
                case 2:
                    controllerMenuEmpleado(); break;
                case 0:
                    System.out.println("Saliendo..."); break;
                default:
                    System.out.println("Opcion no valida"); break;

            }

        }
    }

    public static int MenuPrincipal(){
        System.out.println("\n*** Menú General ***");
        System.out.println("1. Proyectos");
        System.out.println("2. Empleados");
        System.out.println("0. Salir");
        return pedirInt("Que escoges: ");
    }

}

