package POJO;

public class Movimiento {
    private boolean Tipo;
    private double Cantidad;
    private String Fecha;
    private String Concepto;

    public Movimiento(boolean tipo, double cantidad, String concepto) {
        Tipo = tipo;
        Cantidad = cantidad;
        Concepto = concepto;
    }

    public boolean getTipo() {
        return Tipo;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getConcepto() {
        return Concepto;
    }
}
