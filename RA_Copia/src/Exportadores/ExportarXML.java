package Exportadores;
import static Exportadores.UtilesExport.*;
import POJO.Cliente;
import POJO.Cuenta;
import POJO.Movimiento;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class ExportarXML {
    // Ruta del archivo XML
    private static final String ARCHIVO = "exportaciones/cuenta_";
    private static final String NODOPADRE = "cuenta";
    private static final String NODOHIJO = "Propietarios";
    private static final String NODOHIJO2 = "Movimientos";


    private static final String INDENTACION = "    ";
    private static final String INDENTACION2 = INDENTACION + INDENTACION;
    private static final String INDENTACION3 = INDENTACION2 + INDENTACION;


    private static final String CARPETA = "exportaciones"; // NOMBRE CARPETA
    private static final String EXTENSION = ".xml"; // NOMBRE CARPETA




    /**
     * Escribe la lista completa de estudiantes en `datos/estudiantes.xml`.
     * Sobrescribe el archivo con una estructura XML bien formada y un resumen de notas.
     * <p>
     * estudiantes lista de objetos Estudiante (debe existir la clase Estudiante con getters usados)
     */



    public static void escribirXmlExacto(Cuenta cuen) {
        try {


            String nombreArchivo = crearNombreArchivo(ARCHIVO,EXTENSION);

            if (cuen == null) {
                System.out.println("ERROR: No hay productos para exportar.");
                return;
            }

            if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
                return;
            }


            if (crearCarpeta(CARPETA)) {

//                double suma = estudiantes.stream().mapToDouble(Estudiante::getNota).sum();
//                double media = estudiantes.isEmpty() ? 0.0 : suma / estudiantes.size();
//                double maxima = estudiantes.stream().mapToDouble(Estudiante::getNota).max().orElse(0.0);
//                double minima = estudiantes.stream().mapToDouble(Estudiante::getNota).min().orElse(0.0);


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


                    Cliente cliente = cuen.getCliente();

                    ArrayList<Movimiento> movimientos = cuen.getLista();



                    // Metadata
                    bw.write(INDENTACION + "<metadata>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<fecha>" + escapeXml(fecha) + "</fecha>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<totalMovimientos>" + movimientos.size() + "</totalMovimientos>");
                    bw.newLine();
                    bw.write(INDENTACION + "</metadata>");
                    bw.newLine();




                    bw.write(INDENTACION + "<" + NODOHIJO + ">");
                    bw.newLine();

                        bw.write(INDENTACION2 + "<Propietario>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<dni>" + escapeXml(cliente.getDni()) + "</dni>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<nombre>" + escapeXml(cliente.getNombre()) + "</nombre>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<apellidos>" + escapeXml(cliente.getApellido()) + "</apellidos>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<Numero_Cuenta>" + escapeXml(cliente.getNCuenta()) + "</Numero_Cuenta>");
                        bw.newLine();                                  // Decimales a mostrar
                        bw.write(INDENTACION3 + "<Saldo>" + String.format("%.2f", cliente.getSaldo()) + "</Saldo>");
                        bw.newLine();
                        bw.write(INDENTACION2 + "</Propietario>");
                        bw.newLine();

                    bw.write(INDENTACION + "</" + NODOHIJO + ">");
                    bw.newLine();










                    // SE COPIA ESTO HASTA SIGUIENTE COMENTARIO


                    bw.write(INDENTACION + "<" + NODOHIJO2 + ">");
                    bw.newLine();
                    for (Movimiento mov : movimientos) {
                        bw.write(INDENTACION2 + "<Movimiento>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<Tipo>" + escapeXml(mov.isTipo() ? "INGRESO" : "RETIRADA") + "</Tipo>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<Fecha>" + escapeXml(mov.getFecha()) + "</Fecha>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<Cantidad>" + mov.getCantidad() + "</Cantidad>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "<Concepto>" +escapeXml(mov.getConcepto()) + "</Concepto>");
                        bw.newLine();
                        bw.write(INDENTACION2 + "</Movimiento>");
                        bw.newLine();
                    }
                    bw.write(INDENTACION + "</" + NODOHIJO2 + ">");
                    bw.newLine();
                    // SE COPIA HASTA AQUI




                    // Resumen de notas
//                    bw.write(INDENTACION + "<resumen>");
//                    bw.newLine();
//                    bw.write(INDENTACION2 + "<notaMedia>" + String.format("%.2f", media) + "</notaMedia>");
//                    bw.newLine();
//                    bw.write(INDENTACION2 + "<notaMaxima>" + String.format("%.1f", maxima) + "</notaMaxima>");
//                    bw.newLine();
//                    bw.write(INDENTACION2 + "<notaMinima>" + String.format("%.1f", minima) + "</notaMinima>");
//                    bw.newLine();
//                    bw.write(INDENTACION + "</resumen>");
//                    bw.newLine();

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
