package Fácil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportarCSV {
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

    public static void exportarCSV(List<Estudiante> estudiantes) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String nombreArchivo = archivo + timestamp + ".csv";

        if (estudiantes == null || estudiantes.isEmpty()) {
            System.out.println("ERROR: No hay productos para exportar.");
            return;
        }


        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
            return;
        }

        if(crearCarpeta()){
            try (BufferedWriter bf = new BufferedWriter(new FileWriter(nombreArchivo))) {
                String header = String.join(separador, "ID", "Nombre", "Apellidos", "Edad", "Nota");
                bf.write(header);
                bf.newLine();


                double nota = 0;
                int contador = 0;
                double notaFinal = 0;
                String linea;
                for (Estudiante estu : estudiantes) {
                    contador += 1;
                    linea = estu.getId() + separador +

                            escaparCSV(estu.getNombre()) + separador +

                            escaparCSV(estu.getApellido()) + separador +

                            estu.getEdad() + separador +

                            estu.getNota();
                    bf.write(linea);
                    bf.newLine();

                    nota += estu.getNota();
                    notaFinal = nota / contador;
                }

                bf.newLine();
                bf.newLine();
                bf.write("# Nota media;" + String.format("%.2f", notaFinal));


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}
