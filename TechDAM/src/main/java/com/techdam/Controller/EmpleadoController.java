package com.techdam.Controller;

import com.techdam.DAO.EmpleadoDAO;
import com.techdam.Model.Empleado;
import com.techdam.Model.Proyecto;
import com.techdam.Service.ProcedimientosService;
import com.techdam.Service.TransaccionesService;

import static com.techdam.Utiles.Utiles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EmpleadoController {
    public static EmpleadoDAO em = new EmpleadoDAO();
    public static TransaccionesService ts = new TransaccionesService();
    public static ProcedimientosService ps = new ProcedimientosService();



    public static void controllerMenuEmpleado() {
        int opcion = -1;

        while (opcion != 0) {
            opcion = MenuEmpleado();
            switch (opcion) {
                case 1: crearEmpleado();  break;

                case 2: verEmpleado(); break;

                case 3:  verEmpleadoPorID(); break;

                case 4: actualizarEmpleado(); break;

                case 5: eliminarEmpleado(); break;

                case 6: asignarEmpleado(); break;

                case 7: actualizarSalariosADepartamentos(); break;

                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;

            }

        }
    }

    public static int MenuEmpleado() {
        System.out.println("\n*** Menú Empleado ***");
        System.out.println("1. Insertar un Empleado");
        System.out.println("2. Listar Empleados");
        System.out.println("3. Obtener Empleado Por ID");
        System.out.println("4. Actualizar Empleado");
        System.out.println("5. Eliminar Empleado");
        System.out.println("6. Asignar empleados a un proyecto");
        System.out.println("7. Actualizar Salarios a Departamento");
        System.out.println("0. Volver");
        return pedirInt("Que escoges: ");
    }


    public static void crearEmpleado() {
        String nombre = pedirNombre("Ingrese el nombre del empleado: ");

        System.out.print("Ingrese el departamento: ");
        String departamento = sc.nextLine();

        double salario = pedirDouble("Ingrese el Salario: ");

        boolean activo = pedirSiNo("Esta activo el empleado? s/n");

        int idEmpleado = em.crearEmpleado(new Empleado(nombre, departamento, salario, activo));
        System.out.println("Empleado creado con éxito, ID: " + idEmpleado);
    }

    public static void verEmpleado() {
        List<Empleado> empleado = em.obtenerEmpleados();
        if (empleado.isEmpty()) {
            System.out.println("No hay empleados registrados.");
        } else {
            for (Empleado p : empleado) {
                System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Departamento: " + p.getDepartamento() + ", Salario: " + p.getSalario() + ", Activo: " + p.isActivo());
            }
        }
    }

    public static void verEmpleadoPorID() {

        int id = pedirInt("Ingrese el ID del empleado a ver: ");
        Optional<Empleado> empleado = em.obtenerEmpleadoPorID(id);
        if (empleado.isPresent()) {
            System.out.println("Empleado Encontrado: ID: " + empleado.get().getId() + ", Nombre: " + empleado.get().getNombre() + ", Departamento: " + empleado.get().getDepartamento() + ", Salario: " + empleado.get().getSalario() + ", Activo: " + empleado.get().isActivo());
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    public static void actualizarEmpleado() {
        int id = pedirInt("Ingrese el ID del empleado para actualizar: ");
        String nombre = pedirNombre("Ingrese el nombre del empleado: ");

        System.out.print("Ingrese el departamento: ");
        String departamento = sc.nextLine();

        double salario = pedirDouble("Ingrese el Salario: ");

        boolean activo = pedirSiNo("Esta activo el empleado? s/n");

        boolean actualizado = em.actualizarEmpleado(new Empleado(nombre, departamento, salario, activo));
        if (actualizado) {
            System.out.println("Empleado creado con éxito");
        } else {
            System.out.println("No se puedo actualizar el empleado");
        }


    }

    public static void eliminarEmpleado() {

        int id = pedirInt("Ingrese el ID del empleado a eliminar: ");
        boolean empleado = em.eliminarEmpleado(id);
        if (empleado) {
            System.out.println("Empleado eliminado con éxito");
        } else {
            System.out.println("No se pudo eliminar el empleado");
        }
    }

    public static void asignarEmpleado(){
        int idProyecto = pedirInt("A que proyecto quieres asignar :");
        List<Integer> listaIds = new ArrayList<>();

        System.out.println("Introduce tantos IDs como quieras.");

        do {
            int id = pedirInt( "Introduce un ID: ");
            listaIds.add(id);
        } while (pedirSiNo("¿Quieres introducir otro ID?"));

        ts.asignarEmpleadosConSavepoint(idProyecto, listaIds);

        }

    public static void actualizarSalariosADepartamentos(){
        String nombre = pedirNombre("Ingrese el nombre del Departamento: ");
        double porcentaje = pedirDouble("Que porcentaje quieres actualizar: ");
        ps.actualizarSalariosDepartamento(nombre,porcentaje);
    }
    }


