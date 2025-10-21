package POJO;

import java.io.Serializable;

public class Movimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean Tipo;
    private double Cantidad;
    private String Concepto;
    private String fecha;

    public boolean isTipo() {
        return Tipo;
    }

    public void setTipo(boolean tipo) {
        Tipo = tipo;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public String getConcepto() {
        return Concepto;
    }

    public void setConcepto(String concepto) {
        Concepto = concepto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Movimiento() {
    }

    public Movimiento(boolean tipo, String fecha, double cantidad, String concepto) {
        Tipo = tipo;
        this.fecha = fecha;
        Cantidad = cantidad;
        Concepto = concepto;
    }
}
