// Importa las clases necesarias para manejo de archivos y lectura de datos
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Práctica_Clase_Scanner {
    public static void main(String[] args) {
        // Crea un objeto File que representa el archivo productos.txt
        File ar = new File("productos.txt");
        // Verifica si el archivo existe; si no, muestra mensaje y termina
        if (!ar.exists()) {
            System.out.println("El archivo productos.txt no existe.");
            return;
        }
        // Muestra el tamaño del archivo en bytes
        System.out.println("El archivo productos.txt ocupa " + ar.length() + " bytes.");

        // Inicializa variables para estadísticas
        int totalArticulos = 0;
        double sumaPrecios = 0.0;
        double importeTotal = 0.0;

        try {
            // Crea un Scanner para leer el archivo línea por línea
            Scanner fileScanner = new Scanner(ar);
            while (fileScanner.hasNextLine()) {
                // Lee una línea del archivo
                String linea = fileScanner.nextLine();
                // Divide la línea en partes usando el punto y coma como separador
                String[] partes = linea.split(";");
                // Si la línea tiene exactamente 4 partes, procesa los datos
                if (partes.length == 4) {
                    String categoria = partes[0].trim(); // Obtiene la categoría
                    String nombre = partes[1].trim();    // Obtiene el nombre
                    double precio = Double.parseDouble(partes[2].trim()); // Convierte el precio a double
                    int stock = Integer.parseInt(partes[3].trim());       // Convierte el stock a int
                    // Muestra los datos del producto en formato legible
                    System.out.println(nombre + " (" + categoria + ") -- Precio: " + precio + " €  -- Stock: " + stock);
                    // Acumula estadísticas
                    totalArticulos++;
                    sumaPrecios += precio;
                    importeTotal += precio * stock;
                } else {
                    // Si la línea no tiene 4 partes, la muestra tal cual (posible error de formato)
                    System.out.println(linea);
                }
            }
            fileScanner.close();

            // Si se encontraron artículos válidos, muestra las estadísticas finales
            if (totalArticulos > 0) {
                System.out.println("Número total de artículos: " + totalArticulos);
                // Muestra el promedio de precios con 2 decimales
                System.out.println("Promedio de precios: " + String.format("%.2f", (sumaPrecios / totalArticulos)) + " €");
                // Muestra el importe total (precio*stock) con 2 decimales
                System.out.println("Importe total (precio*stock): " + String.format("%.2f", importeTotal) + " €");
            } else {
                // Si no hay artículos válidos, muestra mensaje
                System.out.println("No se encontraron artículos válidos.");
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no se encuentra, muestra mensaje de error
            System.out.println("No se encontró el archivo productos.txt");
        }
    }
}
