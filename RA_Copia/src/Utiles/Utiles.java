package Utiles;

import POJO.Cuenta;
import POJO.Movimiento;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

// Clase de utilidades para operaciones de entrada/salida y gestión de archivos
public class Utiles implements Serializable {
    // Identificador de versión para la serialización
    private static final long serialVersionUID = 1L;
    // Ruta del archivo donde se guarda la cuenta
    private static final String archivo = "datos/cuenta.dat";
    // Instancia de Movimiento para operaciones
    Movimiento mov = new Movimiento();
    // Scanner para entrada de datos por consola (no se serializa)
    public static Scanner sc = new Scanner(System.in);


    // Método para crear la carpeta y el archivo de datos si no existen
    public static void crearArchivos() {
        boolean creado = false;
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            creado = carpeta.mkdir();
            System.out.println("Carpeta creada");
        }
        if (creado) {
            try {
                File ar = new File(archivo);
                if (!ar.exists()) {
                    ar.createNewFile();
                    System.out.println("Archivo creado");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Método para guardar una cuenta en el archivo
    public  static void guardarCuenta(Cuenta cuenta){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(cuenta);
            System.out.println("Cuenta guardada correctamente.");
        } catch (IOException e) {
            // Capturamos cualquier error de escritura
            System.out.println("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    // Método para cargar una cuenta desde el archivo
    public static Cuenta cargarCuenta(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
            Cuenta cuentaCargada = (Cuenta) ois.readObject();
            System.out.println("Cuenta cargada correctamente.");
            return cuentaCargada;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la cuenta: " + e.getMessage());
            return new Cuenta(); // si hay error, devolvemos cuenta vacía
        }
    }

    // Método para pedir un número decimal positivo por consola
    public static double pedirDouble() {
        double numero = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print("Introduce un número positivo: ");
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
    public static int pedirInt() {
        int num = -1;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print("Introduce un número: ");
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
        return num;
    }

    // Método para pausar la ejecución hasta que el usuario pulse una tecla
    public static void saltoLinea() {
        System.out.println("Pulsa ENTER para continuar...");
        new Scanner(System.in).nextLine();
    }

}
