package Gestiones;
import POJO.*;
public class ControllerMenu {
    Libros libro = new Libros();
    Clientes cliente = new Clientes();
    Utils ut = new Utils();


















    public void GestionClientes() {
        int eleccion = 0;
        cliente.crearArchivosClientes();
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
        libro.crearArchivosLibro();
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
