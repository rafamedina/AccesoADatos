import POJO.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// Clase Cuenta que representa una cuenta bancaria asociada a un cliente
public class Cuenta implements Serializable {
    // Identificador de versión para la serialización
    private static final long serialVersionUID = 1L;
    // Cliente asociado a la cuenta
    private Cliente cliente;
    // Utilidad para operaciones varias (no se serializa)
    transient Utiles ut = new Utiles();
    // Scanner para entrada de datos por consola (no se serializa)
    transient Scanner sc = new Scanner(System.in);
    // Lista de movimientos realizados en la cuenta
    private ArrayList<Movimiento> lista = new ArrayList<>();

    // Constructor que recibe un cliente
    public Cuenta(Cliente cliente) {
        this.cliente = cliente;
    }

    // Constructor por defecto
    public Cuenta(){
        Scanner sc = new Scanner(System.in);
        Utiles ut = new Utiles();
    }

    // Getter del cliente
    public Cliente getCliente() {
        return cliente;
    }

    // Setter del cliente
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // Método para ingresar dinero en la cuenta
    public void ingresarDinero() {
        Date fecha = new Date(); // Fecha actual
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy"); // Formato de fecha
        try{
            System.out.println("Cuanto dinero quieres ingresar: ");
            double ingreso = ut.pedirDouble(); // Solicita cantidad a ingresar
            cliente.actualizarSaldo(ingreso); // Actualiza saldo del cliente
            System.out.println("Dime el concepto para esta acción: ");
            String concepto = sc.nextLine(); // Solicita concepto
            // Añade movimiento de ingreso a la lista
            lista.add(new Movimiento(true, formato.format(fecha), ingreso, concepto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Método para retirar dinero de la cuenta
    public void retirarDinero(){
        Date fecha = new Date(); // Fecha actual
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy"); // Formato de fecha
        try{
            System.out.println("Cuanto dinero quieres Retirar: ");
            double ingreso = ut.pedirDouble(); // Solicita cantidad a retirar
            if(ingreso<= cliente.getSaldo()){
                cliente.actualizarSaldo(-ingreso); // Actualiza saldo del cliente
                System.out.println("Dime el concepto para esta acción: ");
                String concepto = sc.nextLine(); // Solicita concepto
                // Añade movimiento de retirada a la lista
                lista.add(new Movimiento(false, formato.format(fecha), -ingreso, concepto));
            } else {
                System.out.println("No hay dinero en la cuenta para realizar esta acción");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Método para consultar el saldo actual de la cuenta
    public void consultarSaldo(){
        System.out.println("El saldo de la cuenta es de: " + cliente.getSaldo());
    }

    // Método para mostrar todos los movimientos realizados en la cuenta
    public void mostrarMovimientos(){
        for(Movimiento mov : lista){
            System.out.println("---------------------------------------------------------");
            System.out.println("Acción: " + (mov.isTipo() ? "INGRESO" : "RETIRADA" ) + " Fecha: " + mov.getFecha() + " Cantidad: " + mov.getCantidad() + " Concepto: " + mov.getConcepto() );
        }
    }
}
