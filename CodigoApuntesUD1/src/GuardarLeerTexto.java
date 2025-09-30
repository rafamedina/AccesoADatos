import java.io.*;

public class GuardarLeerTexto {
    public static void main(String[] args) throws IOException {
        String ruta = System.getProperty("user.dir") + File.separator + "ejemplo.txt";

        // Escritura
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        bw.write("Primera línea\n");
        bw.write("Segunda línea\n");
        bw.close();

        // Lectura
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        br.close();
    }
}