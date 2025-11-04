package Banco;

import java.util.Scanner;

public class GestionMenu {
    static Scanner sc = new Scanner(System.in);
    static GestionCuentas gc = new GestionCuentas();


public static void controlador(){
    int eleccion = 0;
    System.out.println("Bienvenido");
    gc.inicializarPrograma();
    while(eleccion!=5){
        eleccion = MenuPrincipal();
        switch (eleccion){
            case 1:
                gc.listarCuentas();
                break;
            case 2:
                gc.crearCuenta();
                break;
            case 3:

                if(gc.iniciarSesion()){
                    int eleccion2 = 0;
                    while(eleccion2!=6){
                        eleccion2 = MenuSecundario();
                        switch (eleccion2)
                        {
                            case 1:
                                gc.consultarSaldo();
                                break;
                            case 2:
                                gc.mostrarDatos();
                                break;
                            case 3:
                                gc.leerMovimientos();
                                break;
                            case 4:
                                gc.hacerIngreso();
                                break;

                            case 5:
                                gc.hacerRetirada();
                                break;
                            case 6:
                                gc.guardarCliente();
                                System.out.println("Volviendo al menu principal..\n");
                                break;

                            default:

                                System.out.println("Valor no válido");
                        }
                    }
                }
                break;
            case 4:
                gc.mostrarEstadisticas();
                break;
            case 5:
                System.out.println("adios");
                break;
            default:
                System.out.println("Valor no válido");
        }
    }
}










    public static int MenuPrincipal(){
        System.out.println("--------Menu Principal--------");
        System.out.println("1. Listar Cuentas Existentes");
        System.out.println("2. Crear Cuenta");
        System.out.println("3. Seleccionar Cuenta");
        System.out.println("4. Estadisticas Generales");
        System.out.println("5. Salir");
        return eleccion();
    }

    public static int MenuSecundario(){
        System.out.println("--------Menu Principal--------");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Ver datos del titular");
        System.out.println("3. Ver historial de movimientos");
        System.out.println("4. Realizar ingreso");
        System.out.println("5. Realizar retirada");
        System.out.println("6. Salir");
        return eleccion();
    }

    public static int eleccion() {
        while (true) {
            try {
                System.out.print("Seleccione una opción: ");
                int eleccion = sc.nextInt();
                sc.nextLine();
                return eleccion;
            } catch (Exception e) {
                System.out.println("Opción no válida");
            }

        }

    }
}
