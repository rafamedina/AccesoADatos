package Gestiones;
import POJO.*;
public class ControllerMenu {
    Libros libro = new Libros();
    Clientes cliente = new Clientes();
    Ventas ventas = new Ventas();
    Utils ut = new Utils();









    public void Controller() {
        int eleccion = 0;
        ventas.crearArchivosVentas();
        cliente.crearArchivosClientes();
        libro.crearArchivosLibro();

        while (eleccion != 4) {
            eleccion = ut.Menu();
            switch (eleccion) {
                case 1:
                    GestionLibros();
                    break;
                case 2:
                    GestionClientes();
                    break;
                case 3:
                    GestionVentas();
                    break;
                case 4:
                    System.out.println("Saliendo..\n");
                    break;
                default:
                    System.out.println("Numero no valido");
            }
        }
    }








    public void GestionVentas() {
        int eleccion = 0;
        while (eleccion != 6) {
            eleccion = ut.MenuVentas();
            switch (eleccion) {
                case 1:
                    ventas.registrarUnaVenta();
                    break;
                case 2:
                    ventas.mostrarFechas();
                    break;
                case 3:
                    ventas.mostrarVentasporDNI();
                    break;
                case 4:
                    ventas.mostrarVentasporISBN();
                    break;
                case 5:
                    ventas.calcularTotalGanado();
                    break;
                case 6:
                    System.out.println("Volviendo..\n");
                    break;
                default:
                    System.out.println("Numero no valido");
            }
        }
    }


    public void GestionClientes() {
        int eleccion = 0;

        while (eleccion != 6) {
            eleccion = ut.MenuClientes();
            switch (eleccion) {
                case 1:
                    cliente.altaCliente();
                    break;
                case 2:
                    cliente.mostrarClientes();
                    break;
                case 3:
                    cliente.buscarClienteDni();
                    break;
                case 4:
                    cliente.mejores5Clientes();
                    break;
                case 5:
                    cliente.actualizarCliente();
                    break;
                case 6:
                    System.out.println("Volviendo..\n");
                    break;
                default:
                    System.out.println("Numero no valido");
            }
        }
    }


    public void GestionLibros(){
        int eleccion= 0;

        while(eleccion != 7){
             eleccion = ut.MenuLibros();
            switch (eleccion){
                case 1:
                    libro.anadirNuevoLibro();
                    break;
                case 2:
                    libro.mostrarLibros();
                    break;
                case 3:
                    libro.buscarISBN();
                    break;
                case 4:
                    libro.buscarCategoria();
                    break;
                case 5:
                    libro.actualizarStock();
                    break;
                case 6:
                    libro.buscar5Libros();
                    break;
                case 7:
                    System.out.println("Volviendo..\n");
                    break;
                default:
                    System.out.println("Numero no valido");
            }
        }




    }


}
