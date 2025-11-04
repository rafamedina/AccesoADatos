package POJO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase Libros
 * - Representa operaciones sobre los libros almacenados en el archivo
 *   "datos_libreria/Libros.txt".
 * - Cada libro se almacena en una línea con formato: ISBN;Titulo;Autor;Categoria;Precio;Stock;
 *
 * Notas:
 * - La clase mezcla la representación del modelo (campos) con operaciones de
 *   E/S y lectura desde consola. Para aplicaciones más grandes conviene separar
 *   responsabilidades.
 */
public class Libros {
    // Campos que describen un libro
    private String ISBN;       // Identificador único del libro
    private String Titulo;     // Título del libro
    private String Autor;      // Autor
    private String Categoria;  // Categoría o género
    private double Precio;     // Precio por unidad
    private int Stock;         // Unidades disponibles en stock
    static Scanner sc = new Scanner(System.in); // Scanner compartido para entrada por consola
    String archivo = "datos_libreria/Libros.txt"; // Ruta del fichero de datos

    // Getters y setters estándar
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String libro) {
        Titulo = libro;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    // Constructores
    public Libros(String ISBN, String libro, String autor, String categoria, double precio, int stock) {
        this.ISBN = ISBN;
        Titulo = libro;
        Autor = autor;
        Categoria = categoria;
        Precio = precio;
        Stock = stock;
    }
    public Libros(){};

    /**
     * crearArchivosLibro
     * - Crea la carpeta "datos_libreria" y el archivo "Libros.txt" si no existen.
     */
    public void crearArchivosLibro(){
        File datos_libreria = new File("datos_libreria");
        if(!datos_libreria.exists()){
            datos_libreria.mkdir();
        }
        try{
            File arli = new File(archivo);
            if(!arli.exists()){
                arli.createNewFile();
            }
        } catch (IOException e){
            // Aquí sólo se llama a e.getMessage(); sería mejor imprimir la excepción
            e.getMessage();
        }
    }

    /**
     * guardarLibro
     * - Añade un nuevo libro al final del archivo si el ISBN no existe.
     * - Construye la línea separada por ';' y escribe una nueva línea.
     * - Nota: se añade un ';' final en la línea, que es consistente con el resto del
     *   proyecto pero puede dejar campos vacíos al final si se procesa estrictamente.
     */
    public void guardarLibro(Libros libro){
            try(BufferedWriter bflibro = new BufferedWriter(new FileWriter(archivo,true))){

                String Titulo = libro.getTitulo();
                String isbn = libro.getISBN();
                String autor = libro.getAutor();
                String categoria = libro.getCategoria();
                double precio = libro.getPrecio();
                int stock = libro.getStock();
                if(validarLibro(isbn)){
                    String  linea = (isbn + ";" + Titulo + ";" + autor + ";" + categoria + ";" + precio + ";" + stock + ";");
                    bflibro.newLine();
                    bflibro.write(linea);
                } else {
                    System.out.println("El libro ya existe");
                }

            } catch (IOException e){
                // De nuevo, sería mejor hacer e.printStackTrace() o un log.
                e.getMessage();
            }
    }

    /**
     * validarLibro
     * - Comprueba si un ISBN ya está en el archivo. Devuelve false si el ISBN existe,
     *   true si no existe.
     * - Asume que cada línea tiene 6 campos separados por ';'.
     */
    public boolean validarLibro(String isbn){
        try(BufferedReader bfLibro = new BufferedReader(new FileReader(archivo))){
            String linea;
            while((linea=bfLibro.readLine())!=null){
                String[] partes = linea.split(";");
                if(partes.length==6){
                    if(partes[0].equalsIgnoreCase(isbn)){
                        return false;
                    }
                }
            }
        } catch (IOException e){
            e.getMessage();
        }
        return true;
    }

    /**
     * anadirNuevoLibro
     * - Interactúa por consola para pedir datos del libro y lo guarda si el ISBN es nuevo.
     * - Valida la entrada y convierte precio/stock a tipos numéricos.
     */
    public void anadirNuevoLibro(){
        try{
            System.out.println("Vamos a añadir un libro: ");
            System.out.println("Dime un ISBN para comprobar si existe: ");
            String isbn = sc.nextLine();
            if(!validarLibro(isbn)){
                System.out.println("El libro ya existe en tus archivos");
            } else {
                System.out.println("Dame un titulo: ");
                String titulo = sc.nextLine();
                System.out.println("Dame el autor: ");
                String autor = sc.nextLine();
                System.out.println("Categoria: ");
                String categoria = sc.nextLine();
                System.out.println("Precio: ");
                double precio = sc.nextDouble();
                sc.nextLine();
                System.out.println("Stock: ");
                int stock = sc.nextInt();
                sc.nextLine();
                guardarLibro(new Libros(isbn,titulo,autor,categoria,precio,stock));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * mostrarLibros
     * - Muestra por consola todos los libros presentes en el archivo.
     * - Imprime una línea por cada entrada válida (6 campos).
     */
    public  void mostrarLibros(){
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            String linea;

            while((linea = bf.readLine())!=null){
                String[] partes = linea.split(";");
                if(partes.length==6){
                    System.out.println(partes[0] + " / " + partes[1] + " / " + partes[2] + " / " + partes[3] + " / " + partes[4] + " / " + partes[5]);
                } else {
                    System.out.println("Ya no hay mas libros");
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * buscarISBN
     * - Pide un ISBN por consola y comprueba si existe usando validarLibro().
     * - Nota: validarLibro devuelve false cuando el libro existe, por eso la
     *   lógica invierte el mensaje.
     */
    public void buscarISBN() {
        try {
            System.out.println("Dime que isbn quieres buscar: ");
            String isbn = sc.nextLine();
            if (!validarLibro(isbn)) {
                System.out.println("Ya tienes el libro");
            } else {
                System.out.println("No tienes el libro");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * buscarCategoria
     * - Muestra por consola todos los libros cuya categoria coincide con la
     *   solicitada (comparación case-insensitive).
     */
    public void buscarCategoria(){
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            System.out.println( "Que categoria quieres buscar :" );
            String categoria = sc.nextLine();
            String linea;
            while((linea = bf.readLine())!=null){
                String[] partes = linea.split(";");
                if(partes.length==6){
                    if(partes[3].equalsIgnoreCase(categoria)){
                        System.out.println(partes[0] + " / " + partes[1] + " / " + partes[2] + " / " + partes[3] + " / " + partes[4] + " / " + partes[5]);
                    }
                }
            }
            System.out.println("No hay mas libros que coincidan con esa categoria. ");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * actualizarStock
     * - Pide un ISBN y una cantidad a añadir. Crea un archivo temporal, actualiza
     *   la línea correspondiente y reemplaza el archivo original.
     * - Comprueba que el ISBN exista antes de intentar actualizar.
     * - Nota: el método escribe todas las líneas en el temporal y luego renombra.
     */
    public void actualizarStock() {
        // archivo original donde están guardados los libros
        // archivo temporal que usaremos para reescribir con el stock actualizado
        File archivin = new File(archivo);
        File temp = new File("datos_libreria/libros_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo));
             BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
            // pedimos el isbn del libro al usuario

            System.out.println("Dime el ISBN del libro que quieres actualizar el stock: ");
            String ISBN = sc.nextLine();
            // usamos tu método validarISBN()
            // si devuelve true → el ISBN no existe
            // si devuelve false → el libro sí existe
            System.out.println("Dime cuanto quieres añadir: ");
            int unidadesNuevas = sc.nextInt();
            sc.nextLine();
            if (validarLibro(ISBN)) {
                System.out.println("Este libro no está registrado en la librería.");
                return; // salimos del método
            }
            String linea;
            // leemos cada línea del archivo original
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 6) {
                    // si el isbn coincide con el introducido por el usuario
                    if (partes[0].equalsIgnoreCase(ISBN)) {
                        // convertimos el stock actual de String a entero
                        int stockActual = Integer.parseInt(partes[5]);
                        // sumamos las unidades nuevas
                        int nuevoStock = stockActual + unidadesNuevas;
                        // actualizamos el valor en el array
                        partes[5] = String.valueOf(nuevoStock);

                        System.out.println("Stock actualizado correctamente. Nuevo stock: " + nuevoStock);
                    }
                    // reconstruimos la línea (actualizada o no)
                    linea = String.join(";", partes);
                }
                // escribimos la línea en el archivo temporal
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar el stock: " + e.getMessage());
        }
        // reemplazamos el archivo original con el nuevo (ya modificado)
        if (temp.exists()) {
            archivin.delete();   // borramos el archivo original
            temp.renameTo(archivin); // renombramos el temporal con el nombre original
        }
    }
    /**
     * buscar5Libros
     * - Muestra libros con stock menor a 5 (posible aviso para reposición).
     */
    public void buscar5Libros(){
        try(BufferedReader bf = new BufferedReader(new FileReader(archivo))){
            String linea;
            while((linea = bf.readLine())!=null){
            String[] partes = linea.split(";");
            if(partes.length==6){
                int cantidad = Integer.parseInt(partes[5]);
            if(cantidad<5){
                System.out.println(partes[0] + " / " + partes[1] + " / " + partes[2] + " / " + partes[3] + " / " + partes[4] + " / " + partes[5]);
            }
            }
            }
            System.out.println("Ya no hay mas libros");
        } catch (IOException e){
            e.getMessage();
        }
    }
}
