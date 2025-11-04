package POJOs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    String nombre;

    double saldo;

    String dni;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public Cliente(String nombre,double saldo, String Dni) {
        this.nombre = nombre;
        this.saldo = saldo;
        this.dni = Dni;
    }
    public Cliente(String nombre, String Dni) {
        this.nombre = nombre;
        this.saldo = 0;
        this.dni = Dni;
    }

    public void actualizarSaldo(double saldo){
        this.saldo=+saldo;
    }



}
