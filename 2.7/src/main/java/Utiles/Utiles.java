package Utiles;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utiles{
    public static Scanner sc = new Scanner(System.in);
    // Método para pedir un número decimal positivo por consola
    public static double pedirDouble(String mensaje) {
        double numero = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            try {
                numero = sc.nextDouble();

                if (numero >= 0) {
                    valido = true;
                } else {
                    System.out.println("El número que has introducido no es válido. Solo números positivos.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes introducir un número decimal.");
                sc.nextLine(); // limpia el buffer si se introduce texto
            }
        }

        sc.nextLine(); // limpia el salto de línea pendiente
        return numero;
    }

        // Método para pedir un número entero positivo por consola
        public static int pedirInt(String mensaje) {
        int num = -1;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print(mensaje);
                num = sc.nextInt(); // Solicita número entero
                if (num >= 0) {
                    valido = true;
                } else {
                    System.out.println("Número no válido. Debe ser mayor o igual a 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar un número entero.");
                sc.nextLine(); // limpia el buffer
            }
        }
        sc.nextLine();
        return num;
    }
    public static void saltolinea(){
        System.out.println("Pulsa enter para seguir");
        new Scanner(System.in).nextLine();
    }

}



