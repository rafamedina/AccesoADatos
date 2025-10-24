package Exportadores;

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

public class ExportadorCSV
{


    static final String archivo = "exportaciones/cuenta_";

    private static final String separador = ";";
    private static final String CARPETA = "exportaciones"; // NOMBRE CARPETA


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

    public static void exportarCSV(Cuenta cuen) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String nombreArchivo = archivo + timestamp + ".csv";

        if (cuen == null ) {
            System.out.println("ERROR: No hay productos para exportar.");
            return;
        }


        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
            return;
        }

        if(crearCarpeta()){
            try (BufferedWriter bf = new BufferedWriter(new FileWriter(nombreArchivo))) {
                String header = String.join(separador, "DNI", "Nombre", "Apellidos", "Numero Cuenta", "Saldo");
                String headerMov = String.join(separador, "Tipo", "Fecha", "Cantidad", "Concepto");

                bf.write(header);
                bf.newLine();



                String linea;
                String linea2;

                    Cliente cliente = cuen.getCliente();
                    ArrayList<Movimiento> movimiento = cuen.getLista();

                    linea = escaparCSV(cliente.getDni()) + separador +

                            escaparCSV(cliente.getNombre()) + separador +

                            escaparCSV(cliente.getApellido()) + separador +

                            escaparCSV(cliente.getNCuenta()) + separador +

                            cliente.getSaldo();



                    bf.write(linea);
                    bf.newLine();
                bf.newLine();

                bf.write(headerMov);
                bf.newLine();

                    for(Movimiento mov : movimiento) {

                        linea2 = escaparCSV(mov.isTipo() ? "INGRESO" : "RETIRADA") + separador +
                                escaparCSV(mov.getFecha()) + separador +
                                mov.getCantidad() + separador +
                                escaparCSV(mov.getConcepto());

                        bf.write(linea2);
                        bf.newLine();
                    }


                }catch (IOException e) {
                System.out.println(e.getMessage());
            }
            }
        }


    }
