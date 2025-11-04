package Medio;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportarXML_Medio {

    // ====== CONFIG COMÚN ======
    static String fecha;

    // Rutas y nombres
    private static final String CARPETA = "XML";
    private static final String EXTENSION = ".xml";

    // Prefijos para los nombres de archivo
    private static final String ARCHIVO_LIBROS = "xml/libros_";

    // Etiquetas para libros
    private static final String NODOPADRE_LIB = "catalogo";
    private static final String NODO_CATEGORIAS = "categorias";
    private static final String NODO_CATEGORIA = "categoria";
    private static final String NODO_LIBROS = "libros";
    private static final String NODO_LIBRO = "libro";

    // Indentación
    private static final String INDENTACION = "    ";
    private static final String INDENTACION2 = INDENTACION + INDENTACION;
    private static final String INDENTACION3 = INDENTACION2 + INDENTACION;
    private static final String INDENTACION4 = INDENTACION3 + INDENTACION;

    // =================== utilidades ===================

    private static String escapeXml(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private static boolean crearCarpeta() {
        try {
            File dir = new File(CARPETA);
            if (!dir.exists() && !dir.mkdirs()) return false;
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static String crearNombreArchivo(String prefijo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        fecha = LocalDateTime.now().format(formatter);
        return prefijo + fecha + EXTENSION;
    }

    // =================== Exportador para LIBROS ===================

    /**
     * Exporta libros agrupados por categoría, ordenando categorías y títulos,
     * con subtotales por categoría y totales globales.
     */
    public static void exportarXML(List<Libro> libros) {
        try {
            String nombreArchivo = crearNombreArchivo(ARCHIVO_LIBROS);

            if (libros == null || libros.isEmpty()) {
                System.out.println("ERROR: No hay libros para exportar.");
                return;
            }
            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
                return;
            }
            if (!crearCarpeta()) {
                System.out.println("ERROR: No se pudo crear la carpeta destino.");
                return;
            }

            // === 1) Agrupar por categoría (sin nulos/blank) ===
            Map<String, List<Libro>> porCategoria = new HashMap<>();
            for (Libro lb : libros) {
                String categoria = (lb.getCategoria() == null || lb.getCategoria().isBlank())
                        ? "Sin categoría"
                        : lb.getCategoria().trim();
                porCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(lb);
            }

            // === 2) Ordenar categorías ===
            List<String> categoriasOrdenadas = new ArrayList<>(porCategoria.keySet());
            categoriasOrdenadas.sort(String::compareToIgnoreCase);

            // === 3) Totales globales (se calculan al vuelo) ===
            int totalLibrosGlobal = 0;
            int totalPrestamosGlobal = 0;

            File archivo = new File(nombreArchivo);
            boolean creado = archivo.createNewFile();
            if (!creado) {
                System.out.println("El archivo no se ha podido crear");
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
                // Declaración + raíz
                bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                bw.newLine();
                bw.write("<" + NODOPADRE_LIB + ">");
                bw.newLine();

                // Metadata
                bw.write(INDENTACION + "<metadata>");
                bw.newLine();
                bw.write(INDENTACION2 + "<fecha>" + escapeXml(fecha) + "</fecha>");
                bw.newLine();
                bw.write(INDENTACION + "</metadata>");
                bw.newLine();

                // Contenedor de categorías
                bw.write(INDENTACION + "<" + NODO_CATEGORIAS + ">");
                bw.newLine();

                for (String categoria : categoriasOrdenadas) {
                    List<Libro> lista = porCategoria.get(categoria);

                    // Ordenar libros en la categoría por título
                    lista.sort((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()));

                    int totalLibrosCat = 0;
                    int totalPrestamosCat = 0;

                    // <categoria nombre="..." totalLibros="x" totalPrestamos="y"> ... luego completamos
                    bw.write(INDENTACION2 + "<" + NODO_CATEGORIA + " nombre=\"" + escapeXml(categoria) + "\">");
                    bw.newLine();

                    // Nodo <libros>
                    bw.write(INDENTACION3 + "<" + NODO_LIBROS + ">");
                    bw.newLine();

                    for (Libro lb : lista) {
                        // Datos del libro
                        bw.write(INDENTACION4 + "<" + NODO_LIBRO + ">");
                        bw.newLine();

                        bw.write(INDENTACION4 + INDENTACION + "<isbn>" + escapeXml(lb.getIsbn()) + "</isbn>");
                        bw.newLine();
                        bw.write(INDENTACION4 + INDENTACION + "<titulo>" + escapeXml(lb.getTitulo()) + "</titulo>");
                        bw.newLine();
                        bw.write(INDENTACION4 + INDENTACION + "<autor>" + escapeXml(lb.getAutor()) + "</autor>");
                        bw.newLine();
                        bw.write(INDENTACION4 + INDENTACION + "<anioPublicacion>" + lb.getAñoPublicacion() + "</anioPublicacion>");
                        bw.newLine();
                        bw.write(INDENTACION4 + INDENTACION + "<paginas>" + lb.getNumPaginas() + "</paginas>");
                        bw.newLine();
                        bw.write(INDENTACION4 + INDENTACION + "<disponible>" + (lb.isDisponible() ? "true" : "false") + "</disponible>");
                        bw.newLine();
                        bw.write(INDENTACION4 + INDENTACION + "<prestamos>" + lb.getPrestamos() + "</prestamos>");
                        bw.newLine();

                        bw.write(INDENTACION4 + "</" + NODO_LIBRO + ">");
                        bw.newLine();

                        totalLibrosCat++;
                        totalPrestamosCat += lb.getPrestamos();
                    }

                    bw.write(INDENTACION3 + "</" + NODO_LIBROS + ">");
                    bw.newLine();

                    // Subtotal de la categoría
                    bw.write(INDENTACION3 + "<subtotal>");
                    bw.newLine();
                    bw.write(INDENTACION4 + "<totalLibros>" + totalLibrosCat + "</totalLibros>");
                    bw.newLine();
                    bw.write(INDENTACION4 + "<totalPrestamos>" + totalPrestamosCat + "</totalPrestamos>");
                    bw.newLine();
                    bw.write(INDENTACION3 + "</subtotal>");
                    bw.newLine();

                    bw.write(INDENTACION2 + "</" + NODO_CATEGORIA + ">");
                    bw.newLine();

                    totalLibrosGlobal += totalLibrosCat;
                    totalPrestamosGlobal += totalPrestamosCat;
                }

                bw.write(INDENTACION + "</" + NODO_CATEGORIAS + ">");
                bw.newLine();

                // Totales globales
                bw.write(INDENTACION + "<resumenGlobal>");
                bw.newLine();
                bw.write(INDENTACION2 + "<totalLibros>" + totalLibrosGlobal + "</totalLibros>");
                bw.newLine();
                bw.write(INDENTACION2 + "<totalPrestamos>" + totalPrestamosGlobal + "</totalPrestamos>");
                bw.newLine();
                bw.write(INDENTACION + "</resumenGlobal>");
                bw.newLine();

                // cierre raíz
                bw.write("</" + NODOPADRE_LIB + ">");
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo XML (libros): " + e.getMessage());
        }
    }
}
