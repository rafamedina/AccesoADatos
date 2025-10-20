import java.io.*;
import java.util.Scanner;
import POJO.Movimiento;

public class Utiles implements Serializable {
    transient Scanner sc = new Scanner(System.in);
    final String archivo = "datos/cuenta.dat";
    Movimiento mov = new Movimiento();
    public void crearArchivos() {
        boolean creado = false;
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            creado = carpeta.mkdir();
            System.out.println("Carpeta creada");
        }
        if (!creado) {
            System.out.println("La carpeta no se ha podido crear");
        } else {
            try {
                File ar = new File(archivo);
                if (!ar.exists()) {
                    ar.createNewFile();
                    System.out.println("Archivo creado");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void guardarCuenta(Cuenta cuenta){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(cuenta);
            System.out.println("Cuenta guardada correctamente.");
        } catch (IOException e) {
            // Capturamos cualquier error de escritura
            System.out.println("Error al guardar la cuenta: " + e.getMessage());
        }
    }

    public Cuenta cargarCuenta(){

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
            Cuenta cuentaCargada = (Cuenta) ois.readObject();
            System.out.println("Cuenta cargada correctamente.");
            return cuentaCargada;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la cuenta: " + e.getMessage());
            return new Cuenta(); // si hay error, devolvemos cuenta vacía
        }
    }

    public double pedirDouble(){
        while(true){
            double numero = sc.nextDouble();
            if(numero >= 0){
              return numero;
            } else {
                System.out.println("El numero que has introducido no es valido, solo numeros positivos");
            }
        }

    }

    public int pedirInt(){
        while(true){
            int num = sc.nextInt();
            if(num >= 0){
                return num;
            }
            System.out.println("Numero no válido");

        }
    }


}
