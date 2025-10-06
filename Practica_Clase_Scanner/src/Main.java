import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File ar = new File("productos.txt");
        if (!ar.exists()) {
            System.out.println("El archivo productos.txt no existe.");
            return;
        }
        System.out.println("El archivo productos.txt ocupa " + ar.length() + " bytes.");
        try {
            Scanner fileScanner = new Scanner(ar);
            while (fileScanner.hasNextLine()) {
                String linea = fileScanner.nextLine();
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    String categoria = partes[0].trim();
                    String nombre = partes[1].trim();
                    String precio = partes[2].trim();
                    String stock = partes[3].trim();
                    System.out.println(nombre + " (" + categoria + ") -- Precio: " + precio + " €  -- Stock: " + stock);
                } else {
                    System.out.println(linea);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo productos.txt");
        }
    }
}
