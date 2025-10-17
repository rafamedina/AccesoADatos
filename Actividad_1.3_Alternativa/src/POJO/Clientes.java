package POJO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Clientes {
    private String Dni;
    private String Nombre;
    private String Email;
    private int Numero;
    private int Cantidad;
    static Scanner sc = new Scanner(System.in);
    String archivo = "datos_libreria/Clientes.txt";

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

    public Clientes(String dni, String nombre, String email, int cantidad, int numero) {
        Dni = dni;
        Nombre = nombre;
        Email = email;
        Cantidad = cantidad;
        Numero = numero;
    }

    public Clientes() {
    }

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
            System.out.println(e.getMessage());
        }
    }

    public boolean validarDNI(String dni) {
        try (BufferedReader bf = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = bf.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    if (partes[0].equalsIgnoreCase(dni)) {
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public void escribirClientes(Clientes cliente) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(archivo,true))) {
            if (validarDNI(cliente.getDni())) {
                String linea = (cliente.getDni() + ";" +
                        cliente.getNombre() + ";" +
                        cliente.getEmail() + ";" +
                        cliente.getNumero() + ";" +
                        cliente.getCantidad() + ";"
                );
                bf.write(linea);
                bf.newLine();
            } else {
                System.out.println("Error al insertar cliente: ");
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

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
                int cantidad = 0;
                escribirClientes(new Clientes(dni, nombre, email, cantidad,tlf));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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
        clientes.sort((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()) * -1);

        // Mostrar los mejores 5
        System.out.println("Mejores 5 clientes:");
        for (int i = 0; i < Math.min(5, clientes.size()); i++) {
            Clientes c = clientes.get(i);
            System.out.println(" DNI: " + c.getDni() + " Nombre: " + c.getNombre() + " Email: " + c.getEmail() + " Telefono: " + c.getNumero() + " Cantidad: " + c.getCantidad());
        }
    }

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






