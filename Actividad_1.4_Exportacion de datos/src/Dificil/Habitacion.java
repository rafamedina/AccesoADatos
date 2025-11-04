package Dificil;

public class Habitacion {
    private int numero;
    private String tipo; // Individual, Doble, Suite
    private double precioPorNoche;
    private boolean disponible;

    // Constructor vacío
    public Habitacion() {}

    // Constructor completo
    public Habitacion(int numero, String tipo, double precioPorNoche, boolean disponible) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        this.disponible = disponible;
    }

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(double precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "numero=" + numero +
                ", tipo='" + tipo + '\'' +
                ", precioPorNoche=" + precioPorNoche +
                ", disponible=" + disponible +
                '}';
    }
}