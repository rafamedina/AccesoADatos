package com.paquete.crudUsuario.Utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utiles {
    public static Scanner sc = new Scanner(System.in);

    public static double pedirDouble( String mensaje) {
        String entrada;
        double valor = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            entrada = sc.nextLine().trim();

            // Reemplazar coma por punto para uniformar formato SQL/Java
            entrada = entrada.replace(",", ".");

            // Validación: números con decimales opcionales (sin limites estrictos)
            if (!entrada.matches("\\d+(\\.\\d+)?")) {
                System.out.println("Error: Introduce un número válido (solo dígitos, punto o coma).");
                continue;
            }

            try {
                valor = Double.parseDouble(entrada);
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Formato numérico incorrecto.");
            }
        }

        return valor;
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
    public static Long pedirLong(String mensaje) {
        long num = 0; // 1. Inicializamos a 0 para evitar el error de sintaxis
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensaje);
                // 2. IMPORTANTE: Usamos nextLong(), no nextInt()
                num = sc.nextLong();

                if (num >= 0) {
                    valido = true;
                } else {
                    System.out.println("Número no válido. Debe ser mayor o igual a 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Debes ingresar un número entero.");
                sc.nextLine(); // Limpia el buffer si escriben letras
            }
        }

        sc.nextLine(); // Limpia el "Enter" que queda colgado en el scanner
        return num;
    }
    public static String pedirNombre(String mensaje) {
        String nombre;

        do {
            System.out.print(mensaje);
            nombre = sc.nextLine().trim();

            // Validamos que solo tenga letras (incluye acentos) y espacios
            if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                System.out.println("Error: El nombre solo puede contener letras y espacios.");
                nombre = null; // fuerza repetir el bucle
            }

        } while (nombre == null || nombre.isEmpty());

        return nombre;
    }


    public static boolean pedirSiNo( String mensaje) {
        String entrada;

        while (true) {
            System.out.print(mensaje + " (s/n): ");
            entrada = sc.nextLine().trim().toLowerCase();

            if (entrada.equals("s") || entrada.equals("si") || entrada.equals("sí") || entrada.equals("y") || entrada.equals("yes")) {
                return true;
            } else if (entrada.equals("n") || entrada.equals("no")) {
                return false;
            } else {
                System.out.println("Error: Responde solo 's' o 'n'.");
            }
        }
    }

}



