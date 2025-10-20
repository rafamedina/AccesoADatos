import POJO.Cliente;

import java.io.Serializable;
import java.util.Scanner;

public class GestionCuenta implements Serializable {
    Utiles ut = new Utiles();
    Cuenta cuenta;
    transient Scanner sc = new Scanner(System.in);



    public void Menu(){
        int eleccion = 1;
    iniciarCuenta();
    while(eleccion!=0){
        eleccion = MenuOperaciones();
        switch (eleccion){
            case 1:
                cuenta.ingresarDinero();
                break;
            case 2:
                cuenta.retirarDinero();
                break;
            case 3:
                cuenta.consultarSaldo();
                break;
            case 4:
                cuenta.mostrarMovimientos();
                break;
            case 0:
                guardarCuenta();
                break;
            default:
                System.out.println("Opción no válida");

        }
    }

    }


    public int MenuOperaciones(){
        System.out.println("1. Ingresar Dinero");
        System.out.println("2. Sacar Dinero");
        System.out.println("3. Mostrar Saldo");
        System.out.println("4. Mostrar Movimientos");
        System.out.println("0. Salir");
        return ut.pedirInt();
    }


    public boolean iniciarSesion(){
        try{
            cuenta = ut.cargarCuenta();
            return  true;
        } catch (Exception e ){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public void guardarCuenta(){
        try{
            ut.guardarCuenta(cuenta);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void iniciarCuenta(){
        double saldo = 0;
        try{
            ut.crearArchivos();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{
            if(cuenta != null){
                System.out.println("Iniciando Sesión....");
            } else {
                System.out.println("Dime un DNI para la cuenta: ");
                String dni = sc.nextLine();
                System.out.println("Dime un nombre para la cuenta: ");
                String nombre = sc.nextLine();
                System.out.println("Dime un Apellido para la cuenta: ");
                String apellido = sc.nextLine();
                System.out.println("Dime un Numero de cuenta: ");
                String Ncuenta = sc.nextLine();

                System.out.println("Quieres ingresar un saldo para esta cuenta?(S/N)");
                String sino = sc.nextLine();
                if(sino.equalsIgnoreCase("S")){
                    System.out.println("Cuanto quieres ingresar: ");
                    saldo = ut.pedirDouble();
                }
                Cliente newcliente = new Cliente(dni,nombre,apellido,Ncuenta,saldo);
                cuenta = new Cuenta(newcliente);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
