import java.io.*;
public class GestorFicheroTexto {
    public static void main(String[] args) {
        try {
// Escritura
            FileWriter fw = new FileWriter("datos/registro.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Registro 1");
            bw.newLine();
            bw.write("Registro 2");
            bw.newLine();
            bw.write("Registro 3");
            bw.newLine();
            bw.flush();
            bw.close();
            System.out.println("Archivo escrito con éxito.");
// Lectura
            FileReader fr = new FileReader("datos/registro.txt");
            BufferedReader br = new BufferedReader(fr);

            String linea;
            System.out.println("Contenido del archivo:");
            while ((linea = br.readLine()) != null) {
                System.out.println("> " + linea);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

//1. ¿Qué ocurre si se vuelve a ejecutar el programa sin cambiar el nombre del archivo?
//Que se sobrescribe lo que hay escrito cada vez que se ejecuta


//2. ¿Cómo podrías añadir texto sin borrar el contenido anterior?
// Creando un bucle while donde lea primero y cuando la linea = null entonces escribe


//3. ¿Qué diferencias observas si eliminas el BufferedWriter y usas solo FileWriter ?
// Que no tienes el salto de linea


//4. ¿Por qué es importante cerrar los buffers después de usarlos?
//Es importante cerrar los buffers para liberar la memoria del sistema, evitar posibles errores de seguridad como los desbordamientos de búfer, asegurar que los datos se procesen o envíen correctamente y mantener la estabilidad de la aplicación, ya que la memoria utilizada por los buffers se reutiliza para nuevas operaciones cuando son eliminados.