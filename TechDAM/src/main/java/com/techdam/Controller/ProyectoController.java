package com.techdam.Controller;

import com.techdam.DAO.ProyectoDAO;
import com.techdam.Model.Empleado;
import com.techdam.Model.Proyecto;
import com.techdam.Service.ProcedimientosService;
import com.techdam.Service.TransaccionesService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.techdam.Utiles.Utiles.*;
import static com.techdam.Utiles.Utiles.pedirDouble;
import static com.techdam.Utiles.Utiles.pedirNombre;
import static com.techdam.Utiles.Utiles.pedirSiNo;
import static com.techdam.Utiles.Utiles.sc;

public class ProyectoController {
    public static TransaccionesService ts = new TransaccionesService();
    public static ProyectoDAO pro = new ProyectoDAO();
    public static ProcedimientosService ps = new ProcedimientosService();

    public static void controllerMenuProyecto() {
        int opcion = -1;

        while (opcion != 0) {
            opcion = MenuProyecto();
            switch (opcion) {
                case 1: crearProyecto();  break;

                case 2: verProyecto(); break;

                case 3: verProyectoPorID(); break;

                case 4: actualizarProyecto(); break;

                case 5: eliminarProyecto(); break;

                case 6: actualizarPresupestoAProyecto(); break;

                case 7: transferirPresupuesto(); break;


                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;

            }

        }
    }
    public static int MenuProyecto(){
        System.out.println("\n*** Menú Proyecto ***");
        System.out.println("1. Insertar un Proyecto");
        System.out.println("2. Listar Proyecto");
        System.out.println("3. Obtener Proyecto Por ID");
        System.out.println("4. Actualizar Proyecto");
        System.out.println("5. Eliminar Proyecto");
        System.out.println("6. Actualizar Presupuesto a Proyecto");
        System.out.println("7. Transferir Presupuesto de un proyecto a otro");
        System.out.println("0. Volver");
        return pedirInt("Que escoges: ");
    }

    public static void crearProyecto() {
        String nombre = pedirNombre("Ingrese el nombre del Proyecto: ");


        double presupuesto = pedirDouble("Ingrese el Presupuesto: ");

        int idpro = pro.crearProyecto(new Proyecto(nombre,presupuesto));
        System.out.println("Proyecto creado con éxito, ID: " + idpro);
    }

    public static void verProyecto() {
        List<Proyecto> proyecto = pro.obtenerProyectos();
        if (proyecto.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
        } else {
            for (Proyecto p : proyecto) {
                System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Presupuesto: " + p.getPresupuesto());
            }
        }
    }

    public static void verProyectoPorID() {

        int id = pedirInt("Ingrese el ID del empleado a ver: ");
        Optional<Proyecto> proyecto = pro.obtenerProyectoPorID(id);
        if (proyecto.isPresent()) {
            System.out.println("proyecto Encontrado: ID: " + proyecto.get().getId() + ", Nombre: " + proyecto.get().getNombre() + ", Departamento: " + proyecto.get().getPresupuesto());
        } else {
            System.out.println("proyecto no encontrado.");
        }
    }

    public static void actualizarProyecto() {
        int id = pedirInt("Ingrese el ID del proyecto para actualizar: ");
        String nombre = pedirNombre("Ingrese el nombre del proyecto: ");

        double presupuesto = pedirDouble("Ingrese el presupuesto: ");


        boolean actualizado = pro.actualizarProyecto(new Proyecto(id,nombre,presupuesto));
        if (actualizado) {
            System.out.println("Proyecto creado con éxito");
        } else {
            System.out.println("No se puedo actualizar el Proyecto");
        }


    }

    public static void eliminarProyecto() {

        int id = pedirInt("Ingrese el ID del Proyecto a eliminar: ");
        boolean proyecto = pro.eliminarProyecto(id);
        if (proyecto) {
            System.out.println("Empleado eliminado con éxito");
        } else {
            System.out.println("No se pudo eliminar el empleado");
        }
    }


    public static void actualizarPresupestoAProyecto(){
        int id = pedirInt("Ingrese el ID del Proyecto: ");
        double porcentaje = pedirDouble("Cual es el nuevo presupuesto: ");
        ps.actualizarPresupuestoProyecto(id,porcentaje);
    }

    public static void transferirPresupuesto(){
        int id1 = pedirInt("Ingrese el ID del Proyecto de Origen: ");
        int id2 = pedirInt("Ingrese el ID del Proyecto de Origen: ");
        double porcentaje = pedirDouble("Cuanto quieres transferir: ");
        BigDecimal bd = new BigDecimal(String.valueOf(porcentaje));
        ts.transferirPresupuesto(id1,id2,bd);
    }


}
