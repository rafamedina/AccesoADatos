package Medio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class ExportarJSON_Medio {
        // Ruta del archivo JSON
        static String fecha;
        private static final String ARCHIVO = "json/libros_";
        private static final String NODOPADRE = "biblioteca";
        private static final String NODO_INFO = "informacion";
        private static final String NODO_CATEGORIAS = "categorias";
        private static final String NODO_RESUMEN_GLOBAL = "resumenGlobal";

        private static final String INDENTACION = "    ";
        private static final String INDENTACION2 = INDENTACION + INDENTACION;
        private static final String INDENTACION3 = INDENTACION2 + INDENTACION;
        private static final String INDENTACION4 = INDENTACION3 + INDENTACION;
        private static final String CARPETA = "json"; // NOMBRE CARPETA
        private static final String EXTENSION = ".json";

        /**
         * Escapa caracteres especiales para JSON.
         * Reemplaza comillas, barra invertida y caracteres de control por sus
         * secuencias de escape JSON. Si la entrada es null devuelve cadena vacía.
         */
        private static String escapeJson(String texto) {
            if (texto == null || texto.isEmpty()) {
                return "";
            }
            // IMPORTANTE: El orden importa - escapar \ primero
            return texto.replace("\\", "\\\\")   // Barra invertida primero
                    .replace("\"", "\\\"")       // Comillas dobles
                    .replace("\n", "\\n")        // Nueva línea
                    .replace("\r", "\\r")        // Retorno de carro
                    .replace("\t", "\\t");       // Tabulador
        }

        /**
         * Asegura que exista el directorio donde se guardará el archivo.
         * Devuelve false si no puede crear el directorio.
         */
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

        public static String crearNombreArchivo() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            fecha = LocalDateTime.now().format(formatter);
            return ARCHIVO + fecha + EXTENSION;
        }

        private static String fechaDDMMYYYY() {
            DateTimeFormatter out = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDateTime.now().format(out);
        }

        public static void exportarJSON(List<Libro> libros) {
            try {
                String nombreArchivo = crearNombreArchivo();

                // PASO 1: VALIDACIONES
                if (libros == null || libros.isEmpty()) {
                    System.out.println("❌ ERROR: No hay libros para exportar.");
                    return;
                }
                if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                    System.out.println("❌ ERROR: El nombre del archivo no puede estar vacío.");
                    return;
                }
                if (!crearCarpeta()) {
                    System.out.println("❌ ERROR: La carpeta no se ha podido crear");
                    return;
                }

                // PASO 2: AGRUPAR, ORDENAR Y CALCULAR ESTADÍSTICAS
                Map<String, List<Libro>> porCategoria = new HashMap<>();
                for (Libro lb : libros) {
                    String categoria = (lb.getCategoria() == null || lb.getCategoria().isBlank())
                            ? "Sin categoría"
                            : lb.getCategoria().trim();
                    porCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(lb);
                }

                // Ordenar categorías alfabéticamente (sin Collator) y títulos dentro de cada categoría
                List<String> categoriasOrdenadas = new ArrayList<>(porCategoria.keySet());
                categoriasOrdenadas.sort(String::compareToIgnoreCase);
                for (String cat : categoriasOrdenadas) {
                    porCategoria.get(cat).sort((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()));
                }

                // Resumen global
                int totalLibrosGlobal = libros.size();
                int librosDisponibles = 0;
                int totalPrestamosHistorico = 0;
                for (Libro lb : libros) {
                    if (lb.isDisponible()) librosDisponibles++;
                    totalPrestamosHistorico += lb.getPrestamos();
                }
                int librosPrestados = totalLibrosGlobal - librosDisponibles;

                // PASO 3: ESCRITURA DEL JSON
                File archivo = new File(nombreArchivo);
                boolean creado = archivo.createNewFile(); // debería crear el archivo con el timestamp
                if (creado) {
                    // Usamos UTF-8 y cerramos automáticamente con try-with-resources
                    try (BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {

                        // Apertura
                        bw.write("{");
                        bw.newLine();

                        bw.write(INDENTACION + "\"" + NODOPADRE + "\": {");
                        bw.newLine();

                        // Metadata (informacion)
                        bw.write(INDENTACION2 + "\"" + NODO_INFO + "\": {");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"nombre\": \"Biblioteca Municipal\",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"fecha\": \"" + escapeJson(fechaDDMMYYYY()) + "\",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"totalLibros\": " + totalLibrosGlobal);
                        bw.newLine();
                        bw.write(INDENTACION2 + "},");
                        bw.newLine();

                        // SE COPIA DESDE AQUÍ: CATEGORÍAS ANIDADAS

                        bw.write(INDENTACION2 + "\"" + NODO_CATEGORIAS + "\": {");
                        bw.newLine();

                        for (int i = 0; i < categoriasOrdenadas.size(); i++) {
                            String categoria = categoriasOrdenadas.get(i);
                            List<Libro> lista = porCategoria.get(categoria);

                            // Estadísticas por categoría
                            int totalPrestamosCat = 0;
                            Libro masPrestado = null;
                            for (Libro lb : lista) {
                                totalPrestamosCat += lb.getPrestamos();
                                if (masPrestado == null ||
                                        lb.getPrestamos() > masPrestado.getPrestamos() ||
                                        (lb.getPrestamos() == masPrestado.getPrestamos()
                                                && lb.getTitulo().compareToIgnoreCase(masPrestado.getTitulo()) < 0)) {
                                    masPrestado = lb;
                                }
                            }
                            double prestamosMedio = lista.isEmpty() ? 0.0 :
                                    (double) totalPrestamosCat / (double) lista.size();

                            // Nodo de categoría
                            bw.write(INDENTACION3 + "\"" + escapeJson(categoria) + "\": {");
                            bw.newLine();

                            // totalLibros
                            bw.write(INDENTACION4 + "\"totalLibros\": " + lista.size() + ",");
                            bw.newLine();

                            // libros []
                            bw.write(INDENTACION4 + "\"libros\": [");
                            bw.newLine();
                            for (int j = 0; j < lista.size(); j++) {
                                Libro lb = lista.get(j);

                                bw.write(INDENTACION4 + INDENTACION + "{");
                                bw.newLine();

                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"isbn\": \"" + escapeJson(lb.getIsbn()) + "\",");
                                bw.newLine();
                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"titulo\": \"" + escapeJson(lb.getTitulo()) + "\",");
                                bw.newLine();
                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"autor\": \"" + escapeJson(lb.getAutor()) + "\",");
                                bw.newLine();
                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"año\": " + lb.getAñoPublicacion() + ",");
                                bw.newLine();
                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"paginas\": " + lb.getNumPaginas() + ",");
                                bw.newLine();
                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"disponible\": " + (lb.isDisponible() ? "true" : "false") + ",");
                                bw.newLine();
                                bw.write(INDENTACION4 + INDENTACION + INDENTACION + "\"prestamos\": " + lb.getPrestamos());
                                bw.newLine();

                                bw.write(INDENTACION4 + INDENTACION + "}" + (j < lista.size() - 1 ? "," : ""));
                                bw.newLine();
                            }
                            bw.write(INDENTACION4 + "],");
                            bw.newLine();

                            // estadisticas {}
                            bw.write(INDENTACION4 + "\"estadisticas\": {");
                            bw.newLine();
                            bw.write(INDENTACION4 + INDENTACION + "\"totalPrestamos\": " + totalPrestamosCat + ",");
                            bw.newLine();
                            // número con punto decimal (no coma)
                            bw.write(INDENTACION4 + INDENTACION + "\"prestamosMedio\": " + String.format(Locale.US, "%.2f", prestamosMedio) + ",");
                            bw.newLine();
                            bw.write(INDENTACION4 + INDENTACION + "\"libroMasPrestado\": \"" + escapeJson(masPrestado == null ? "" : masPrestado.getTitulo()) + "\"");
                            bw.newLine();
                            bw.write(INDENTACION4 + "}");
                            bw.newLine();

                            bw.write(INDENTACION3 + "}" + (i < categoriasOrdenadas.size() - 1 ? "," : ""));
                            bw.newLine();
                        }

                        bw.write(INDENTACION2 + "},");
                        bw.newLine();

                        // HASTA AQUÍ

                        // Resumen global
                        bw.write(INDENTACION2 + "\"" + NODO_RESUMEN_GLOBAL + "\": {");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"totalCategorias\": " + categoriasOrdenadas.size() + ",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"totalLibros\": " + totalLibrosGlobal + ",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"librosDisponibles\": " + librosDisponibles + ",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"librosPrestados\": " + librosPrestados + ",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"totalPrestamosHistorico\": " + totalPrestamosHistorico);
                        bw.newLine();
                        bw.write(INDENTACION2 + "}");
                        bw.newLine();

                        // Cierre padre y raíz
                        bw.write(INDENTACION + "}");
                        bw.newLine();
                        bw.write("}");
                    }
                } else {
                    System.out.println("El archivo no se ha podido crear");
                }

            } catch (IOException e) {
                // Mensaje de error claro en español
                System.err.println("Error escribiendo JSON: " + e.getMessage());
            }
        }
    }


