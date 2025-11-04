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

public class ExportadorCSV
{


    static final String ARCHIVO = "exportaciones/cuenta_";

    private static final String separador = ";";
    private static final String CARPETA = "exportaciones"; // NOMBRE CARPETA
    private static final String EXTENSION = ".csv";




    public static void exportarCSV(Cuenta cuen) {


        String nombreArchivo = crearNombreArchivo(ARCHIVO,EXTENSION);

        if (cuen == null ) {
            System.out.println("ERROR: No hay productos para exportar.");
            return;
        }


        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
            return;
        }

        if(crearCarpeta(CARPETA)){
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
