import java.io.File;
import java.util.logging.Logger;

public class RutaRelativa {

    private static final Logger log = Logger.getLogger(RutaRelativa.class.getName());

    public static void main(String[] args) {
// Obtener ruta base del proyecto
        String rutaBase = System.getProperty("user.dir");
        String separador = File.separator;
// Construir ruta completa relativa
        String rutaRelativa = rutaBase + separador + "datos" +
                separador
                + "ejemplo.txt";
// Crear objeto File con esa ruta
        File archivo = new File(rutaRelativa);
        String nombre=archivo.getName();
        String ruta = archivo.getParent();
// Mostrar información
        System.out.println(nombre);
        System.out.println(ruta);
        new RutaRelativa().log("Ruta base del proyecto: " + rutaBase);
        new RutaRelativa().log("Separador de carpetas del sistema: " +

                separador);
        new RutaRelativa().log("Ruta relativa completa: " + rutaRelativa);
        new RutaRelativa().log("¿Existe el archivo? " + archivo.exists());
        new RutaRelativa().log("Ruta absoluta real: " +
                archivo.getAbsolutePath());
    }

    public void log(String mensaje) {
        log.info(mensaje);
    }

}