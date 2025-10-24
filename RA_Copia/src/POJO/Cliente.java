package POJO;

import java.io.Serializable;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private String Dni;
    private String Nombre;
    private String Apellido;
    private String NCuenta;
    private double Saldo;

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

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double saldo) {
        Saldo = saldo;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getNCuenta() {
        return NCuenta;
    }

    public void setNCuenta(String NCuenta) {
        this.NCuenta = NCuenta;
    }

    public Cliente() {
    }

    public void actualizarSaldo(double cantidad){
        this.Saldo += cantidad;
    }

    public Cliente(String dni, String nombre, String apellido, String NCuenta, double saldo) {
        Dni = dni;
        Nombre = nombre;
        Apellido = apellido;
        this.NCuenta = NCuenta;
        Saldo = saldo;
    }
}
