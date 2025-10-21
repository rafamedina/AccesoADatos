import POJO.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    private Cliente cliente;
    Utiles ut = new Utiles();
    final String archivo = "datos/cuenta.dat";
    Scanner sc = new Scanner(System.in);
    private ArrayList<Movimiento> lista = new ArrayList<>();

    public Cuenta(Cliente cliente) {
        this.cliente = cliente;
    }
    public Cuenta(){

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void ingresarDinero() {
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
    try{
        System.out.println("Cuanto dinero quieres ingresar: ");
        double ingreso = ut.pedirDouble();
        cliente.actualizarSaldo(ingreso);
        System.out.println("Dime el concepto para esta acción: ");
        String concepto = sc.nextLine();
        lista.add(new Movimiento(true, formato.format(fecha), ingreso, concepto));

    } catch (Exception e) {
        throw new RuntimeException(e);
    }}

    public void retirarDinero(){
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        try{
            System.out.println("Cuanto dinero quieres Retirar: ");
            double ingreso = ut.pedirDouble();
            if(ingreso<= cliente.getSaldo()){
                cliente.actualizarSaldo(-ingreso);
                System.out.println("Dime el concepto para esta acción: ");
                String concepto = sc.nextLine();
                lista.add(new Movimiento(false, formato.format(fecha), -ingreso, concepto));

            } else {
                System.out.println("No hay dinero en la cuenta para realizar esta acción");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);

        }}

    public void consultarSaldo(){
        System.out.println("El saldo de la cuenta es de: " + cliente.getSaldo());
    }

    public void mostrarMovimientos(){
        for(Movimiento mov : lista){
            System.out.println("---------------------------------------------------------");
            System.out.println("Acción: " + (mov.isTipo() ? "INGRESO" : "RETIRADA" ) + " Fecha: " + mov.getFecha() + " Cantidad: " + mov.getCantidad() + " Concepto: " + mov.getConcepto() );
        }
    }



        }































































