import java.io.*;
public class AccesoAleatorioEjercicio {
    public static void main(String[] args) {
        try {
// Escribir registros
            RandomAccessFile raf = new RandomAccessFile("datos/registros.dat", "rw");
            raf.writeUTF("Registro 1");
            raf.writeUTF("Registro 2");
            raf.writeUTF("Registro 3");
            raf.seek(0); // Volver al inicio
// Leer primer registro
            System.out.println("Posición antes de leer 1: " +
                    raf.getFilePointer());
            String r1 = raf.readUTF();
            System.out.println("Registro 1: " + r1);
            System.out.println("Posición después de leer 1: " +
                    raf.getFilePointer());
// Leer segundo registro
            System.out.println("Posición antes de leer 2: " +
                    raf.getFilePointer());
            String r2 = raf.readUTF();

            System.out.println("Registro 2: " + r2);
            System.out.println("Posición después de leer 2: " +
                    raf.getFilePointer());
            raf.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


//1. ¿Qué indica el valor que devuelve getFilePointer() ?
//La posición del puntero en ese momento


//2. ¿Qué sucede si cambias el orden de lectura?
//Un error porque intentas leer donde no esta el cursor


//3. ¿Por qué RandomAccessFile no es recomendable para archivos de texto plano sin estructura?
//Porque opera a nivel de bytes y necesitas un formato consistente


    //4. ¿Cómo podrías modificar solo el tercer registro sin afectar los demás?
    public void Modificar3() throws IOException {

        RandomAccessFile raf = new RandomAccessFile("datos/registros.dat", "rw");
        raf.readUTF();
        raf.readUTF();
        long posTercerRegistro = raf.getFilePointer();
        raf.seek(posTercerRegistro);
        raf.writeUTF("Registro 3 modificado");
        raf.close();

    }
}