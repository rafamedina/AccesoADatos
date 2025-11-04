package POJO;

public class Cliente {
    private String dni;
    private String nombre;
    private String nCuenta;
    private double Saldo;

    public Cliente(String dni, String nCuenta, String nombre) {
        this.dni = dni;
        this.nCuenta = nCuenta;
        this.nombre = nombre;
        this.Saldo = 0;
    }

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

    public String getnCuenta() {
        return nCuenta;
    }

    public void setnCuenta(String nCuenta) {
        this.nCuenta = nCuenta;
    }

    public Double getSaldo() {
        return Saldo;
    }

    public void setSaldo(Double saldo) {
        Saldo = saldo;
    }

    public void actualizarSaldo(Double Cantidad){
        this.Saldo += Cantidad;
    }
}
