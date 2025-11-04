package Cuenta;

import POJOs.Cliente;
import POJOs.Movimiento;
import Utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Cuenta implements Serializable {
    Cliente cl;
    Scanner sc = new Scanner(System.in);
    Utils ut = new Utils();
    ArrayList<Movimiento> lista = new ArrayList<>();

    public Cuenta(String nombre, double saldo, String Dni){
        cl = new Cliente(nombre,saldo, Dni);
    }
    public Cuenta(){

    }
    public Cuenta(String nombre, String Dni){
        cl = new Cliente(nombre, Dni);
    }

    public int Menu(){
        System.out.println("Que quieres hacer: ");
        System.out.println("1. Ingresar Dinero");
        System.out.println("2. Sacar Dinero");
        System.out.println("3. Consultar Lista de movimientos");
        System.out.println("4. Consultar Saldo ");
        System.out.println("5. Salir ");
        return ut.eleccion();

    }

    public void ingresarDinero(){
        try{
            System.out.println("Cuanto quieres ingresar: ");
            double ingreso = sc.nextDouble();
            sc.nextLine();
            cl.actualizarSaldo(ingreso);
            lista.add(new Movimiento(ingreso));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void sacarDinero(){
        try{
            System.out.println("Cuanto quieres sacar: ");
            double retirada = sc.nextDouble();
            sc.nextLine();
            if(cl.getSaldo()>=retirada){
                cl.actualizarSaldo(-1*retirada);
                lista.add(new Movimiento(-1*retirada));
            } else {
                System.out.println("No hay suficiente cantidad en el banco");
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void mostrarMovimientos(){
        for(Movimiento mov : lista){
            int contador = 1;
            System.out.println("El movimiento numero " + contador + " es de: " + mov + "$");
            contador =+ 1;
        }
    }

    public void mostrarSaldo(){
        System.out.println("El saldo actual es de: " + cl.getSaldo());
    }

    public Cliente getCl() {
        return cl;
    }

    public Object getDni() {
        return cl.getDni();
    }
}
