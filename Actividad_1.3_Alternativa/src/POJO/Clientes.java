package POJO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase Clientes
 * - Representa operaciones sobre los clientes almacenados en el archivo
 *   "datos_libreria/Clientes.txt".
 * - Cada cliente se almacena en una línea con formato: DNI;Nombre;Email;Telefono;Cantidad;
 *
 * Notas:
 * - La clase mezcla responsabilidades de modelo (POJO) con operaciones de E/S y
 *   de interacción por consola. En aplicaciones mayores conviene separar estas capas.
 */
public class Clientes {
    // Campos que representan la información de un cliente
    private String Dni;      // DNI del cliente (identificador)
    private String Nombre;   // Nombre completo
    private String Email;    // Correo electrónico
    private int Numero;      // Teléfono (almacenado como int en este proyecto)
    private int Cantidad;    // Cantidad total de compras (o unidades)
    static Scanner sc = new Scanner(System.in); // Scanner compartido para lectura desde consola
    String archivo = "datos_libreria/Clientes.txt"; // Ruta relativa al archivo de datos

    // Getters y setters (métodos simples para acceder y modificar campos)
    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    // Constructores
    public Clientes(String dni, String nombre, String email, int cantidad, int numero) {
        Dni = dni;
        Nombre = nombre;
        Email = email;
        Cantidad = cantidad;
        Numero = numero;
    }

    public Clientes() {
    }

    /**
     * Crear el directorio y el archivo si no existen.
     * - Crea la carpeta "datos_libreria" si no existe.
     * - Crea el archivo "Clientes.txt" si no existe.
     */
    public void crearArchivosClientes() {
        File carpeta = new File("datos_libreria");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        try {
            File archivoCliente = new File(archivo);
            if (!archivoCliente.exists()) {
                archivoCliente.createNewFile();
            }
        } catch (IOException e) {
            // Mostrar el mensaje de error en caso de fallo de I/O
            System.out.println(e.getMessage());
        }
    }

    /**
     * validarDNI
     * - Lee el archivo de clientes línea a línea y comprueba si el DNI ya existe.
     * - Devuelve false si encuentra el DNI (no válido para insertar), true si no existe.
     *
     * Observación: el método asume que cada línea está en formato con 5 campos separados
     * por ';'. Si una línea no tiene 5 campos la ignora.
     */
    public boolean validarDNI(String dni) {
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    if (partes[0].equalsIgnoreCase(dni)) {
                        return false; // DNI ya existe
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true; // DNI no encontrado
    }

    /**
     * escribirClientes
     * - Añade un cliente al final del archivo si el DNI es válido (no existe).
     * - Forma la línea con los campos separados por ';' y escribe una nueva línea.
     */
    public void escribirClientes(Clientes cliente) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(archivo,true))) {
            if (validarDNI(cliente.getDni())) {
                String linea = (cliente.getDni() + ";" +
                        cliente.getNombre() + ";" +
                        cliente.getEmail() + ";" +
                        cliente.getNumero() + ";" +
                        cliente.getCantidad() + ";"
                );
                bf.newLine();
                bf.write(linea);

            } else {
                System.out.println("Error al insertar cliente: ");
            }
        } catch (IOException e) {
            // Se captura la excepción pero sólo se llama a getMessage();
            // sería mejor imprimirla o manejarla de forma más explícita.
            e.getMessage();
        }
    }

    /**
     * altaCliente
     * - Pide por consola los datos del cliente y lo escribe en el archivo.
     * - Comprueba primero que el DNI no exista para evitar duplicados.
     */
    public void altaCliente() {
        try {
            System.out.println("Dime el Dni que quieras añadir: ");
            String dni = sc.nextLine();
            if (!validarDNI(dni)) {
                System.out.println("Dni ya existe");
            } else {
                System.out.println("Dime el nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Dime el Email: ");
                String email = sc.nextLine();
                System.out.println("Dime el numero: ");
                int tlf = sc.nextInt();
                sc.nextLine();
                int cantidad = 0; // nuevo cliente empieza con cantidad 0
                escribirClientes(new Clientes(dni, nombre, email, cantidad,tlf));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * mostrarClientes
     * - Lee todas las líneas del archivo e imprime los clientes en consola.
     * - Ignora líneas que no tengan exactamente 5 campos.
     */
    public void mostrarClientes() {
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    System.out.println(" DNI: " + partes[0] + " Nombre: " + partes[1] + " Email: " + partes[2] + " Telefono: " + partes[3] + " Cantidad: " + partes[4]);
                }
            }
            System.out.println("Ya no quedan mas clientes");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * buscarClienteDni
     * - Pide un DNI por consola y busca el cliente correspondiente en el archivo.
     * - Usa validarDNI para comprobar existencia: si validarDNI devuelve true -> no existe.
     */
    public void buscarClienteDni() {
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            System.out.println("Que cliente quieres buscar(Dime el dni): ");
            String dni = sc.nextLine();
            if (validarDNI(dni)) {
                System.out.println("El dni no existe en nuestros datos");
            } else {
                String linea;
                while ((linea = bf.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length == 5) {
                        if (partes[0].equalsIgnoreCase(dni)) {
                            System.out.println(" DNI: " + partes[0] + " Nombre: " + partes[1] + " Email: " + partes[2] + " Telefono: " + partes[3] + " Cantidad: " + partes[4]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * mejores5Clientes
     * - Carga todos los clientes en una lista (siempre que la línea tenga 5 campos y
     *   los campos numéricos se puedan parsear), ordena por Cantidad y muestra los 5
     *   primeros.
     *
     * Nota: la línea que ordena contiene una multiplicación por -1 que podría estar
     * invirtiendo el criterio de orden. Observación añadida sin cambiar la lógica.
     */
    public void mejores5Clientes() {
        List<Clientes> clientes = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    try {
                        int cantidad = Integer.parseInt(partes[4]);
                        int numero = Integer.parseInt(partes[3]);
                        clientes.add(new Clientes(partes[0], partes[1], partes[2], cantidad, numero));
                    } catch (NumberFormatException e) {
                        // Si cantidad no es un número, lo ignoramos
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Ordenar por cantidad descendente
        // OBS: el uso de * -1 podría invertir el orden esperado; revisar si se desea
        clientes.sort((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()) * -1);

        // Mostrar los mejores 5
        System.out.println("Mejores 5 clientes:");
        for (int i = 0; i < Math.min(5, clientes.size()); i++) {
            Clientes c = clientes.get(i);
            System.out.println(" DNI: " + c.getDni() + " Nombre: " + c.getNombre() + " Email: " + c.getEmail() + " Telefono: " + c.getNumero() + " Cantidad: " + c.getCantidad());
        }
    }

   /**
    * actualizarCliente
    * - Genera un archivo temporal y reescribe todas las líneas, actualizando el
    *   correo o teléfono del cliente cuyo DNI indique el usuario.
    * - Si se produce una actualización, reemplaza el archivo original por el temporal.
    */
   public void actualizarCliente() {
       File temp = new File("datos_libreria/Clientestemp.txt");
       File archivin = new File(archivo);

       boolean actualizado = false;

       try (BufferedWriter bf = new BufferedWriter(new FileWriter(temp));
            BufferedReader bfr = new BufferedReader(new FileReader(archivin))) {
           System.out.println("¿De qué DNI quieres actualizar?");
           String dni = sc.nextLine();

           if (validarDNI(dni)) {
               System.out.println("Ese cliente no existe");
               temp.delete(); // eliminamos el archivo temporal si no se va a usar
               return;
           }

           String linea;
           while ((linea = bfr.readLine()) != null) {
               String[] partes = linea.split(";");
               if (partes.length == 5 && partes[0].equalsIgnoreCase(dni)) {
                   boolean cambioHecho = false;
                   while (!cambioHecho) {
                       System.out.println("¿Qué quieres cambiar, el correo o el teléfono?");
                       String eleccion = sc.nextLine();
                       if (eleccion.equalsIgnoreCase("correo")) {
                           System.out.println("¿Cuál es el nuevo correo?");
                           String correo = sc.nextLine();
                           linea = partes[0] + ";" + partes[1] + ";" + correo + ";" + partes[3] + ";" + partes[4];
                           cambioHecho = true;
                           actualizado = true;
                       } else if (eleccion.equalsIgnoreCase("telefono")) {
                           System.out.println("¿Cuál es el nuevo teléfono?");
                           String tlf = sc.nextLine();
                           linea = partes[0] + ";" + partes[1] + ";" + partes[2] + ";" + tlf + ";" + partes[4];
                           cambioHecho = true;
                           actualizado = true;
                       } else {
                           System.out.println("Opción no válida, escribe 'correo' o 'telefono'.");
                       }
                   }
               }
               bf.write(linea);
               bf.newLine();
           }
       } catch (IOException e) {
           System.out.println(e.getMessage());
       }
       if (actualizado) {
           if (archivin.delete()) {
               if (!temp.renameTo(archivin)) {
                   System.out.println("No se pudo renombrar el archivo temporal.");
               }
           } else {
               System.out.println("No se pudo borrar el archivo original.");
           }
       } else {
           System.out.println("No se realizó ninguna actualización.");
           temp.delete(); // eliminamos el archivo temporal si no se va a usar
       }
   }
        }
