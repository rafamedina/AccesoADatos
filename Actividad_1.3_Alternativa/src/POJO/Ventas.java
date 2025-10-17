package POJO;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Ventas {
    private String DNI;
    private String ISBN;
    private String Fecha;
    private int Unidades;
    private double Precio_Total;
    String archivo = "datos_libreria/Ventas.txt";
    static Scanner sc = new Scanner(System.in);

    public Ventas() {
    }

    public Ventas(String DNI, String ISBN, String fecha, int unidades, double precio_Total) {
        this.DNI = DNI;
        this.ISBN = ISBN;
        Fecha = fecha;
        Unidades = unidades;
        Precio_Total = precio_Total;
    }
    public Ventas(String DNI, String ISBN, int unidades, double precio_Total) {
        this.DNI = DNI;
        this.ISBN = ISBN;
        Unidades = unidades;
        Precio_Total = precio_Total;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public double getPrecio_Total() {
        return Precio_Total;
    }

    public void setPrecio_Total(double precio_Total) {
        Precio_Total = precio_Total;
    }

    public int getUnidades() {
        return Unidades;
    }

    public void setUnidades(int unidades) {
        Unidades = unidades;
    }

    public void crearArchivosVentas(){
        File carpeta = new File("datos_libreria");
        if(!carpeta.exists()){
            carpeta.mkdir();
        }
        try{
            File ventas = new File(archivo);
            if(!ventas.exists()){
                ventas.createNewFile();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void escribirVenta(Ventas venta){
        try(BufferedWriter bf = new BufferedWriter(new FileWriter(archivo,true))){
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String linea = venta.getDNI() + ";" +
                    venta.getISBN() + ";" +
                    formato.format(fechaActual) + ";" +
                    venta.getUnidades() + ";" +
                    venta.getPrecio_Total() + ";" ;
            if(actualizarStock(venta.getISBN(), venta.getUnidades()) && actualizarCantidad(venta.getDNI(), venta.getUnidades()) ){
                    bf.write(linea);
            } else {
                System.out.println("La venta no se pudo completar");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean actualizarCantidad(String dni, int unidades) {
        File temp = new File("datos_libreria/Clientestemp.txt");
        File archivin = new File("datos_libreria/Clientes.txt");
        boolean actualizado = false;
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(temp));
             BufferedReader bfr = new BufferedReader(new FileReader(archivin))) {
            String linea;
            while ((linea = bfr.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    if (dni.equalsIgnoreCase(partes[0])) {
                        int cantidadVieja = Integer.parseInt(partes[4]);
                        int nuevaCantidad = cantidadVieja + unidades;
                        linea = partes[0] + ";" + partes[1] + ";" + partes[2] + ";" + partes[3] + ";" + nuevaCantidad;
                        actualizado = true;
                    }
                }
                bf.write(linea);
                bf.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (temp.exists()) {
                archivin.delete();
                temp.renameTo(archivin);
            }
        }
        if (!actualizado) {
            System.out.println("El cliente no existe");
        }
        return actualizado;

    }

    public boolean actualizarStock(String isbn, int unidades){
        File temp = new File("datos_libreria/Librostemp.txt");
        File archivin = new File("datos_libreria/Libros.txt");
        boolean actualizado = false;
    try(BufferedWriter bf = new BufferedWriter(new FileWriter(temp));
        BufferedReader bfr = new BufferedReader(new FileReader(archivin))) {
        String linea;

        while ((linea = bfr.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length == 6) {
                if (isbn.equalsIgnoreCase(partes[0])) {
                    int Stockviejo = Integer.parseInt(partes[5]);
                    if(unidades<=Stockviejo){
                        int NuevoStock = Stockviejo - unidades;
                        linea = (partes[0] + ";" + partes[1] + ";" + partes[2] + ";" + partes[3] + ";" + partes[4] + ";" + NuevoStock + ";");
                        actualizado = true;
                    }
                }
            }
            bf.write(linea);
            bf.newLine();
        }
    }
     catch (IOException e) {
        System.out.println(e.getMessage());

       } if(temp.exists()){
            archivin.delete();
            temp.renameTo(archivin);}
    if(!actualizado){
        System.out.println("No hay tantas unidades de libros disponibles");
    }
    return actualizado;
    }

    public double calcularPrecioTotal(String isbn, int unidades){
        double precio = 0;
        try(BufferedReader bf = new BufferedReader(new FileReader("datos_libreria/Libros.txt"))){

            String linea;
            while((linea = bf.readLine())!=null){
                String[] partes = linea.split(";");
                if(partes.length==6){
                    if(isbn.equalsIgnoreCase(partes[0])){
                         precio = unidades * Double.parseDouble(partes[4]);

                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return precio;

    }

    public void registrarUnaVenta(){
    try{
        System.out.println("Procedemos a registrar la venta: " );
        System.out.println("Dime un isbn: ");
        String isbn = sc.nextLine();
        System.out.println("Dime un dni: ");
        String dni = sc.nextLine();
        System.out.println("Dime cuantos ejemplares quieres comprar: ");
        int unidades = sc.nextInt();
        sc.nextLine();
        double precioTotal=calcularPrecioTotal(isbn,unidades);
        if(precioTotal!=0){
            escribirVenta(new Ventas(dni,isbn,unidades,precioTotal));
        } else {
            System.out.println("El libro no existe");
        }

    } catch (Exception e){
        System.out.println(e.getMessage());
    }
    }

    public void mostrarFechas(){
        boolean fechas = false;
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            String linea;
            System.out.println("Dime que fecha quieres buscar: (dd-MM-yyyy)");
            String date = sc.nextLine();
            while((linea = bf.readLine())!= null){
                String[] partes = linea.split(";");
                if(partes.length==5){
                    if(date.equalsIgnoreCase(partes[3])){
                        System.out.println(" DNI: " + partes[0] + " ISBN: " + partes[1] + " Fecha: " + partes[2] + " Unidades: " + partes[3] + " Precio Total: " + partes[4]);
                        fechas = true;
                    }
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    if(!fechas){
        System.out.println("No hay Ventas en esa fecha");
    }

    }

    public void mostrarVentasporDNI(){
        boolean d = false;
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            String linea;
            System.out.println("Dime que DNI quieres buscar: ");
            String dni = sc.nextLine();
            while((linea = bf.readLine())!= null){
                String[] partes = linea.split(";");
                if(partes.length==5){
                    if(dni.equalsIgnoreCase(partes[0])){
                        System.out.println(" DNI: " + partes[0] + " ISBN: " + partes[1] + " Fecha: " + partes[2] + " Unidades: " + partes[3] + " Precio Total: " + partes[4]);
                        d = true;
                    }
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        if(!d){
            System.out.println("No hay Ventas con ese DNI");
        }

    }

    public void mostrarVentasporISBN(){
        boolean d = false;
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            String linea;
            System.out.println("Dime que ISBN quieres buscar: ");
            String isbn = sc.nextLine();
            while((linea = bf.readLine())!= null){
                String[] partes = linea.split(";");
                if(partes.length==5){
                    if(isbn.equalsIgnoreCase(partes[1])){
                        System.out.println(" DNI: " + partes[0] + " ISBN: " + partes[1] + " Fecha: " + partes[2] + " Unidades: " + partes[3] + " Precio Total: " + partes[4]);
                        d = true;
                    }
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        if(!d){
            System.out.println("No hay Ventas con ese ISBN");
        }

    }

    public void calcularTotalGanado(){
        double total=0;
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            String linea;
            while((linea = bf.readLine())!= null){
                String[] partes = linea.split(";");
                if(partes.length==5){
                        total = total + Double.parseDouble(partes[5]);
                    }
            }
            System.out.println("El total ganado es de: " + total + "euros");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}

