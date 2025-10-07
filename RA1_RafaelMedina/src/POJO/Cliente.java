package POJO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente extends Movimiento implements Serializable {
    String nombre;
    int edad;
    double saldo;
    ArrayList<Movimiento> lista = new ArrayList<>();

    public ArrayList<Movimiento> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Movimiento> lista) {
        this.lista = lista;
    }

    public String getNombre() {
        return nombre;

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Cliente(String nombre, int edad, double saldo){
        super(0);
        this.nombre=nombre;
        this.edad=edad;
        this.saldo=saldo;

    }
    public Cliente() {
        super(0);
    }
    public void sacarDinero() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Cuanto quieres sacar");
            this.Movimiento = sc.nextDouble();
            sc.nextLine();
            if (this.Movimiento >= this.saldo) {
                this.saldo = -this.Movimiento;
                lista.add(new Movimiento(-1 * this.Movimiento) {
                });
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void ingresarDinero(){
        try{
        Scanner sc = new Scanner(System.in);
        System.out.println("Cuanto quieres sacar");
        double cantidad = sc.nextDouble();
        sc.nextLine();
        this.saldo =+ cantidad;
        lista.add(new Movimiento(this.Movimiento){
        });

    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarMovimientos(){
       for(Movimiento mov : lista){
           System.out.println(mov);
       }
        System.out.println("Saldo actual: " + getSaldo());
    }
}
