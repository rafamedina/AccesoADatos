package Exportadores;
import static Exportadores.UtilesExport.*;
import POJO.Cliente;
import POJO.Cuenta;
import POJO.Movimiento;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExportarJSON {
    // Ruta del archivo JSON
    private static final String ARCHIVO = "exportaciones/estudiantes_";
    private static final String NODOHIJO = "propietarios";
    private static final String NODOHIJO2 = "movimientos";
    private static final String NODOPADRE = "cuenta";
    private static final String INDENTACION = "    ";
    private static final String INDENTACION2 = INDENTACION + INDENTACION;
    private static final String INDENTACION3 = INDENTACION2 + INDENTACION;
    private static final String INDENTACION4 = INDENTACION3 + INDENTACION;
    private static final String CARPETA = "exportaciones"; // NOMBRE CARPETA
    private static final String EXTENSION = ".json";


    public static void escribirJsonExacto(Cuenta cuenta) {
        try {
            String nombreArchivo = crearNombreArchivo(ARCHIVO,EXTENSION);

            // PASO 1: VALIDACIONES

            if (cuenta == null ) {
                System.out.println("❌ ERROR: No hay productos para exportar.");
                return;
            }

            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                System.out.println("❌ ERROR: El nombre del archivo no puede estar vacío.");
                return;
            }


            if (crearCarpeta(CARPETA)) {


                File archivo = new File(nombreArchivo);
                boolean creado = archivo.createNewFile(); // debería crear el archivo con el timestamp
                if (creado) {
                    // Usamos UTF-8 y cerramos automáticamente con try-with-resources
                    try (BufferedWriter bw = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {

                        //Apertura
                        Cliente cliente = cuenta.getCliente();
                        ArrayList<Movimiento> movimientos = cuenta.getLista();


                        bw.write("{");
                        bw.newLine();

                        bw.write(INDENTACION + "\"" + NODOPADRE + "\": {");
                        bw.newLine();


                        // Metadata
                        bw.write(INDENTACION2 + "\"metadata\": {");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"fecha\": \"" + escapeJson(fecha) + "\",");
                        bw.newLine();
                        bw.write(INDENTACION3 + "\"totalMovimientos\": " + movimientos.size());
                        bw.newLine();
                        bw.write(INDENTACION2 + "},");
                        bw.newLine();


                        // SE COPIA DESDE AQUI



                        // Lista de estudiantes
                        bw.write(INDENTACION2 + "\"" + NODOHIJO + "\": [");
                        bw.newLine();



                            bw.write(INDENTACION3 + "{");
                            bw.newLine();
                            // id como string (fiel al tratamiento original de atributos en XML)
                            bw.write(INDENTACION4 + "\"dni\": \"" + escapeJson(cliente.getDni()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"nombre\": \"" +  escapeJson(cliente.getNombre()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"apellidos\": \"" +  escapeJson(cliente.getApellido()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"numeroCuenta\":\"" + escapeJson(cliente.getNCuenta()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"saldo\": " + cliente.getSaldo());
                            bw.newLine();


                            bw.write(INDENTACION3 + "}" ); // se encarga de cerrar el objeto JSON correspondiente a un estudiante y, dependiendo de si es el último estudiante de la lista o no, agrega una coma al final.
                            bw.newLine();


                        bw.write(INDENTACION2 + "],");
                        bw.newLine();



                        // HASTA AQUI






                        // Lista de estudiantes

                        bw.write(INDENTACION2 + "\"" + NODOHIJO2 + "\": [");
                        bw.newLine();

                        for (int i = 0; i < movimientos.size(); i++) {
                            Movimiento e = movimientos.get(i);


                            bw.write(INDENTACION3 + "{");
                            bw.newLine();
                            // id como string (fiel al tratamiento original de atributos en XML)
                            bw.write(INDENTACION4 + "\"tipo\": \"" + escapeJson(e.isTipo() ? "INGRESO" : "RETIRADA") + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"fecha\": \"" + escapeJson(e.getFecha()) + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"cantidad\": \"" + e.getCantidad() + "\",");
                            bw.newLine();
                            bw.write(INDENTACION4 + "\"concepto\":\"" + escapeJson(e.getConcepto()) + "\"");
                            bw.newLine();

                            // nota como número formateado a 1 decimal (sin comillas)
                            bw.write(INDENTACION3 + "}" + (i < movimientos.size() - 1 ? "," : "")); // se encarga de cerrar el objeto JSON correspondiente a un estudiante y, dependiendo de si es el último estudiante de la lista o no, agrega una coma al final.
                            bw.newLine();
                        }

                        bw.write(INDENTACION2 + "]");
                        bw.newLine();




                        // Resumen de notas
//                        bw.write(INDENTACION2 + "\"resumen\": {");
//                        bw.newLine();
//                        bw.write(INDENTACION3 + "\"notaMedia\": " + String.format("%.2f", media).replace(",", ".") + ",");
//                        bw.newLine();
//                        bw.write(INDENTACION3 + "\"notaMaxima\": " + String.format("%.1f", maxima).replace(",", ".") + ",");
//                        bw.newLine();
//                        bw.write(INDENTACION3 + "\"notaMinima\": " + String.format("%.1f", minima).replace(",", "."));
//                        bw.newLine();
//                        bw.write(INDENTACION2 + "}");
//                        bw.newLine();

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