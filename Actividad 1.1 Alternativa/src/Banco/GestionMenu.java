package Banco;

import java.util.Scanner;

public class GestionMenu {
    GestionCuenta gc = new GestionCuenta();
    Scanner sc = new Scanner(System.in);


    public void menuFlow(){
        int eleccion =0;
        gc.inicializarArchivos();
        gc.comprobarCuenta();
            while(eleccion!=6){
                eleccion = soutsMenu();
                switch (eleccion){
                    case 1:
                        gc.consultarSaldo();
                        break;
                    case 2:
                        gc.consultarCuenta();
                        break;
                    case 3:
                        gc.mostrarMovimientos();
                        break;
                    case 4:
                        gc.hacerIngreso();
                        break;
                    case 5:
                        gc.hacerRetirada();
                        break;
                    case 6:
                        break;
                }

            }
    }


    public int soutsMenu(){
        System.out.println("=== GESTIÓN DE CUENTA BANCARIA ===");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Consultar datos del titular");
        System.out.println("3. Ver historial de movimientos");
        System.out.println("4. Realizar ingreso");
        System.out.println("5. Realizar retirada");
        System.out.println("6. Salir");
        return eleccion();
    }
    public int eleccion(){
        while(true){
            try{
                System.out.print("Seleccione una opción: ");
                int eleccion = sc.nextInt();
                sc.nextLine();
                return  eleccion;
            } catch (Exception e) {
                System.out.println("Opción no válida");
            }

        }

    }


































}
