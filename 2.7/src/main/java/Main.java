import static Utiles.Utiles.*;
import DAO.*;
public class Main {



    public static void main(String[] args) {
        MenuPrincipal();
    }

    public static void MenuPrincipal() {
        daoAsignaciones dA = new daoAsignaciones();
        daoEmpleados dE = new daoEmpleados();
        daoProyectos dP = new daoProyectos();

            int opcion = -1;

              while(opcion != 0){
                    opcion = Menu();
                    switch (opcion) {
                        case 1:
                            dE.insertarEmpleado();
                            saltolinea();
                            break;
                        case 2:
                            dP.insertarProyecto();
                            saltolinea();
                            break;
                        case 3:

                           dA.llamarProcedimiento();
                            saltolinea();
                            break;
                        case 4:
                            dE.transcacion();
                            saltolinea();
                            break;
                        case 5:
                            dE.mostrarEmpleados();
                            saltolinea();
                            dP.mostrarProyectos();
                            saltolinea();
                            dA.mostrarAsignaciones();
                            saltolinea();
                            break;
                        default:
                            System.out.println("\nOpción no válida. Por favor, intente de nuevo.");

                        case 0:
                            System.out.println("Saliendo....");
                            break;


                    }
                }

            }

        public static int Menu(){
            System.out.println("\n--- GESTIÓN DE EMPLEADOS Y PROYECTOS ---");
            System.out.println("1. Insertar Empleados"); // Para el punto 2
            System.out.println("2. Insertar Proyectos"); // Para el punto 2
            System.out.println("3. Asignar Empleado a Proyecto (Ejecutar Procedimiento Almacenado)"); // Para el punto 3
            System.out.println("4. Realizar Transacción (Incremento Salario y Descuento Presupuesto)"); // Para el punto 4
            System.out.println("5. Mostrar Estado Final de Tablas"); // Para el punto 5
            System.out.println("0. Salir");
            return pedirInt("Seleccione una opción: ");

        }
    }

