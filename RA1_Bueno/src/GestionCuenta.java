import POJO.Cliente;
import POJO.Cuenta;
import static Utiles.Utiles.*;

import java.io.Serializable;


public class GestionCuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    Cuenta cuenta;



    public void Menu(){
        int eleccion = 1;
    iniciarCuenta();
    while(eleccion!=0){
        eleccion = MenuOperaciones();
        switch (eleccion){
            case 1:
                cuenta.ingresarDinero();
                saltoLinea();
                break;
            case 2:
                cuenta.retirarDinero();
                saltoLinea();
                break;
            case 3:
                cuenta.consultarSaldo();
                saltoLinea();
                break;
            case 4:
                cuenta.mostrarMovimientos();
                saltoLinea();
                break;
            case 0:
                guardarCuenta(cuenta);
                break;
            default:
                System.out.println("Opción no válida");

        }
    }

    }


    public int MenuOperaciones(){
        System.out.println("-----Menu-----");
        System.out.println("1. Ingresar Dinero");
        System.out.println("2. Sacar Dinero");
        System.out.println("3. Mostrar Saldo");
        System.out.println("4. Mostrar Movimientos");
        System.out.println("0. Salir");
        return pedirInt();
    }


    public boolean iniciarSesion() {
        cuenta = cargarCuenta();
        if (cuenta != null && cuenta.getCliente() != null) {
            System.out.println("Sesión iniciada correctamente.");
            return true;
        } else {
            System.out.println("No se encontró ninguna cuenta registrada.");
            return false;
        }
    }
    public void iniciarCuenta(){
        double saldo = 0;
        try{
            crearArchivos();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{

            if(iniciarSesion()){
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
                    saldo = pedirDouble();
                }
                Cliente newcliente = new Cliente(dni,nombre,apellido,Ncuenta,saldo);
                cuenta = new Cuenta(newcliente);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
