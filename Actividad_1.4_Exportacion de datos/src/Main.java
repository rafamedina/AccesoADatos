import Dificil.*;
import Fácil.ExportarCSV;
import Medio.ExportarCSV_Medio;
import Fácil.ExportarJSON;
import Fácil.ExportarXML;
import Fácil.Estudiante;
import Medio.ExportarJSON_Medio;
import Medio.ExportarXML_Medio;
import Medio.Libro;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        exportarFacil();
        exportarDificil();
        exportarMedio();


    }



    public static void exportarFacil(){
        List<Estudiante> estudiantes = List.of(
                new Estudiante(1, "Juan", "García López", 20, 8.5),
                new Estudiante(2, "María", "Rodríguez", 19, 9.2),
                new Estudiante(3, "Pedro", "Martínez", 21, 7.8),
                new Estudiante(4, "Ana", "López", 20, 8.9),
                new Estudiante(5, "Carlos", "Sánchez", 22, 6.5));
        ExportarCSV.exportarCSV(estudiantes);
        ExportarXML.exportarXML(estudiantes);
        ExportarJSON.exportarJSON(estudiantes);
    }

    public static void exportarMedio() {
        // Crear una lista de libros
        List<Libro> libros = Arrays.asList(
                new Libro("978-84-123456-1", "Don Quijote de la Mancha", "Miguel de Cervantes", "Clásico", 1605, 863, true, 150),
                new Libro("978-84-123456-2", "Cien Años de Soledad", "Gabriel García Márquez", "Ficción", 1967, 471, false, 200),
                new Libro("978-84-123456-3", "1984", "George Orwell", "Distopía", 1949, 328, true, 120),
                new Libro("978-84-123456-4", "La Odisea", "Homero", "Épica", -800, 350, true, 90),
                new Libro("978-84-123456-5", "Harry Potter y la Piedra Filosofal", "J.K. Rowling", "Fantasía", 1997, 223, true, 500)
        );

        // Exportar a CSV
        ExportarCSV_Medio.exportarCSV(libros);
        // Exportar a XML
        ExportarXML_Medio.exportarXML(libros);
        // Exportar a JSON
        ExportarJSON_Medio.exportarJSON(libros);
    }


    public static void exportarDificil() {
        // Crear clientes
        List<Cliente> clientes = Arrays.asList(
                new Cliente(1, "Juan García", "juan@email.com", "666111222"),
                new Cliente(2, "María López", "maria@email.com", "611223344"),
                new Cliente(3, "Carlos Pérez", "carlos@email.com", "622334455"),
                new Cliente(4, "Ana Sánchez", "ana@email.com", "633445566")
        );

        // Crear habitaciones
        List<Habitacion> habitaciones = Arrays.asList(
                new Habitacion(101, "Doble", 90.00, false),
                new Habitacion(102, "Doble", 95.00, true),
                new Habitacion(203, "Suite", 200.00, true),
                new Habitacion(204, "Suite", 210.00, false),
                new Habitacion(305, "Individual", 50.00, true)
        );

        // Crear reservas
        List<Reserva> reservas = Arrays.asList(
                new Reserva(1, clientes.get(0), habitaciones.get(0), LocalDate.of(2025, 10, 20), LocalDate.of(2025, 10, 23), 3, 270.00, "Confirmada"),
                new Reserva(2, clientes.get(1), habitaciones.get(2), LocalDate.of(2025, 10, 21), LocalDate.of(2025, 10, 25), 4, 800.00, "Confirmada"),
                new Reserva(3, clientes.get(2), habitaciones.get(1), LocalDate.of(2025, 10, 22), LocalDate.of(2025, 10, 24), 2, 190.00, "Cancelada"),
                new Reserva(4, clientes.get(3), habitaciones.get(4), LocalDate.of(2025, 10, 23), LocalDate.of(2025, 10, 26), 3, 150.00, "Completada"),
                new Reserva(5, clientes.get(0), habitaciones.get(3), LocalDate.of(2025, 10, 25), LocalDate.of(2025, 10, 27), 2, 400.00, "Confirmada")
        );

        // Exportar a CSV
        ExportarCSV_Dificil.exportarCSV(reservas);
        // Exportar a XML
        ExportarXML_Dificil.escribirXmlHotel(reservas);
        // Exportar a JSON
        ExportacionJSON_Dificil.exportarJSON(reservas);
    }

}