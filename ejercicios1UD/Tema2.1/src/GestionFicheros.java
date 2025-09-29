import java.io.File;
import java.io.IOException;
public class GestionFicheros {
    public static void main(String[] args) {
        try {
// 1. Crear la ruta al archivo datos/fichero.txt
            File archivo = new File("datos/fichero.txt");
// 2. Si el archivo no existe, créalo
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs(); // Asegurar carpeta
                archivo.createNewFile();
                System.out.println("Archivo creado correctamente.");
            } else {
                System.out.println("El archivo ya existe.");
            }
// 3. Mostrar información del archivo
            System.out.println("Nombre: " + archivo.getName());
            System.out.println("Ruta absoluta: " +
                    archivo.getAbsolutePath());
            System.out.println("¿Se puede leer? " + archivo.canRead());

            System.out.println("¿Se puede escribir? " +
                    archivo.canWrite());
            System.out.println("¿Es un archivo? " + archivo.isFile());
// 4. Crear un nuevo directorio llamado datos/pruebas
            File carpeta = new File("datos/pruebas");
            if (!carpeta.exists()) {
                carpeta.mkdir();
                System.out.println("Carpeta creada.");
            } else {
                System.out.println("La carpeta ya existe.");
            }
// 5. Listar contenido de la carpeta datos
            File carpetaDatos = new File("datos");
            File[] lista = carpetaDatos.listFiles();
            System.out.println("Contenido de la carpeta 'datos':");
            if (lista != null) {
                for (File f : lista) {
                    System.out.println("- " + f.getName() +
                            (f.isDirectory() ? " (directorio)" : " (archivo)"));
                }
            }
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " +
                    e.getMessage());
        }
    }
}

// 1. ¿Qué ocurre si borras datos/fichero.txt y vuelves a ejecutar el programa?

// Que se vuelve a crear el archivo

// 2. ¿Y si cambias los permisos del archivo para que no se pueda escribir?

// El archivo deniega el acceso a la escritura y no podrias hacerlo

// 3. ¿Por qué es importante comprobar si un archivo existe antes de crearlo?

// Para crearlo si no existe y asi ahorrar al programa leer lineas de codigo

// 4. ¿Qué sucede si intentas crear un archivo en una ruta donde no existe la carpeta contenedora?

// que no puede crearlo