import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Estudiante> estudiantes = List.of(
                new Estudiante(1, "Juan", "García López", 20, 8.5),
                new Estudiante(2, "María", "Rodríguez", 19, 9.2),
                new Estudiante(3, "Pedro", "Martínez", 21, 7.8),
                new Estudiante(4, "Ana", "López", 20, 8.9),
                new Estudiante(5, "Carlos", "Sánchez", 22, 6.5));

        ExportarCSV csv = new ExportarCSV();
        csv.exportarCSV(estudiantes);

    ExportarXML xml = new ExportarXML();
    ExportarXML.escribirXmlExacto(estudiantes);



        ExportarJSON.escribirJsonExacto(estudiantes);
    }
    }