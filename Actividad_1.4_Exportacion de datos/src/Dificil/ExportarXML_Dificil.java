package Dificil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ExportarXML_Dificil {

        // ====== Configuración ======
        static String fecha;
        private static final String ARCHIVO = "xml/hotel_";
        private static final String NODOPADRE = "hotel";

        private static final String INDENTACION  = "    ";
        private static final String INDENTACION2 = INDENTACION + INDENTACION;
        private static final String INDENTACION3 = INDENTACION2 + INDENTACION;
        private static final String INDENTACION4 = INDENTACION3 + INDENTACION;

        private static final String CARPETA   = "XML";  // NOMBRE CARPETA
        private static final String EXTENSION = ".xml";

        // ====== Helpers (mismos que usas) ======
        private static String escapeXml(String texto) {
            if (texto == null || texto.isEmpty()) {
                return "";
            }
            return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
        }

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

        private static String fechaDDMMYYYY() {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        private static String fmt2(double n) {
            return String.format(java.util.Locale.US, "%.2f", n);
        }

        private static int nochesEntre(LocalDate entrada, LocalDate salida, int fallback) {
            if (entrada != null && salida != null) {
                int n = (int) ChronoUnit.DAYS.between(entrada, salida);
                return Math.max(0, n);
            }
            return Math.max(0, fallback);
        }

        public static void escribirXmlHotel(List<Reserva> reservas) {
            try {
                String nombreArchivo = crearNombreArchivo();

                // Validaciones
                if (reservas == null || reservas.isEmpty()) {
                    System.out.println("ERROR: No hay reservas para exportar.");
                    return;
                }
                if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                    System.out.println("ERROR: El nombre del archivo no puede estar vacío.");
                    return;
                }
                if (!crearCarpeta()) {
                    System.out.println("ERROR: No se ha podido crear la carpeta.");
                    return;
                }

                // ===== Preparar estadísticas =====
                Map<String, Integer>  conteoPorEstado = new HashMap<>();                // Confirmada/Cancelada/Completada
                Map<String, Integer>  reservasPorTipo = new HashMap<>();                // Individual/Doble/Suite
                Map<String, Double>   ingresosPorTipo = new HashMap<>();

                int totalReservas = 0;
                int nochesReservadas = 0;
                double ingresosTotales = 0.0;

                // Precalcular contadores globales
                for (Reserva r : reservas) {
                    if (r == null) continue;

                    // Tipo de habitación
                    String tipo = (r.getHabitacion() != null && r.getHabitacion().getTipo() != null)
                            ? r.getHabitacion().getTipo().trim()
                            : "Desconocida";
                    reservasPorTipo.put(tipo, reservasPorTipo.getOrDefault(tipo, 0) + 1);

                    // Estado
                    String estado = (r.getEstado() != null) ? r.getEstado().trim() : "Desconocido";
                    conteoPorEstado.put(estado, conteoPorEstado.getOrDefault(estado, 0) + 1);

                    // Noches (recalcular por fechas si están; si no, usar r.getNoches())
                    int noches = nochesEntre(r.getFechaEntrada(), r.getFechaSalida(), r.getNoches());
                    nochesReservadas += noches;

                    // Ingresos por reserva (usar precioTotal; si 0, estimar por precio/noche * noches si se puede)
                    double precioTotal = r.getPrecioTotal();
                    double porNoche = (r.getHabitacion() != null) ? r.getHabitacion().getPrecioPorNoche() : 0.0;
                    if (precioTotal <= 0 && porNoche > 0 && noches > 0) {
                        precioTotal = porNoche * noches;
                    }
                    ingresosTotales += precioTotal;

                    ingresosPorTipo.put(tipo, ingresosPorTipo.getOrDefault(tipo, 0.0) + precioTotal);
                    totalReservas++;
                }

                // ===== Escribir XML =====
                File archivo = new File(nombreArchivo);
                boolean creado = archivo.createNewFile();
                if (!creado) {
                    System.out.println("El archivo no se ha podido crear");
                    return;
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
                    // Declaración + raíz
                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    bw.newLine();
                    bw.write("<" + NODOPADRE + ">");
                    bw.newLine();

                    // <informacion>
                    bw.write(INDENTACION + "<informacion>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<nombre>" + escapeXml("Hotel Paradise") + "</nombre>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "<fecha>" + escapeXml(fechaDDMMYYYY()) + "</fecha>");
                    bw.newLine();
                    bw.write(INDENTACION + "</informacion>");
                    bw.newLine();

                    // <reservas totalReservas="N">
                    bw.write(INDENTACION + "<reservas totalReservas=\"" + totalReservas + "\">");
                    bw.newLine();

                    // Iterar reservas y escribir estructura anidada
                    DateTimeFormatter F = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    for (Reserva r : reservas) {
                        if (r == null) continue;

                        // Valores base
                        Cliente c = r.getCliente();
                        Habitacion h = r.getHabitacion();

                        String estado = (r.getEstado() != null) ? r.getEstado() : "";
                        int idReserva = r.getId();

                        // Habitacion
                        int numHab = (h != null) ? h.getNumero() : 0;
                        String tipoHab = (h != null && h.getTipo() != null) ? h.getTipo() : "";
                        double precioNoche = (h != null) ? h.getPrecioPorNoche() : 0.0;
                        boolean disponible = (h != null) && h.isDisponible();

                        // Fechas y noches
                        LocalDate fEntrada = r.getFechaEntrada();
                        LocalDate fSalida  = r.getFechaSalida();
                        int noches = nochesEntre(fEntrada, fSalida, r.getNoches());

                        // Precio total (ver cálculo conservador)
                        double precioTotal = r.getPrecioTotal();
                        if (precioTotal <= 0 && precioNoche > 0 && noches > 0) {
                            precioTotal = precioNoche * noches;
                        }

                        // <reserva ...>
                        bw.write(INDENTACION2 + "<reserva id=\"" + idReserva + "\" estado=\"" + escapeXml(estado) + "\">");
                        bw.newLine();

                        // <cliente>
                        bw.write(INDENTACION3 + "<cliente>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<id>" + (c != null ? c.getId() : 0) + "</id>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<nombre>" + escapeXml(c != null ? c.getNombre() : "") + "</nombre>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<email>" + escapeXml(c != null ? c.getEmail() : "") + "</email>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<telefono>" + escapeXml(c != null ? c.getTelefono() : "") + "</telefono>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "</cliente>");
                        bw.newLine();

                        // <habitacion numero=".." tipo="..">
                        bw.write(INDENTACION3 + "<habitacion numero=\"" + numHab + "\" tipo=\"" + escapeXml(tipoHab) + "\">");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<precioPorNoche>" + fmt2(precioNoche) + "</precioPorNoche>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<disponible>" + (disponible ? "true" : "false") + "</disponible>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "</habitacion>");
                        bw.newLine();

                        // <fechas>
                        bw.write(INDENTACION3 + "<fechas>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<entrada>" + (fEntrada != null ? fEntrada.format(F) : "") + "</entrada>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<salida>" + (fSalida  != null ? fSalida.format(F)  : "") + "</salida>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<noches>" + noches + "</noches>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "</fechas>");
                        bw.newLine();

                        // <precio>
                        bw.write(INDENTACION3 + "<precio>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<total>" + fmt2(precioTotal) + "</total>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<porNoche>" + fmt2(precioNoche) + "</porNoche>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "</precio>");
                        bw.newLine();

                        // </reserva>
                        bw.write(INDENTACION2 + "</reserva>");
                        bw.newLine();
                    }

                    bw.write(INDENTACION + "</reservas>");
                    bw.newLine();

                    // <estadisticas>
                    bw.write(INDENTACION + "<estadisticas>");
                    bw.newLine();

                    // porTipoHabitacion
                    bw.write(INDENTACION2 + "<porTipoHabitacion>");
                    bw.newLine();
                    // Ordenar por nombre de tipo para salida estable
                    List<String> tipos = new ArrayList<>(reservasPorTipo.keySet());
                    tipos.sort(String::compareToIgnoreCase);
                    for (String tipo : tipos) {
                        bw.write(INDENTACION3 + "<" + escapeXml(tipo) + ">");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<totalReservas>" + reservasPorTipo.getOrDefault(tipo, 0) + "</totalReservas>");
                        bw.newLine();
                        bw.write(INDENTACION4 + "<ingresos>" + fmt2(ingresosPorTipo.getOrDefault(tipo, 0.0)) + "</ingresos>");
                        bw.newLine();
                        bw.write(INDENTACION3 + "</" + escapeXml(tipo) + ">");
                        bw.newLine();
                    }
                    bw.write(INDENTACION2 + "</porTipoHabitacion>");
                    bw.newLine();

                    // porEstado
                    bw.write(INDENTACION2 + "<porEstado>");
                    bw.newLine();
                    // Mostrar en orden fijo típico
                    String[] estados = new String[] {"Confirmada", "Cancelada", "Completada"};
                    for (String est : estados) {
                        int count = conteoPorEstado.getOrDefault(est, 0);
                        bw.write(INDENTACION3 + "<" + est + ">" + count + "</" + est + ">");
                        bw.newLine();
                    }
                    // También mostramos cualquier estado no estándar si existiera
                    for (String est : conteoPorEstado.keySet()) {
                        if (!Arrays.asList(estados).contains(est)) {
                            bw.write(INDENTACION3 + "<" + escapeXml(est) + ">" + conteoPorEstado.get(est) + "</" + escapeXml(est) + ">");
                            bw.newLine();
                        }
                    }
                    bw.write(INDENTACION2 + "</porEstado>");
                    bw.newLine();

                    // resumen
                    bw.write(INDENTACION2 + "<resumen>");
                    bw.newLine();
                    bw.write(INDENTACION3 + "<totalReservas>" + totalReservas + "</totalReservas>");
                    bw.newLine();
                    bw.write(INDENTACION3 + "<ingresosTotal>" + fmt2(ingresosTotales) + "</ingresosTotal>");
                    bw.newLine();
                    bw.write(INDENTACION3 + "<nochesReservadas>" + nochesReservadas + "</nochesReservadas>");
                    bw.newLine();
                    bw.write(INDENTACION2 + "</resumen>");
                    bw.newLine();

                    // </estadisticas>
                    bw.write(INDENTACION + "</estadisticas>");
                    bw.newLine();

                    // Cierre raíz
                    bw.write("</" + NODOPADRE + ">");
                    bw.newLine();
                }

            } catch (IOException e) {
                System.err.println("Error escribiendo XML (hotel): " + e.getMessage());
            }
        }
    }

