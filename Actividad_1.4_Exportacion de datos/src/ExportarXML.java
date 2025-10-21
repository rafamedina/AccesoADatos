import java.io.*;
import java.util.List;

public class ExportarXML {
    // Ruta del archivo XML
    private static final String ARCHIVO = "datos/estudiantes.xml";

    /**
     * Escapa caracteres especiales que tienen significado en XML para evitar
     * romper la estructura del documento o introducir vulnerabilidades.
     *
     * Convierte:
     *   &  -> &amp;
     *   <  -> &lt;
     *   >  -> &gt;
     *   "  -> &quot;
     *   '  -> &apos;
     *
     * Importante: el orden de reemplazo importa (primero '&') cuando se
     * hace con reemplazos en cadena; aquí procesamos carácter a carácter
     * para evitar dobles escapes.
     *
     * Si la entrada es null se devuelve cadena vacía.
     *
     * Ejemplo:
     *   entrada:  Tom & Jerry <3
     *   salida:   Tom &amp; Jerry &lt;3
     *
     * Uso: llamar antes de insertar valores de texto dentro de elementos o
     * atributos XML.
     *
     * @param s texto sin escapar
     * @return texto con los caracteres XML especiales escapados
     */
    private static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }


    /**
     * Asegura que exista el directorio donde se guardará el archivo.
     * Lanza IOException si no puede crear el directorio.
     */
    private static void crearArchivos() throws IOException {
        File dir = new File("datos");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("No se pudo crear el directorio: datos");
        }
    }

    /**
     * Escribe la lista completa de estudiantes en `datos/estudiantes.xml`.
     * Sobrescribe el archivo con una estructura XML bien formada y un resumen de notas.
     *
     * @param estudiantes lista de objetos Estudiante (debe existir la clase Estudiante con getters usados)
     * @param fecha       fecha en formato texto que se incluirá en el metadata
     */
    public static void escribirXmlExacto(List<Estudiante> estudiantes, String fecha) {
        try {
            crearArchivos();

            double suma = estudiantes.stream().mapToDouble(Estudiante::getNota).sum();
            double media = estudiantes.isEmpty() ? 0.0 : suma / estudiantes.size();
            double maxima = estudiantes.stream().mapToDouble(Estudiante::getNota).max().orElse(0.0);
            double minima = estudiantes.stream().mapToDouble(Estudiante::getNota).min().orElse(0.0);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
                // Declaración XML y elemento raíz
                bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                bw.newLine();
                bw.write("<clase>");
                bw.newLine();

                // Metadata
                bw.write("  <metadata>");
                bw.newLine();
                bw.write("    <fecha>" + escapeXml(fecha) + "</fecha>");
                bw.newLine();
                bw.write("    <totalEstudiantes>" + estudiantes.size() + "</totalEstudiantes>");
                bw.newLine();
                bw.write("  </metadata>");
                bw.newLine();

                // Lista de estudiantes
                bw.write("  <estudiantes>");
                bw.newLine();
                for (Estudiante e : estudiantes) {
                    bw.write("    <estudiante id=\"" + escapeXml(String.valueOf(e.getId())) + "\">");
                    bw.newLine();
                    bw.write("      <nombre>" + escapeXml(e.getNombre()) + "</nombre>");
                    bw.newLine();
                    bw.write("      <apellidos>" + escapeXml(e.getApellido()) + "</apellidos>");
                    bw.newLine();
                    bw.write("      <edad>" + e.getEdad() + "</edad>");
                    bw.newLine();
                    bw.write("      <nota>" + String.format("%.1f", e.getNota()) + "</nota>");
                    bw.newLine();
                    bw.write("    </estudiante>");
                    bw.newLine();
                }
                bw.write("  </estudiantes>");
                bw.newLine();

                // Resumen de notas
                bw.write("  <resumen>");
                bw.newLine();
                bw.write("    <notaMedia>" + String.format("%.2f", media) + "</notaMedia>");
                bw.newLine();
                bw.write("    <notaMaxima>" + String.format("%.1f", maxima) + "</notaMaxima>");
                bw.newLine();
                bw.write("    <notaMinima>" + String.format("%.1f", minima) + "</notaMinima>");
                bw.newLine();
                bw.write("  </resumen>");
                bw.newLine();

                bw.write("</clase>");
                bw.newLine();
            }
        } catch (IOException ex) {
            // Mensaje de error claro en español
            System.err.println("Error escribiendo XML: " + ex.getMessage());
        }
    }
}
