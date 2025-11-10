import Consultas.proyectoDAO;
import static Utiles.utilesConexion.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        proyectoDAO pd = new proyectoDAO();
        int opcion;

        do {
            mostrarMenu();
            System.out.print("Elige una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    pd.insertarProyecto();
                    saltolinea();
                    break;
                case 2:
                    pd.actualizarProyecto();
                    saltolinea();
                    break;
                case 3:
                    pd.eliminarPorId();
                    saltolinea();
                    break;
                case 4:
                    pd.mostrarProyectos(); // suponiendo que muestra todos los proyectos
                    saltolinea();
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }

            System.out.println(); // salto de línea para separar las acciones

        } while (opcion != 5);

        sc.close();
    }

    public static void mostrarMenu() {
        System.out.println("-------- MENÚ DE PROYECTOS --------");
        System.out.println("1. Insertar proyecto");
        System.out.println("2. Actualizar proyecto");
        System.out.println("3. Eliminar proyecto por ID");
        System.out.println("4. Mostrar proyectos");
        System.out.println("5. Salir");
        System.out.println("-----------------------------------");
    }
}
