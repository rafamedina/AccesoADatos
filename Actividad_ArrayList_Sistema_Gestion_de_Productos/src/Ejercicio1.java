import java.util.ArrayList;

public class Ejercicio1 {
    static ArrayList<Producto> inventario = new ArrayList<>();

    public static void main(String[] args) {
        ejercicio1();
        ejercicio2();
        ejercicio3();
        ejercicio4();
    }


    public static void ejercicio1(){
        System.out.println("Ejercicio 1");
        inventario.add(new Producto("P001","Portátil",899.99));
        inventario.add(new Producto("P002","Ratón",25.50));
        inventario.add(new Producto("P003","Teclado",45));
        inventario.add(new Producto("P004","Monitor",199.99));
        inventario.add(new Producto("P005","Webcam",59.90));
        for(Producto producto : inventario){
            System.out.println(producto);
        }
    }

    public static void ejercicio2(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Ejercicio 2");
        String codigo = "P003";
        String nombre = "ratón";
        for(Producto producto : inventario){
            if(codigo.equalsIgnoreCase(producto.getCodigo())){
                System.out.println(producto);
            }
        }
        for(Producto producto : inventario){
            if(nombre.equalsIgnoreCase(producto.getNombre())){
                System.out.println(producto);
            }
        }
        System.out.println(inventario.size());

    }
    public static void ejercicio3(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Ejercicio 3");
        for(Producto producto : inventario){
            if(producto.getNombre().equalsIgnoreCase("Monitor")){
                System.out.println("Antes del cambio");
                System.out.println(producto);
                producto.setPrecio(179.99);
                System.out.println("Despues del cambio");
                System.out.println(producto);
            }
        }
        inventario.removeIf(producto -> producto.getNombre().equalsIgnoreCase("Webcam"));
        inventario.add(new Producto("P006","Altavoces",35));

    }
    public static void ejercicio4(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Ejercicio 4");
        double total=0;
        for(Producto p : inventario){
            total += p.getPrecio();
        }
        System.out.println("El total es de : " + total + "€");
        System.out.println("Producto mas caro");
        // Obtener el producto más caro sin reordenar la lista
        if(inventario.isEmpty()){
            System.out.println("El inventario está vacío.");
        } else {
            Producto masCaro = inventario.stream()
                .max(java.util.Comparator.comparingDouble(Producto::getPrecio))
                .orElse(inventario.get(0));
            System.out.println("El producto mas caro es: " + masCaro);
        }
        System.out.println("Productos mayores de 50");
        for(Producto p : inventario){
            if(p.getPrecio()>50){
                System.out.println(p);
            }
        }

    }
}
