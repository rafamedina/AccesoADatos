import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ExportarCSV {

final String archivo = "csv/estudiantes_";

private static final String separador = ";";

    public void creacion(){
        try{
            File carpeta = new File("csv");
            if(!carpeta.exists()){
                carpeta.mkdir();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void exportarCSV(List<Estudiante> estudiantes) {
        creacion();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String nombreArchivo = archivo+timestamp+".csv";
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(nombreArchivo))){
            String header = String.join(separador, "ID", "Nombre", "Apellidos", "Edad", "Nota");
            bf.write(header);
            bf.newLine();


            double nota = 0;
            int contador = 0;
            double notaFinal = 0;
            String linea;
            for(Estudiante estu : estudiantes){
            contador += 1;
            linea = estu.getId()+separador+ estu.getNombre()+separador+ estu.getApellido()+separador+ estu.getEdad()+separador+ estu.getNota();
            bf.write(linea);
            bf.newLine();

            nota += estu.getNota();
            notaFinal = nota / contador;
            }

            bf.newLine();
            bf.newLine();
            bf.write("# Nota media;" + notaFinal);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
