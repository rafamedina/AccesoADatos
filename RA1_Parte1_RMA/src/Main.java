import Cuenta.*;
import java.util.Scanner;

public class Main {
    static ManejarCuenta mc = new ManejarCuenta();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            boolean salida = false;

            do {
                System.out.print("Dime tu DNI: ");
                String dni = sc.nextLine();

                Cuenta cuenta = mc.iniciarSesion(dni);

                if (cuenta == null) {
                    System.out.println("No se encontró una cuenta con ese DNI.");
                    System.out.print("¿Deseas crear una nueva cuenta? (s/n): ");
                    String opcion = sc.nextLine();

                    if (opcion.equalsIgnoreCase("s")) {
                        System.out.print("Introduce tu nombre: ");
                        String nombre = sc.nextLine();

                        cuenta = new Cuenta(nombre, dni );
                        mc.serializarCuenta(cuenta);
                        System.out.println("Cuenta creada correctamente.");
                    } else {
                        continue;
                    }
                }

                int eleccion;
                do {
                    eleccion = cuenta.Menu();

                    switch (eleccion) {
                        case 1 : cuenta.ingresarDinero();
                        case 2 : cuenta.sacarDinero();
                        case 3 : cuenta.mostrarMovimientos();
                        case 4 : cuenta.mostrarSaldo();
                        case 5 : {
                            mc.serializarCuenta(cuenta);
                            System.out.println("Cuenta guardada. Saliendo...");
                        }
                        default : System.out.println("Número no válido.");
                    }

                } while (eleccion != 5);

                System.out.print("¿Deseas salir del programa? (s/n): ");
                String salir = sc.nextLine();
                salida = salir.equalsIgnoreCase("s");

            } while (!salida);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
