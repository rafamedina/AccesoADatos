package Banco;
import POJO.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GestionCuentas {
    // Scanner compartido para leer la entrada estándar
    static Scanner sc = new Scanner(System.in);
    // Contador simple para generar números de cuenta (no persistente entre ejecuciones)
    int contador = 1;
    // Referencia al cliente actualmente en sesión (si se ha iniciado)
    Cliente cliente;

    /**
     * Inicializa el programa asegurando que exista la carpeta "datos" y los
     * archivos necesarios (cuentas.txt y movimientos.txt). Si no existen, los crea.
     */
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

    /**
     * Crea una nueva cuenta pidiendo DNI y nombre por consola. Antes de crearla
     * comprueba que no exista ya un registro con el mismo DNI en "datos/cuentas.txt".
     */
    public void crearCuenta() {

            System.out.print("Dime tu DNI: ");
            String dni = sc.nextLine();

            boolean existe = false;

            // Recorre el archivo de cuentas buscando si ya existe el DNI
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
                // Si el archivo no existe, no hacemos nada aquí porque
                // luego al guardar se creará (o ya lo creó inicializarPrograma)
            }

            if (existe) {
                System.out.println("Esta cuenta ya existe");
            } else {
                // Pide nombre y genera un número de cuenta simple
                System.out.print("Dime tu nombre: ");
                String nombre = sc.nextLine();
                String nCuenta = "ES" + contador;
                contador++;
                // Crea el objeto Cliente y lo guarda en el archivo
                cliente = new Cliente(dni,nCuenta, nombre);
                guardarCliente();
                System.out.println("Cuenta creada correctamente.");
            }
        }

    /**
     * Guarda (o actualiza) la información del cliente actual en "datos/cuentas.txt".
     * El formato por línea es: DNI;Nombre;NumeroCuenta;Saldo
     * Si ya existía una línea con el mismo DNI se elimina y se reemplaza por la nueva.
     */
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
            // Si el archivo no existe, lo creamos luego al escribir con FileWriter
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

    /**
     * Inicia sesión pidiendo el DNI y buscando en el fichero de cuentas. Si encuentra
     * la línea correspondiente, crea el objeto Cliente en memoria y devuelve true.
     */
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
                        // Crea el cliente en memoria y asigna saldo
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

    /**
     * Guarda un movimiento asociado al cliente actualmente logueado.
     * El formato en el fichero movimientos.txt es: DNI;TIPO;Cantidad;Fecha;Concepto;
     */
    public void guardarMovimientos(Movimientos mov){
        try (BufferedWriter bf = new BufferedWriter(new FileWriter("datos/movimientos.txt", true))) {
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            // tipo true -> INGRESO, false -> RETIRADA
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
    /**
     * Lee y muestra todos los movimientos correspondientes al DNI del cliente en sesión.
     */
    public void leerMovimientos(){
        try (Scanner scanner = new Scanner(new File("datos/movimientos.txt"))) {
            boolean encontrado = false;
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(";");
                if (partes.length >= 5) {
                    String dni = partes[0];
                    if (cliente.getDni().equalsIgnoreCase(dni)) {
                        // Imprime la línea completa tal cual está en el fichero
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

    /**
     * Lista todas las cuentas (muestra cada línea del fichero de cuentas).
     */
    public void listarCuentas(){
        try (Scanner scanner = new Scanner(new File("datos/cuentas.txt"))) {

            while(scanner.hasNext()){
                System.out.println(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Muestra estadísticas: número de cuentas y saldo total. Atención: aquí se
     * corrige una suma errónea usando '+=' en lugar de '=+' (se comenta la intención).
     */
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
                    // NOTA: en el código original estaba `total =+ saldo;` lo cual asigna +saldo.
                    // Aquí se mantiene la intención correcta: acumular el saldo.
                    total += saldo;

                }
            }
            System.out.println("El numero de cuentas es de : " + contadorC);
            System.out.println("El saldo total es de : " + total);

        } catch (FileNotFoundException e){
            e.getMessage();
        }
    }

    /**
     * Muestra el saldo del cliente en sesión.
     */
    public void consultarSaldo() {
        try {
            System.out.println("El saldo es de: " + cliente.getSaldo());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Muestra datos del cliente en sesión. Observa que las etiquetas estaban
     * mezcladas en el original (nombre/cuenta). Se conserva el comportamiento
     * pero se comentan las líneas para entender qué imprime cada una.
     */
    public void mostrarDatos() {
        try {
            // Imprime el número de cuenta (getnCuenta) pero la etiqueta dice "nombre"
            System.out.println("El nommbre es : " + cliente.getnCuenta());
            // Imprime el DNI
            System.out.println("El DNI es : " + cliente.getDni());
            // Imprime el nombre pero la etiqueta dice "La cuenta es"
            System.out.println("La cuenta es : " + cliente.getNombre());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Realiza un ingreso: pide cantidad, actualiza el saldo del cliente y guarda
     * el movimiento en el fichero.
     */
    public void hacerIngreso() {
        try {
            System.out.println("Que cantidad quieres ingresar: ");
            double cantidad = sc.nextDouble();
            sc.nextLine();
            // Actualiza saldo sumando la cantidad
            cliente.actualizarSaldo(cantidad);
            System.out.println("Dime un concepto: ");
            String concepto = sc.nextLine();
            guardarMovimientos(new Movimientos(true, cantidad, concepto));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Realiza una retirada solicitando la cantidad y comprobando que hay saldo
     * suficiente. Si es válido, actualiza saldo y guarda el movimiento.
     */
    public void hacerRetirada() {
        try {
            while(true) {
                System.out.println("Que cantidad quieres retirar: ");
                double cantidad = sc.nextDouble();
                sc.nextLine();
                if (cantidad <= cliente.getSaldo()) {
                    // Resta del saldo (se pasa -cantidad a actualizarSaldo)
                    cliente.actualizarSaldo(-cantidad);
                    System.out.println("Dime un concepto: ");
                    String concepto = sc.nextLine();
                    // En Movimientos se marca como retirada; en el fichero se guarda la cantidad negativa
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
