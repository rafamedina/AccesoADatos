package Exportadores;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilesExport {
    public static String fecha;
    public static boolean crearCarpeta(String CARPETA) {
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

    public static String crearNombreArchivo(String ARCHIVO, String EXTENSION) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        fecha = LocalDateTime.now().format(formatter);
        return ARCHIVO + fecha + EXTENSION;
    }

    public static String escaparCSV(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }

        // Si contiene el separador, comillas o saltos de línea, debemos escapar
        if (texto.contains(";") || texto.contains("\"") || texto.contains("\n")) {
            // Duplicamos las comillas y encerramos todo entre comillas
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }

        return texto;
    }

    /**
     * Escapa caracteres especiales para JSON.
     * Reemplaza comillas, barra invertida y caracteres de control por sus
     * secuencias de escape JSON. Si la entrada es null devuelve cadena vacía.
     */

    public static String escapeJson(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }

        // IMPORTANTE: El orden importa - escapar \ primero
        return texto.replace("\\", "\\\\")   // Barra invertida primero
                .replace("\"", "\\\"")    // Comillas dobles
                .replace("\n", "\\n")     // Nueva línea
                .replace("\r", "\\r")     // Retorno de carro
                .replace("\t", "\\t");    // Tabulador
    }



    /**
     * Escribe la lista completa de estudiantes en json/estudiantes_YYYYMMDD_HHMMSS.json.
     * Sobrescribe el archivo recién creado (se crea con timestamp) con una estructura JSON
     * bien formada que incluye metadata, la lista de estudiantes y un resumen de notas.
     * <p>
     * estudiantes: lista de objetos Estudiante (debe existir la clase Estudiante con getters usados)
     */

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

    public static String escapeXml(String texto) {
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
}
