package POJO;

public class Movimientos
{
    private boolean tipo;
    private double cantidad;
    private String fecha;
    private String concepto;

    public Movimientos(boolean tipo, double cantidad, String concepto) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.concepto = concepto;
    }

    public boolean getTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
}
