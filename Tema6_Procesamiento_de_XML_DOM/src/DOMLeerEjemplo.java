import javax.xml.parsers.*; // Importa las clases necesarias para el análisis de XML
import org.w3c.dom.*; // Importa las clases del modelo de objetos de documento (DOM)
import java.io.File; // Importa la clase File para manejar archivos

public class DOMLeerEjemplo {
    public static void main(String[] args) {
        try {
            File archivo = new File("datos/empleados.xml"); // Crea un objeto File que representa el archivo XML a leer
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // Obtiene una instancia de la fábrica de constructores de documentos
            DocumentBuilder db = dbf.newDocumentBuilder(); // Crea un constructor de documentos a partir de la fábrica
            Document doc = db.parse(archivo); // Parsea el archivo XML y lo convierte en un objeto Document (DOM)

            NodeList lista = doc.getElementsByTagName("empleado"); // Obtiene una lista de nodos con la etiqueta "empleado"

            for (int i = 0; i < lista.getLength(); i++) { // Itera sobre cada nodo "empleado"
                Element emp = (Element) lista.item(i); // Convierte el nodo actual en un elemento
                String id = emp.getAttribute("id"); // Obtiene el atributo "id" del elemento "empleado"
                String nombre = emp.getElementsByTagName("nombre").item(0).getTextContent(); // Obtiene el texto del primer elemento "nombre" hijo
                String puesto = emp.getElementsByTagName("puesto").item(0).getTextContent(); // Obtiene el texto del primer elemento "puesto" hijo
                System.out.println("ID: " + id + " | Nombre: " + nombre + " | Puesto: " + puesto); // Imprime los datos del empleado
            }

        } catch (Exception e) {
            System.out.println("Error al leer: " + e.getMessage()); // Captura e imprime cualquier error que ocurra durante el proceso
        }
    }

}