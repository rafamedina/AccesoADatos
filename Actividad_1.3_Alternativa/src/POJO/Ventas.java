package POJO;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Clase Ventas
 * - Gestiona las operaciones relacionadas con las ventas de la librería.
 * - Almacena las ventas en "datos_libreria/Ventas.txt" con el formato:
 *   DNI;ISBN;Fecha;Unidades;PrecioTotal;
 *
 * Notas:
 * - Al igual que las otras clases POJO del proyecto, mezcla representación de datos
 *   con operaciones de E/S y entrada por consola. En una aplicación más grande
 *   convendría separar modelo, acceso a datos y capa de interacción.
 */
public class Ventas {
    // Campos que describen una venta
    private String DNI;            // DNI del cliente que realiza la compra
    private String ISBN;           // ISBN del libro vendido
    private String Fecha;          // Fecha de la venta (cadena)
    private int Unidades;          // Cantidad de unidades vendidas
    private double Precio_Total;   // Precio total de la venta
    String archivo = "datos_libreria/Ventas.txt"; // Ruta del archivo de ventas
    static Scanner sc = new Scanner(System.in);     // Scanner compartido para entrada por consola

    // Constructores
    public Ventas() {
    }

    // Constructor con fecha explícita
    public Ventas(String DNI, String ISBN, String fecha, int unidades, double precio_Total) {
        this.DNI = DNI;
        this.ISBN = ISBN;
        Fecha = fecha;
        Unidades = unidades;
        Precio_Total = precio_Total;
    }
    // Constructor que dejará que el método que escribe la venta ponga la fecha actual
    public Ventas(String DNI, String ISBN, int unidades, double precio_Total) {
        this.DNI = DNI;
        this.ISBN = ISBN;
        Unidades = unidades;
        Precio_Total = precio_Total;
    }

    // Getters y setters
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

    /**
     * crearArchivosVentas
     * - Crea el directorio "datos_libreria" y el archivo de ventas si no existen.
     */
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

    /**
     * escribirVenta
     * - Escribe una venta en el archivo con la fecha actual.
     * - Antes de escribir actualiza el stock del libro y la cantidad del cliente
     *   (llama a `actualizarStock` y `actualizarCantidad`). Si alguna de esas
     *   operaciones falla, no registra la venta.
     */
    public void escribirVenta(Ventas venta){
        try(BufferedWriter bf = new BufferedWriter(new FileWriter(archivo,true))){
            Date fechaActual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String linea = venta.getDNI() + ";" +
                    venta.getISBN() + ";" +
                    formato.format(fechaActual) + ";" +
                    venta.getUnidades() + ";" +
                    venta.getPrecio_Total() + ";" ;
            // Actualizar stock y cantidad en ficheros relacionados antes de escribir
            if(actualizarStock(venta.getISBN(), venta.getUnidades()) && actualizarCantidad(venta.getDNI(), venta.getUnidades()) ){
                    bf.write(linea);
                    bf.newLine();
            } else {
                System.out.println("La venta no se pudo completar");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * actualizarCantidad
     * - Incrementa la 'cantidad' asociada a un cliente en "Clientes.txt".
     * - Reescribe el archivo mediante un temporal. Devuelve true si se actualizó.
     * - Si el cliente no existe devuelve false.
     */
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
                        // Reconstruir la línea con la cantidad actualizada
                        linea = partes[0] + ";" + partes[1] + ";" + partes[2] + ";" + partes[3] + ";" + nuevaCantidad;
                        actualizado = true;
                    }
                }
                bf.write(linea);
                bf.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        if (!actualizado) {
            System.out.println("El cliente no existe");
        }
        // Si se escribió el temporal, reemplazamos el original
        if (temp.exists()) {
            archivin.delete();
            temp.renameTo(archivin);
        }
        return actualizado;

    }

    /**
     * actualizarStock
     * - Resta 'unidades' al stock del libro con el ISBN dado en "Libros.txt".
     * - Reescribe el archivo mediante un temporal. Devuelve true si la operación se realizó
     *   (es decir, si había suficiente stock y se actualizó), false en caso contrario.
     */
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
            // Escribir la línea (modificada o no) en el temporal
            bf.newLine();
            bf.write(linea);

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

    /**
     * calcularPrecioTotal
     * - Busca el precio unitario del libro en "Libros.txt" y calcula el total
     *   multiplicando por el número de unidades solicitadas.
     * - Si no encuentra el libro devuelve 0.
     */
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

    /**
     * registrarUnaVenta
     * - Interactúa con el usuario para pedir ISBN, DNI y unidades; calcula el precio y
     *   trata de registrar la venta usando `escribirVenta`.
     */
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

    /**
     * mostrarFechas
     * - Muestra todas las ventas que ocurren en la fecha indicada por el usuario.
     * - Lee el archivo de ventas y compara el campo fecha.
     */
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

    /**
     * mostrarVentasporDNI
     * - Muestra todas las ventas realizadas por el DNI indicado por el usuario.
     */
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

    /**
     * mostrarVentasporISBN
     * - Muestra todas las ventas de un libro (ISBN) indicado por el usuario.
     */
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

    /**
     * calcularTotalGanado
     * - Suma el campo Precio Total de todas las ventas y muestra el total.
     */
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
