package Dificil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
public class ExportarCSV_Dificil {
    static final String archivo = "csv/reservas_";

    private static final String separador = ";";
    private static final String CARPETA = "csv"; // NOMBRE CARPETA

    // Formato de fechas para las columnas de entrada/salida
    private static final DateTimeFormatter F_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    public static void exportarCSV(List<Reserva> reservas) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String nombreArchivo = archivo + timestamp + ".csv";

        if (reservas == null || reservas.isEmpty()) {
            System.out.println("ERROR: No hay reservas para exportar.");
            return;
        }

        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
            return;
        }

        if (crearCarpeta()) {
            try (BufferedWriter bf = new BufferedWriter(new FileWriter(nombreArchivo))) {
                // Cabecera (aplanada con cliente + habitación)
                String header = String.join(separador,
                        "ID",
                        "ClienteNombre",
                        "ClienteEmail",
                        "HabitacionNum",
                        "TipoHabitacion",
                        "FechaEntrada",
                        "FechaSalida",
                        "Noches",
                        "PrecioTotal",
                        "Estado"
                );
                bf.write(header);
                bf.newLine();

                // Acumuladores para un pequeño resumen al final (opcional)
                int contador = 0;
                int nochesTotales = 0;
                double ingresosTotales = 0.0;

                // Filas
                for (Reserva r : reservas) {
                    contador += 1;

                    // Cliente
                    Cliente c = r.getCliente();
                    String cNombre = c != null ? c.getNombre() : "";
                    String cEmail  = c != null ? c.getEmail()  : "";

                    // Habitación
                    Habitacion h = r.getHabitacion();
                    String hNum  = h != null ? String.valueOf(h.getNumero()) : "";
                    String hTipo = h != null ? h.getTipo() : "";

                    // Fechas y noches calculadas
                    LocalDate fEntrada = r.getFechaEntrada();
                    LocalDate fSalida  = r.getFechaSalida();

                    int nochesCalc = 0;
                    if (fEntrada != null && fSalida != null) {
                        nochesCalc = Math.max(0, (int) ChronoUnit.DAYS.between(fEntrada, fSalida));
                    }
                    int noches = (nochesCalc > 0) ? nochesCalc : Math.max(0, r.getNoches());// Comprueba que exista fecha de entrada y salida antes de calcular
                    if (fEntrada != null && fSalida != null) {
                        // Calcula los días entre las dos fechas (long), lo convierte a int
                        // y asegura que no sea negativo (si la salida es anterior a la entrada)
                        nochesCalc = Math.max(0, (int) ChronoUnit.DAYS.between(fEntrada, fSalida));
                    }

                    // Precio total (si viene 0 y tenemos noches, lo mantenemos tal cual o podrías recalcular según tarifa)
                    double precioTotal = r.getPrecioTotal();

                    // Estado
                    String estado = r.getEstado() != null ? r.getEstado() : "";

                    // Construcción de la línea CSV
                    String linea = r.getId() + separador +

                            escaparCSV(cNombre) + separador +

                            escaparCSV(cEmail) + separador +

                            escaparCSV(hNum) + separador +

                            escaparCSV(hTipo) + separador +

                            (fEntrada != null ? fEntrada.format(F_FECHA) : "") + separador +

                            (fSalida != null ? fSalida.format(F_FECHA) : "") + separador +

                            noches + separador +

                            String.format(java.util.Locale.US, "%.2f", precioTotal) + separador +

                            escaparCSV(estado);

                    bf.write(linea);
                    bf.newLine();

                    // Acumulados
                    nochesTotales += noches;
                    ingresosTotales += precioTotal;
                }

                // Dos líneas en blanco y pequeño resumen (similar a tu "Nota media")
                bf.newLine();
                bf.newLine();
                bf.write("# Total reservas;" + contador);
                bf.newLine();
                bf.write("# Noches totales;" + nochesTotales);
                bf.newLine();
                bf.write("# Ingresos totales;" + String.format(java.util.Locale.US, "%.2f", ingresosTotales));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

