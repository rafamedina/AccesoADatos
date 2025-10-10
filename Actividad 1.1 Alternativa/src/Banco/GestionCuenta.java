package Banco;
import POJO.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GestionCuenta {
    Scanner sc = new Scanner(System.in);
    Cliente cliente;
    int contador = 0;

    public GestionCuenta() {
    }
    public void inicializarArchivos() {
        // Crear carpeta "datos" si no existe
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        // Crear archivos si no existen
        try {
            File titular = new File("datos/titular.txt");
            if (!titular.exists()) {
                titular.createNewFile();
            }
            File saldo = new File("datos/saldo.txt");
            if (!saldo.exists()) {
                saldo.createNewFile();
            }
            File movimientos = new File("datos/movimientos.txt");
            if (!movimientos.exists()) {
                movimientos.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creando archivos: " + e.getMessage());
        }
    }

    public void comprobarCuenta() {
        // Comprobar si la cuenta existe leyendo el fichero
        try (BufferedReader Leertitular = new BufferedReader(new FileReader("datos/titular.txt"))) {
            if (Leertitular.readLine() == null) {
                System.out.println("Dime tu nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Dime tu dni: ");
                String dni = sc.nextLine();
                String nCuenta = ("ES"  + contador);
                contador++;
                cliente = new Cliente(nCuenta, nombre, dni);
                // Guardar datos
                try (BufferedWriter Escribirtitular = new BufferedWriter(new FileWriter("datos/titular.txt"))) {
                    Escribirtitular.write(cliente.getNombre() + "\n");
                    Escribirtitular.write(cliente.getDNI() + "\n");
                    Escribirtitular.write(cliente.getNumeroDeCuenta() + "\n");
                }
                registrarSaldo(cliente.getSaldo());
            } else {
                cargarCuenta();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void registrarMovimientos(Movimiento movimiento) {
        try (BufferedWriter Escribirmovimientos = new BufferedWriter(new FileWriter("datos/movimientos.txt", true))) {
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String tipo = movimiento.getTipo() ? "INGRESO" : "RETIRADA";
            String linea = tipo + ";" +
                    movimiento.getCantidad() + ";" +
                    formato.format(fechaActual) + ";" +
                    movimiento.getConcepto() + "\n";
            Escribirmovimientos.write(linea);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void registrarSaldo(double Saldo){
        try (BufferedWriter Escribirsaldo = new BufferedWriter(new FileWriter("datos/saldo.txt"))) {
            Escribirsaldo.write(String.valueOf(Saldo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void hacerIngreso() {
        try {
            System.out.println("Que cantidad quieres ingresar: ");
            double cantidad = sc.nextDouble();
            sc.nextLine();
            cliente.cambiarSaldo(cantidad);
            System.out.println("Dime un concepto: ");
            String concepto = sc.nextLine();
            registrarMovimientos(new Movimiento(true, cantidad, concepto));
            registrarSaldo(cliente.getSaldo());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void hacerRetirada() {
        try {
            while(true) {
                System.out.println("Que cantidad quieres retirar: ");
                double cantidad = sc.nextDouble();
                sc.nextLine();
                if (cantidad <= cliente.getSaldo()) {
                    cliente.cambiarSaldo(-cantidad);
                    System.out.println("Dime un concepto: ");
                    String concepto = sc.nextLine();
                    registrarMovimientos(new Movimiento(false, -cantidad, concepto));
                    registrarSaldo(cliente.getSaldo());
                    break;
                } else {
                    System.out.println("No puedes sacar mas dinero que el que tienes");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void consultarSaldo() {
        try (BufferedReader Leersaldo = new BufferedReader(new FileReader("datos/saldo.txt"))) {
            System.out.println("El saldo es de: " + Leersaldo.readLine());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void consultarCuenta() {
        try (BufferedReader Leertitular = new BufferedReader(new FileReader("datos/titular.txt"))) {
            String linea;
            while ((linea = Leertitular.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarMovimientos(){
        try (BufferedReader Leermovimientos = new BufferedReader(new FileReader("datos/movimientos.txt"))) {
            String linea;
            while ((linea = Leermovimientos.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarCuenta()  {
        try (BufferedReader Leertitular = new BufferedReader(new FileReader("datos/titular.txt"));
             BufferedReader Leersaldo = new BufferedReader(new FileReader("datos/saldo.txt"))) {

            String nombreCompleto = Leertitular.readLine(); // Primera línea: nombre
            String dni = Leertitular.readLine();            // Segunda línea: DNI
            String numeroCuenta = Leertitular.readLine();   // Tercera línea: número de cuenta
            cliente = new Cliente(nombreCompleto, dni, numeroCuenta);
            double saldo = Double.parseDouble(Leersaldo.readLine());
            cliente.setSaldo(saldo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


