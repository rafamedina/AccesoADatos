package POJO;

public class Cliente {
    private String nombre;
    private String DNI;
    private String numeroDeCuenta;
    private double saldo;


    public Cliente(String numeroDeCuenta, String nombre, String DNI) {
        this.numeroDeCuenta = numeroDeCuenta;
        this.nombre = nombre;
        this.DNI = DNI;
        this.saldo = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void cambiarSaldo(double saldo) {
        this.saldo += saldo;
    }
}
