import POJO.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cuenta {
    Scanner sc = new Scanner(System.in);
    Cliente cl = new Cliente("Rafa",25,1500);
        public void guardarCuenta(){

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos/cuenta.dat"))) {
                oos.writeObject(cl);
                System.out.println("Objeto serializado correctamente.");
            } catch (IOException e) {
                System.out.println("Error al escribir objeto: " + e.getMessage());
            }
        }

    public void iniciarSesion()  {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("datos/cuenta.dat"))) {
            Cliente c = (Cliente) ois.readObject();
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Edad: " + c.getEdad());
            System.out.println("Saldo: " + c.getSaldo());

        } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al leer objeto: " + e.getMessage());

        }

    }
    public void Menu() {
        System.out.println("Iniciando sesion");
        try{
        iniciarSesion();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (iniciarSesion()) {
            int eleccion = 0;
            while (eleccion != 4) {
                System.out.println("Que quieres hacer: ");
                System.out.println("1. Ingresar Dinero");
                System.out.println("2. Sacar Dinero");
                System.out.println("3. Consultar Saldo y Lista de movimientos");
                System.out.println("4. Salir");
                eleccion = eleccion();
                switch (eleccion) {
                    case 1:
                        cl.ingresarDinero();
                        break;
                    case 2:
                        cl.sacarDinero();
                        break;
                    case 3:
                        cl.mostrarMovimientos();
                        break;
                    case 4:
                        // salir del menú
                        break;
                    default:
                        System.out.println("Numero no valido");
                }
            }
        } else {
            guardarCuenta();
        }
    }

    public int eleccion(){
        System.out.println("Que escoges: ");
        int eleccion = sc.nextInt();
        sc.nextLine();
        return eleccion;
    }

}
