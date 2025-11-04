package Medio;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportarCSV_Medio {

    static final String archivo = "csv/estudiantes_";

    private static final String separador = ";";
    private static final String CARPETA = "csv"; // NOMBRE CARPETA

    private static boolean crearCarpeta() {
        try {
            File dir = new File(CARPETA);
            if (!dir.exists() && !dir.mkdirs()) {
                return false;

            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static String escaparCSV(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }

        // Si contiene el separador, comillas o saltos de línea, debemos escapar
        if (texto.contains(separador) || texto.contains("\"") || texto.contains("\n")) {
            // Duplicamos las comillas y encerramos todo entre comillas
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }

        return texto;
    }

    public static void exportarCSV(List<Libro> libros) {

        DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String nombreArchivo = archivo + timestamp + ".csv";

        if (libros == null || libros.isEmpty()) {
            System.out.println("ERROR: No hay productos para exportar.");
            return;
        }


        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
            return;
        }

        if(crearCarpeta()) {
            Map<String, List<Libro>> porCategoria = new HashMap<>();
            for (Libro lb : libros) {
                String categoria = (lb.getCategoria() == null || lb.getCategoria().isBlank())
                        ? "Sin categoría"
                        : lb.getCategoria().trim();

                porCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(lb);
            }

            // Obtener lista de categorías y ordenarlas alfabéticamente
            List<String> categoriasOrdenadas = new ArrayList<>(porCategoria.keySet());

            categoriasOrdenadas.sort(String::compareToIgnoreCase);
            try (BufferedWriter bf = new BufferedWriter(new FileWriter(nombreArchivo))) {
                bf.write("# BIBLIOTECA MUNICIPAL - CATÁLOGO DE LIBROS");
                bf.newLine();

                String header = String.join(separador,
                        "ISBN", "Título", "Autor", "Año", "Páginas", "Disponible", "Préstamos", "Categoría");
                bf.write(header);
                bf.newLine();

                int totalLibros = 0;
                int totalPrestamos = 0;

                for (String categoria : categoriasOrdenadas) {
                    List<Libro> lista = porCategoria.get(categoria);

                    // Ordenar los libros dentro de cada categoría por título
                    lista.sort((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()));

                    bf.newLine();
                    bf.write("# Categoría: " + categoria);
                    bf.newLine();

                    int contadorCat = 0;
                    int prestamosCat = 0;

                    for (Libro lb : lista) {
                        String linea = escaparCSV(lb.getIsbn()) + separador
                                + escaparCSV(lb.getTitulo()) + separador
                                + escaparCSV(lb.getAutor()) + separador
                                + lb.getAñoPublicacion() + separador
                                + lb.getNumPaginas() + separador
                                + (lb.isDisponible() ? "Disponible" : "No disponible") + separador
                                + lb.getPrestamos() + separador
                                + escaparCSV(categoria);

                        bf.write(linea);
                        bf.newLine();

                        contadorCat++;
                        prestamosCat += lb.getPrestamos();
                    }

                    // Subtotal por categoría
                    bf.write("Subtotal" + separador + separador + separador + separador + separador
                            + separador + prestamosCat + separador + categoria + " (" + contadorCat + " libros)");
                    bf.newLine();

                    totalLibros += contadorCat;
                    totalPrestamos += prestamosCat;
                }

                // Totales generales
                bf.newLine();
                bf.write("# Totales");
                bf.newLine();
                bf.write("Total Libros" + separador + totalLibros);
                bf.newLine();
                bf.write("Total Préstamos" + separador + totalPrestamos);
                bf.newLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }}}


