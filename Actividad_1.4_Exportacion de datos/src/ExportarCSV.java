import java.io.*;
import java.util.List;

public class ExportarCSV {

final String archivo = "datos/estudiantes.csv";

private static final String separador = ";";

    public void Creacion(){
        try{
            File carpeta = new File("datos");
            if(!carpeta.exists()){
                carpeta.mkdir();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        try{
             File archi = new File(archivo);
             if(!archi.exists()){
                 archi.createNewFile();
             }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try
        (BufferedWriter bf = new BufferedWriter(new FileWriter(archivo))){
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while((linea = br.readLine())==null){
                bf.write("ID;Nombre;Apellidos;Edad;Nota");
                bf.newLine();
                break;
            }
            br.close();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public void exportarCSV(List<Estudiante> estudiantes) {
        Creacion();
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(archivo, true))) {
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
