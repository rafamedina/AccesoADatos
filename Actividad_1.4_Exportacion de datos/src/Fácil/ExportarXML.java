package Fácil;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportarXML {
    // Ruta del archivo XML
    static String fecha;
    private static final String ARCHIVO = "xml/estudiantes_";
    private static final String NODOPADRE = "clase";
    private static final String NODOHIJO = "estudiantes";


    private static final String INDENTACION = "    ";
    private static final String INDENTACION2 = INDENTACION + INDENTACION;
    private static final String INDENTACION3 = INDENTACION2 + INDENTACION;


    private static final String CARPETA = "XML"; // NOMBRE CARPETA
    private static final String EXTENSION = ".xml"; // NOMBRE CARPETA

    /**
     * Escapa caracteres especiales que tienen significado en XML para evitar
     * romper la estructura del documento o introducir vulnerabilidades.
     * <p>
     * Convierte:
     * &  -> &amp;
     * <  -> &lt;
     * >  -> &gt;
     * "  -> &quot;
     * '  -> &apos;
     * <p>
     * Importante: el orden de reemplazo importa (primero '&') cuando se
     * hace con reemplazos en cadena; aquí procesamos carácter a carácter
     * para evitar dobles escapes.
     * <p>
     * Si la entrada es null se devuelve cadena vacía.
     * <p>
     * Ejemplo:
     * entrada:  Tom & Jerry <3
     * salida:   Tom &amp; Jerry &lt;3
     * <p>
     * Uso: llamar antes de insertar valores de texto dentro de elementos o
     * atributos XML.
     *
     * @param texto texto sin escapar
     * @return texto con los caracteres XML especiales escapados
     */

    private static String escapeXml(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }

        // IMPORTANTE: El orden importa - escapar & primero
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    /**
     * Asegura que exista el directorio donde se guardará el archivo.
     * Lanza IOException si no puede crear el directorio.
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

    /**
     * Escribe la lista completa de estudiantes en `datos/estudiantes.xml`.
     * Sobrescribe el archivo con una estructura XML bien formada y un resumen de notas.
     * <p>
     * estudiantes lista de objetos Fácil.Estudiante (debe existir la clase Fácil.Estudiante con getters usados)
     */

    public static String crearNombreArchivo() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        fecha = LocalDateTime.now().format(formatter);
        return ARCHIVO + fecha + EXTENSION;
    }

    public static void exportarXML(List<Estudiante> estudiantes) {
        try {


            String nombreArchivo = crearNombreArchivo();

            if (estudiantes == null || estudiantes.isEmpty()) {
                System.out.println("ERROR: No hay productos para exportar.");
                return;
            }

            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
                return;
            }


            if (crearCarpeta()) {

                double suma = estudiantes.stream().mapToDouble(Estudiante::getNota).sum();
                double media = estudiantes.isEmpty() ? 0.0 : suma / estudiantes.size();
                double maxima = estudiantes.stream().mapToDouble(Estudiante::getNota).max().orElse(0.0);
                double minima = estudiantes.stream().mapToDouble(Estudiante::getNota).min().orElse(0.0);


                File archivo;
                archivo = new File(nombreArchivo);
                boolean creado = archivo.createNewFile();
                if (creado) {

                    BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));


                    // Declaración XML y elemento raíz
                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    bw.newLine();
                    bw.write("<" + NODOPADRE + ">");
                    bw.newLine();


                    // Metadata
                    bw.write(INDENTACION + "<metadata>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<fecha>" + escapeXml(fecha) + "</fecha>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<totalEstudiantes>" + estudiantes.size() + "</totalEstudiantes>");
                    bw.newLine();
                    bw.write(INDENTACION + "</metadata>");
                    bw.newLine();


                    // SE COPIA ESTO HASTA SIGUIENTE COMENTARIO


                    bw.write(INDENTACION + "<" + NODOHIJO + ">");
                    bw.newLine();
                    for (Estudiante e : estudiantes) {
                        bw.write(INDENTACION2 + "<estudiante id=\"" + escapeXml(String.valueOf(e.getId())) + "\">");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<nombre>" + escapeXml(e.getNombre()) + "</nombre>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<apellidos>" + escapeXml(e.getApellido()) + "</apellidos>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<edad>" + e.getEdad() + "</edad>");
                        bw.newLine();                                  // Decimales a mostrar
                        bw.write(INDENTACION3 + "<nota>" + String.format("%.1f", e.getNota()) + "</nota>");
                        bw.newLine();
                        bw.write(INDENTACION2 + "</estudiante>");
                        bw.newLine();
                    }
                    bw.write(INDENTACION + "</" + NODOHIJO + ">");
                    bw.newLine();
                    // SE COPIA HASTA AQUI




                    // Resumen de notas
                    bw.write(INDENTACION + "<resumen>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<notaMedia>" + String.format("%.2f", media) + "</notaMedia>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<notaMaxima>" + String.format("%.1f", maxima) + "</notaMaxima>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<notaMinima>" + String.format("%.1f", minima) + "</notaMinima>");
                    bw.newLine();
                    bw.write(INDENTACION + "</resumen>");
                    bw.newLine();

                    bw.write("</" + NODOPADRE + ">");
                    bw.newLine();
                    bw.close();
                } else {
                    System.out.println("El archivo no se ha podido crear");
                }

            }


        } catch (IOException e) {
            // Mensaje de error claro en español
            System.err.println("Error escribiendo XML: " + e.getMessage());
        }
    }
}
