package Fácil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportarJSON {
    // Ruta del archivo JSON
    static String fecha;
    private static final String ARCHIVO = "json/estudiantes_";
    private static final String NODOHIJO = "estudiantes";
    private static final String NODOPADRE = "clase";
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
                .replace("\"", "\\\"")    // Comillas dobles
                .replace("\n", "\\n")     // Nueva línea
                .replace("\r", "\\r")     // Retorno de carro
                .replace("\t", "\\t");    // Tabulador
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

    public static String crearNombreArchivo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        fecha = LocalDateTime.now().format(formatter);
        return ARCHIVO + fecha + EXTENSION;
    }

    /**
     * Escribe la lista completa de estudiantes en json/estudiantes_YYYYMMDD_HHMMSS.json.
     * Sobrescribe el archivo recién creado (se crea con timestamp) con una estructura JSON
     * bien formada que incluye metadata, la lista de estudiantes y un resumen de notas.
     * <p>
     * estudiantes: lista de objetos Fácil.Estudiante (debe existir la clase Fácil.Estudiante con getters usados)
     */
    public static void exportarJSON(List<Estudiante> estudiantes) {
        try {
            String nombreArchivo = crearNombreArchivo();

            // PASO 1: VALIDACIONES

            if (estudiantes == null || estudiantes.isEmpty()) {
                System.out.println("❌ ERROR: No hay productos para exportar.");
                return;
            }

            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                System.out.println("❌ ERROR: El nombre del archivo no puede estar vacío.");
                return;
            }


            if (crearCarpeta()) {
                double suma = estudiantes.stream().mapToDouble(Estudiante::getNota).sum();
                double media = estudiantes.isEmpty() ? 0.0 : suma / estudiantes.size();
                double maxima = estudiantes.stream().mapToDouble(Estudiante::getNota).max().orElse(0.0);
                double minima = estudiantes.stream().mapToDouble(Estudiante::getNota).min().orElse(0.0);

                File archivo = new File(nombreArchivo);
                boolean creado = archivo.createNewFile(); // debería crear el archivo con el timestamp
                if (creado) {
                    // Usamos UTF-8 y cerramos automáticamente con try-with-resources
                    try (BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {

                        //Apertura


                        bw.write("{");
                        bw.newLine();

                        bw.write(INDENTACION + "\"" + NODOPADRE + "\": {");
                        bw.newLine();


                        // Metadata
                        bw.write(INDENTACION2 + "\"metadata\": {");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"fecha\": \"" + escapeJson(fecha) + "\",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"totalEstudiantes\": " + estudiantes.size());
                        bw.newLine();
                        bw.write(INDENTACION2 + "},");
                        bw.newLine();


                        // SE COPIA DESDE AQUI



                        // Lista de estudiantes
                        bw.write(INDENTACION2 + "\"" + NODOHIJO + "\": [");
                        bw.newLine();

                        for (int i = 0; i < estudiantes.size(); i++) {
                            Estudiante e = estudiantes.get(i);


                            bw.write(INDENTACION3 + "{");
                            bw.newLine();
                            // id como string (fiel al tratamiento original de atributos en XML)
                            bw.write(INDENTACION4 + "\"id\": \"" + escapeJson(String.valueOf(e.getId())) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"nombre\": \"" + escapeJson(e.getNombre()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"apellidos\": \"" + escapeJson(e.getApellido()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"edad\": " + escapeJson(String.valueOf(e.getEdad())) + ",");
                            bw.newLine();

                            // nota como número formateado a 1 decimal (sin comillas)

                            bw.write(INDENTACION4 + "\"nota\": " + String.format("%.1f", e.getNota()).replace(",", "."));
                            bw.newLine();
                            bw.write(INDENTACION3 + "}" + (i < estudiantes.size() - 1 ? "," : "")); // se encarga de cerrar el objeto JSON correspondiente a un estudiante y, dependiendo de si es el último estudiante de la lista o no, agrega una coma al final.
                            bw.newLine();
                        }

                        bw.write(INDENTACION2 + "],");
                        bw.newLine();



                        // HASTA AQUI




                        // Resumen de notas
                        bw.write(INDENTACION2 + "\"resumen\": {");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"notaMedia\": " + String.format("%.2f", media).replace(",", ".") + ",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"notaMaxima\": " + String.format("%.1f", maxima).replace(",", ".") + ",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"notaMinima\": " + String.format("%.1f", minima).replace(",", "."));
                        bw.newLine();
                        bw.write(INDENTACION2 + "}");
                        bw.newLine();

                        bw.write(INDENTACION + "}");
                        bw.newLine();
                        bw.write("}");
                    }
                } else {
                    System.out.println("El archivo no se ha podido crear");
                }
            } else {
                System.out.println("La carpeta no se ha podido crear");
            }


        } catch (IOException e) {
            // Mensaje de error claro en español
            System.err.println("Error escribiendo JSON: " + e.getMessage());
        }
    }
}