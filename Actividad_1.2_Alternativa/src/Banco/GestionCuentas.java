package Banco;
import POJO.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GestionCuentas {
    static Scanner sc = new Scanner(System.in);
    int contador = 1;
    Cliente cliente;

    public void inicializarPrograma() {
        // Crear carpeta "datos" si no existe
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        // Crear archivos si no existen
        try {
            File cuenta = new File("datos/cuentas.txt");
            if (!cuenta.exists()) {
                cuenta.createNewFile();
            }
            File movimientos = new File("datos/movimientos.txt");
            if (!movimientos.exists()) {
                movimientos.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creando archivos: " + e.getMessage());
        }
    }

    public void crearCuenta() {

            System.out.print("Dime tu DNI: ");
            String dni = sc.nextLine();

            boolean existe = false;

            try (Scanner scanner = new Scanner(new File("datos/cuentas.txt"))) {
                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine();
                    String[] partes = linea.split(";");
                    if (partes.length >= 1 && partes[0].equalsIgnoreCase(dni)) {
                        existe = true;
                        break;
                    }
                }
            } catch (FileNotFoundException e) {

            }

            if (existe) {
                System.out.println("Esta cuenta ya existe");
            } else {
                System.out.print("Dime tu nombre: ");
                String nombre = sc.nextLine();
                String nCuenta = "ES" + contador;
                contador++;
                cliente = new Cliente(dni,nCuenta, nombre);
                guardarCliente();
                System.out.println("Cuenta creada correctamente.");
            }
        }

    public void guardarCliente() {
        StringBuilder contenido = new StringBuilder();
        boolean actualizado = false;
        String dniCliente = cliente.getDni();

        // Lee todas las líneas, omite la del cliente a actualizar
        try (Scanner scanner = new Scanner(new File("datos/cuentas.txt"))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(";");
                if (partes.length >= 1 && dniCliente.equalsIgnoreCase(partes[0])) {
                    // No guardamos la línea vieja, la eliminamos
                    actualizado = true;
                } else {
                    contenido.append(linea).append(System.lineSeparator());
                }
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, lo creamos luego
        }

        // Añade la línea nueva de ese cliente (actualizada)
        String nuevaLinea = cliente.getDni() + ";" +
                cliente.getNombre() + ";" +
                cliente.getnCuenta() + ";" +
                cliente.getSaldo();
        contenido.append(nuevaLinea).append(System.lineSeparator());

        // Sobrescribe el archivo con el contenido actualizado
        try (BufferedWriter bf = new BufferedWriter(new FileWriter("datos/cuentas.txt"))) {
            bf.write(contenido.toString());
            if (actualizado) {
                System.out.println("Cliente actualizado correctamente.");
            } else {
                System.out.println("Cliente guardado correctamente.");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
        }
    }
    public boolean iniciarSesion() {
        System.out.print("Dime un dni: ");
        String dni_metido = sc.nextLine();

        boolean encontrado = false;

        try (Scanner scanner = new Scanner(new File("datos/cuentas.txt"))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    String dni = partes[0];
                    if (dni_metido.equalsIgnoreCase(dni)) {
                        System.out.println("Dni encontrado...");
                        String nombreCompleto = partes[1];
                        String numeroCuenta = partes[2];
                        double saldo = Double.parseDouble(partes[3]);
                        cliente = new Cliente(dni, numeroCuenta, nombreCompleto);
                        cliente.setSaldo(saldo);
                        encontrado = true;
                        System.out.println("Sesion iniciada bienvenido: " + nombreCompleto);
                        return true;
                    }
                }
            }
            if (!encontrado) {
                System.out.println("Dni no encontrado.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }
        return false;
    }

    public void guardarMovimientos(Movimientos mov){
        try (BufferedWriter bf = new BufferedWriter(new FileWriter("datos/movimientos.txt", true))) {
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String tipo = mov.getTipo() ? "INGRESO":"RETIRADA";
            String linea = cliente.getDni() + ";" +
                    tipo + ";" +
                    mov.getCantidad() + ";" +
                    formato.format(fechaActual)+";" +
                    mov.getConcepto() + ";";
            bf.write(linea);
            bf.newLine();
            System.out.println("Movimiento guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el Movimiento: " + e.getMessage());
        }
    }
    public void leerMovimientos(){
        try (Scanner scanner = new Scanner(new File("datos/movimientos.txt"))) {
            boolean encontrado = false;
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(";");
                if (partes.length >= 5) {
                    String dni = partes[0];
                    if (cliente.getDni().equalsIgnoreCase(dni)) {
                        System.out.println(linea);
                        encontrado = true;
                    }
                }
            }
            if (!encontrado) {
                System.out.println("Movimientos a este dni no encontrados.");
            }
        } catch (Exception e) {
            System.out.println("Error al leer movimientos: " + e.getMessage());
        }
    }

    public void listarCuentas(){
        try (Scanner scanner = new Scanner(new File("datos/cuentas.txt"))) {

            while(scanner.hasNext()){
                System.out.println(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarEstadisticas(){
        try (Scanner cue = new Scanner(new File("datos/cuentas.txt")))
              {
            int contadorC=0;
            double total = 0;
            while(cue.hasNext()){
                contadorC ++;
                String linea = cue.nextLine();
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    double saldo = Double.parseDouble(partes[3]);
                    total =+ saldo;

                }
            }
            System.out.println("El numero de cuentas es de : " + contadorC);
            System.out.println("El saldo total es de : " + total);

        } catch (FileNotFoundException e){
            e.getMessage();
        }
    }

    public void consultarSaldo() {
        try {
            System.out.println("El saldo es de: " + cliente.getSaldo());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarDatos() {
        try {
            System.out.println("El nommbre es : " + cliente.getnCuenta());
            System.out.println("El DNI es : " + cliente.getDni());
            System.out.println("La cuenta es : " + cliente.getNombre());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void hacerIngreso() {
        try {
            System.out.println("Que cantidad quieres ingresar: ");
            double cantidad = sc.nextDouble();
            sc.nextLine();
            cliente.actualizarSaldo(cantidad);
            System.out.println("Dime un concepto: ");
            String concepto = sc.nextLine();
            guardarMovimientos(new Movimientos(true, cantidad, concepto));
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
                    cliente.actualizarSaldo(-cantidad);
                    System.out.println("Dime un concepto: ");
                    String concepto = sc.nextLine();
                    guardarMovimientos(new Movimientos(false, -cantidad, concepto));
                    break;
                } else {
                    System.out.println("No puedes sacar mas dinero que el que tienes");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
