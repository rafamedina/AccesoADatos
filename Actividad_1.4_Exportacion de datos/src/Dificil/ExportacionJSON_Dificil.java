package Dificil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportacionJSON_Dificil {

        // Ruta del archivo JSON
        static String fecha;
        private static final String ARCHIVO = "json/hotel_";
        private static final String NODOPADRE = "hotel";
        private static final String CARPETA = "json"; // NOMBRE CARPETA
        private static final String EXTENSION = ".json";

        private static final String INDENTACION = "    ";
        private static final String INDENTACION2 = INDENTACION + INDENTACION;
        private static final String INDENTACION3 = INDENTACION2 + INDENTACION;
        private static final String INDENTACION4 = INDENTACION3 + INDENTACION;
        private static final String INDENTACION5 = INDENTACION4 + INDENTACION;

        // ===== Helpers de formato =====
        private static String escapeJson(String texto) {
            if (texto == null || texto.isEmpty()) {
                return "";
            }
            // IMPORTANTE: El orden importa - escapar \ primero
            return texto.replace("\\", "\\\\")   // Barra invertida primero
                    .replace("\"", "\\\"")       // Comillas dobles
                    .replace("\n", "\\n")        // Nueva línea
                    .replace("\r", "\\r")        // Retorno de carro
                    .replace("\t", "\\t");       // Tabulador
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
            DateTimeFormatter out = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.now().format(out);
        }

        private static String fmt2(double d) {
            return String.format(Locale.US, "%.2f", d);
        }

        // ===== Exportación JSON óptima =====
        public static void exportarJSON(List<Reserva> reservas) {
            try {
                String nombreArchivo = crearNombreArchivo();

                // VALIDACIONES
                if (reservas == null || reservas.isEmpty()) {
                    System.out.println("❌ ERROR: No hay reservas para exportar.");
                    return;
                }
                if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
                    System.out.println("❌ ERROR: El nombre del archivo no puede estar vacío.");
                    return;
                }
                if (!crearCarpeta()) {
                    System.out.println("❌ ERROR: La carpeta no se ha podido crear");
                    return;
                }

                // ===== DEDUP: clientes y habitaciones =====
                // Mapear por id/numero, conservando último dato visto
                Map<Integer, Cliente> clientesMap = new HashMap<>();
                Map<Integer, Habitacion> habitacionesMap = new HashMap<>();

                // ===== Estadísticas =====
                Map<String, Integer> countPorEstado = new HashMap<>();
                Map<String, Integer> countPorTipo = new HashMap<>();
                Map<String, Double>  ingresosPorTipo = new HashMap<>();

                int totalReservas = 0;
                int nochesReservadas = 0;
                double ingresosTotales = 0.0;

                // Para ocupación media estimada:
                // ocupacionMedia = (nochesReservadas / (numHabitaciones * diasEnRango)) * 100, si hay rango.
                Integer minDia = null; // ordinal day (yyyyMMdd) aproximado
                Integer maxDia = null;

                for (Reserva r : reservas) {
                    if (r == null) continue;

                    // Collect clientes/habitaciones
                    if (r.getCliente() != null) {
                        clientesMap.put(r.getCliente().getId(), r.getCliente());
                    }
                    if (r.getHabitacion() != null) {
                        habitacionesMap.put(r.getHabitacion().getNumero(), r.getHabitacion());
                    }

                    // Estadísticas básicas
                    String estado = r.getEstado() != null ? r.getEstado().trim() : "Desconocido";
                    countPorEstado.put(estado, countPorEstado.getOrDefault(estado, 0) + 1);

                    String tipo = (r.getHabitacion() != null && r.getHabitacion().getTipo() != null)
                            ? r.getHabitacion().getTipo().trim()
                            : "Desconocida";
                    countPorTipo.put(tipo, countPorTipo.getOrDefault(tipo, 0) + 1);

                    // Noches (recalcular si hay fechas; si no, usar r.getNoches)
                    int noches = 0;
                    if (r.getFechaEntrada() != null && r.getFechaSalida() != null) {
                        noches = Math.max(0, (int) java.time.temporal.ChronoUnit.DAYS.between(r.getFechaEntrada(), r.getFechaSalida()));
                        // rango para ocupación
                        int dIn  = Integer.parseInt(r.getFechaEntrada().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                        int dOut = Integer.parseInt(r.getFechaSalida().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                        minDia = (minDia == null) ? dIn : Math.min(minDia, dIn);
                        maxDia = (maxDia == null) ? dOut : Math.max(maxDia, dOut);
                    } else {
                        noches = Math.max(0, r.getNoches());
                    }
                    nochesReservadas += noches;

                    // Ingresos por reserva
                    double precioTotal = r.getPrecioTotal();
                    if (precioTotal <= 0 && r.getHabitacion() != null
                            && r.getHabitacion().getPrecioPorNoche() > 0 && noches > 0) {
                        precioTotal = r.getHabitacion().getPrecioPorNoche() * noches;
                    }
                    ingresosTotales += precioTotal;
                    ingresosPorTipo.put(tipo, ingresosPorTipo.getOrDefault(tipo, 0.0) + precioTotal);

                    totalReservas++;
                }

                // Porcentajes por tipo = (reservas del tipo / totalReservas) * 100
                Map<String, Double> porcentajePorTipo = new HashMap<>();
                for (String t : countPorTipo.keySet()) {
                    double pct = (totalReservas > 0) ? (100.0 * countPorTipo.get(t) / totalReservas) : 0.0;
                    porcentajePorTipo.put(t, pct);
                }

                // Ocupación media estimada
                double ocupacionMedia = 0.0;
                int numHabitaciones = habitacionesMap.size();
                if (numHabitaciones > 0 && minDia != null && maxDia != null) {
                    // días de rango (exclusivo en salida, como noches): si entrada=20 y salida=23 => 3 días
                    LocalDate minDate = LocalDate.parse(minDia.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                    LocalDate maxDate = LocalDate.parse(maxDia.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                    int diasRango = Math.max(0, (int) java.time.temporal.ChronoUnit.DAYS.between(minDate, maxDate));
                    int capacidad = numHabitaciones * Math.max(1, diasRango);
                    ocupacionMedia = capacidad > 0 ? (100.0 * nochesReservadas / capacidad) : 0.0;
                }

                // ===== Escritura del JSON =====
                File archivo = new File(nombreArchivo);
                boolean creado = archivo.createNewFile(); // debería crear el archivo con el timestamp
                if (!creado) {
                    System.out.println("El archivo no se ha podido crear");
                    return;
                }

                try (BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(archivo, true), StandardCharsets.UTF_8))) {

                    // Apertura
                    bw.write("{");
                    bw.newLine();

                    bw.write(INDENTACION + "\"" + NODOPADRE + "\": {");
                    bw.newLine();

                    // informacion
                    bw.write(INDENTACION2 + "\"informacion\": {");
                    bw.newLine();
                    bw.write(INDENTACION3 + "\"nombre\": \"" + escapeJson("Hotel Paradise") + "\",");
                    bw.newLine();
                    bw.write(INDENTACION3 + "\"fecha\": \"" + escapeJson(fechaDDMMYYYY()) + "\"");
                    bw.newLine();
                    bw.write(INDENTACION2 + "},");
                    bw.newLine();

                    // clientes (diccionario por id)
                    bw.write(INDENTACION2 + "\"clientes\": {");
                    bw.newLine();
                    List<Integer> clientesOrden = new ArrayList<>(clientesMap.keySet());
                    Collections.sort(clientesOrden);
                    for (int i = 0; i < clientesOrden.size(); i++) {
                        int id = clientesOrden.get(i);
                        Cliente c = clientesMap.get(id);
                        bw.write(INDENTACION3 + "\"" + id + "\": {");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"nombre\": \"" + escapeJson(c != null ? c.getNombre() : "") + "\",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"email\": \"" + escapeJson(c != null ? c.getEmail() : "") + "\",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"telefono\": \"" + escapeJson(c != null ? c.getTelefono() : "") + "\"");
                        bw.newLine();
                        bw.write(INDENTACION3 + "}" + (i < clientesOrden.size() - 1 ? "," : ""));
                        bw.newLine();
                    }
                    bw.write(INDENTACION2 + "},");
                    bw.newLine();

                    // habitaciones (diccionario por numero)
                    bw.write(INDENTACION2 + "\"habitaciones\": {");
                    bw.newLine();
                    List<Integer> habOrden = new ArrayList<>(habitationsKeySet(habitacionesMap));
                    Collections.sort(habOrden);
                    for (int i = 0; i < habOrden.size(); i++) {
                        int num = habOrden.get(i);
                        Habitacion h = habitacionesMap.get(num);
                        bw.write(INDENTACION3 + "\"" + num + "\": {");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"tipo\": \"" + escapeJson(h != null ? h.getTipo() : "") + "\",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"precioPorNoche\": " + fmt2(h != null ? h.getPrecioPorNoche() : 0.0) + ",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"disponible\": " + ((h != null && h.isDisponible()) ? "true" : "false"));
                        bw.newLine();
                        bw.write(INDENTACION3 + "}" + (i < habOrden.size() - 1 ? "," : ""));
                        bw.newLine();
                    }
                    bw.write(INDENTACION2 + "},");
                    bw.newLine();

                    // reservas (array referenciando clienteId y habitacionNumero)
                    bw.write(INDENTACION2 + "\"reservas\": [");
                    bw.newLine();
                    for (int i = 0; i < reservas.size(); i++) {
                        Reserva r = reservas.get(i);
                        if (r == null) continue;

                        // Datos reserva
                        int idReserva = r.getId();
                        int clienteId = (r.getCliente() != null) ? r.getCliente().getId() : 0;
                        int habNum    = (r.getHabitacion() != null) ? r.getHabitacion().getNumero() : 0;

                        LocalDate fe = r.getFechaEntrada();
                        LocalDate fs = r.getFechaSalida();
                        int noches = 0;
                        if (fe != null && fs != null) {
                            noches = Math.max(0, (int) java.time.temporal.ChronoUnit.DAYS.between(fe, fs));
                        } else {
                            noches = Math.max(0, r.getNoches());
                        }

                        double precioTotal = r.getPrecioTotal();
                        if (precioTotal <= 0 && r.getHabitacion() != null
                                && r.getHabitacion().getPrecioPorNoche() > 0 && noches > 0) {
                            precioTotal = r.getHabitacion().getPrecioPorNoche() * noches;
                        }

                        String estado = r.getEstado() != null ? r.getEstado() : "";
                        DateTimeFormatter F = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        bw.write(INDENTACION3 + "{");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"id\": " + idReserva + ",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"clienteId\": " + clienteId + ",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"habitacionNumero\": " + habNum + ",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"fechaEntrada\": \"" + (fe != null ? fe.format(F) : "") + "\",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"fechaSalida\": \"" + (fs != null ? fs.format(F) : "") + "\",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"noches\": " + noches + ",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"precioTotal\": " + fmt2(precioTotal) + ",");
                        bw.newLine();
                        bw.write(INDENTACION4 + "\"estado\": \"" + escapeJson(estado) + "\"");
                        bw.newLine();
                        bw.write(INDENTACION3 + "}" + (i < reservas.size() - 1 ? "," : ""));
                        bw.newLine();
                    }
                    bw.write(INDENTACION2 + "],");
                    bw.newLine();

                    // estadisticas
                    bw.write(INDENTACION2 + "\"estadisticas\": {");
                    bw.newLine();

                    // porTipoHabitacion
                    bw.write(INDENTACION3 + "\"porTipoHabitacion\": {");
                    bw.newLine();
                    List<String> tipos = new ArrayList<>(countPorTipo.keySet());
                    tipos.sort(String::compareToIgnoreCase);
                    for (int i = 0; i < tipos.size(); i++) {
                        String t = tipos.get(i);
                        bw.write(INDENTACION4 + "\"" + escapeJson(t) + "\": {");
                        bw.newLine();
                        bw.write(INDENTACION5 + "\"totalReservas\": " + countPorTipo.getOrDefault(t, 0) + ",");
                        bw.newLine();
                        bw.write(INDENTACION5 + "\"ingresos\": " + fmt2(ingresosPorTipo.getOrDefault(t, 0.0)) + ",");
                        bw.newLine();
                        bw.write(INDENTACION5 + "\"porcentaje\": " + String.format(Locale.US, "%.1f", porcentajePorTipo.getOrDefault(t, 0.0)));
                        bw.newLine();
                        bw.write(INDENTACION4 + "}" + (i < tipos.size() - 1 ? "," : ""));
                        bw.newLine();
                    }
                    bw.write(INDENTACION3 + "},");
                    bw.newLine();

                    // porEstado
                    bw.write(INDENTACION3 + "\"porEstado\": {");
                    bw.newLine();
                    String[] ordenEstados = new String[] {"Confirmada", "Cancelada", "Completada"};
                    // Primero los conocidos en orden
                    for (int i = 0; i < ordenEstados.length; i++) {
                        String est = ordenEstados[i];
                        bw.write(INDENTACION4 + "\"" + est + "\": " + countPorEstado.getOrDefault(est, 0)
                                + (i < ordenEstados.length - 1 || tieneEstadosExtra(countPorEstado, ordenEstados) ? "," : ""));
                        bw.newLine();
                    }
                    // Luego los extra (si los hay)
                    List<String> extras = new ArrayList<>();
                    for (String k : countPorEstado.keySet()) {
                        if (!Arrays.asList(ordenEstados).contains(k)) extras.add(k);
                    }
                    extras.sort(String::compareToIgnoreCase);
                    for (int i = 0; i < extras.size(); i++) {
                        String k = extras.get(i);
                        bw.write(INDENTACION4 + "\"" + escapeJson(k) + "\": " + countPorEstado.get(k)
                                + (i < extras.size() - 1 ? "," : ""));
                        bw.newLine();
                    }
                    bw.write(INDENTACION3 + "},");
                    bw.newLine();

                    // resumen
                    bw.write(INDENTACION3 + "\"resumen\": {");
                    bw.newLine();
                    bw.write(INDENTACION4 + "\"totalReservas\": " + totalReservas + ",");
                    bw.newLine();
                    bw.write(INDENTACION4 + "\"ingresosTotal\": " + fmt2(ingresosTotales) + ",");
                    bw.newLine();
                    bw.write(INDENTACION4 + "\"nochesReservadas\": " + nochesReservadas + ",");
                    bw.newLine();
                    bw.write(INDENTACION4 + "\"ocupacionMedia\": " + String.format(Locale.US, "%.1f", ocupacionMedia));
                    bw.newLine();
                    bw.write(INDENTACION3 + "}");
                    bw.newLine();

                    // cierre estadisticas
                    bw.write(INDENTACION2 + "}");
                    bw.newLine();

                    // CIERRE hotel y raíz
                    bw.write(INDENTACION + "}");
                    bw.newLine();
                    bw.write("}");
                }

            } catch (IOException e) {
                // Mensaje de error claro en español
                System.err.println("Error escribiendo JSON (hotel): " + e.getMessage());
            }
        }

        // ===== Utilidades internas =====
        private static boolean tieneEstadosExtra(Map<String, Integer> m, String[] conocidos) {
            for (String k : m.keySet()) {
                boolean esConocido = false;
                for (String c : conocidos) {
                    if (c.equals(k)) { esConocido = true; break; }
                }
                if (!esConocido) return true;
            }
            return false;
        }

        // KeySet con null-safety (por si se cambia el map en el futuro)
        private static Set<Integer> habitationsKeySet(Map<Integer, Habitacion> map) {
            return map != null ? map.keySet() : Collections.emptySet();
        }
    }

