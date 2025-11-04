package Cuenta;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ManejarCuenta {
    private ArrayList<Cuenta> lista = new ArrayList<>();
    private final String FILE_PATH = "datos/cuentas.dat";
    static Scanner sc = new Scanner(System.in);

    // Guarda o actualiza el archivo con la lista completa
    public void serializarCuenta(Cuenta cuenta) {
        // Primero cargamos los datos previos si existen
        lista = deserializarCuentas();
        lista.add(cuenta);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(lista);
            System.out.println("Cuenta guardada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir objeto: " + e.getMessage());
        }
    }

    // Carga todas las cuentas desde el archivo

    private ArrayList<Cuenta> deserializarCuentas() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<Cuenta>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Busca una cuenta por DNI
    public Cuenta iniciarSesion(String dni) {
        lista = deserializarCuentas();

        for (Cuenta cuenta : lista) {
            if (cuenta.getDni().equals(dni)) {
                System.out.println("Cliente existente. Cargando cuenta...");
                return cuenta;
            }
        }

        return null; // no encontrada
    }

}
