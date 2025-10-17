package POJO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Libros {
    private String ISBN;
    private String Titulo;
    private String Autor;
    private String Categoria;
    private double Precio;
    private int Stock;
    static Scanner sc = new Scanner(System.in);
    String archivo = "datos_libreria/Libros.txt";

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

    public Libros(String ISBN, String libro, String autor, String categoria, double precio, int stock) {
        this.ISBN = ISBN;
        Titulo = libro;
        Autor = autor;
        Categoria = categoria;
        Precio = precio;
        Stock = stock;
    }
    public Libros(){};

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
            e.getMessage();
        }
    }
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
                    bflibro.write(linea);
                } else {
                    System.out.println("El libro ya existe");
                }

            } catch (IOException e){
                e.getMessage();
            }
    }

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

                        System.out.println("✅ Stock actualizado correctamente. Nuevo stock: " + nuevoStock);
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
