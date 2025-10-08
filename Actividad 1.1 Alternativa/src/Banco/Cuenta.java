package Banco;
import POJO.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Cuenta {
    Scanner sc = new Scanner(System.in);
    BufferedWriter Escribirtitular;
    BufferedWriter Escribirsaldo;
    BufferedWriter Escribirmovimientos;
    BufferedReader Leertitular;
    BufferedReader Leersaldo;
    BufferedReader Leermovimientos;
    Cliente cliente;
    int contador = 0;
    Movimiento movi;

    public Cuenta() {
        try {
            //Creamos contructor para que iniciar la cuenta me carguen todos los archivos y asi poder trabajar con ello, el true es para que me escriba al final y no me soobreescriba
            Escribirtitular = new BufferedWriter(new FileWriter("datos/titular.txt", true));
            Escribirsaldo = new BufferedWriter(new FileWriter("datos/saldo.txt"));
            Escribirmovimientos = new BufferedWriter(new FileWriter("datos/movimientos.txt", true));
            Leertitular = new BufferedReader(new FileReader("datos/titular.txt"));
            Leersaldo = new BufferedReader(new FileReader("datos/saldo.txt"));
            Leermovimientos = new BufferedReader(new FileReader("datos/movimientos.txt"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void comprobarCuenta() {
        try {//Si la cuenta esta vacia, procedemos a inciciar la sesion y crearla
            if (Leertitular.readLine() == null) {
                System.out.println("Dime tu nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Dime tu dni: ");
                String dni = sc.nextLine();
                System.out.println("Dime tu : ");
                String nCuenta;
                nCuenta = String.valueOf(contador);
                contador++;
                cliente = new Cliente(nCuenta, nombre, dni);
            }
            Escribirtitular.write(cliente.getNombre() + "\n");
            Escribirtitular.write(cliente.getDNI() + "\n");
            Escribirtitular.write(cliente.getNumeroDeCuenta() + "\n");
            Escribirsaldo.write(String.valueOf(cliente.getSaldo()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registrarMovimientos(Movimiento movimiento) {
        try {
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String tipo;
            if (movimiento.getTipo() == true) {
                tipo = "INGRESO";
            } else {
                tipo = "RETIRADA";
            }
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
        try{
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
            cliente.setSaldo(cantidad);
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
                cliente.setSaldo(-1 * cantidad);
                System.out.println("Dime un concepto: ");
                String concepto = sc.nextLine();
                registrarMovimientos(new Movimiento(false, -1 * cantidad, concepto));
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
        try{
            System.out.println("El saldo es de: " + Leersaldo.readLine());

        } catch (Exception e){

        }


    }

    public void consultarCuenta() {


    }


}


