package conexion;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class UtilesConexion {

    private static String url;
    private static String user;
    private static String password;
    static Scanner sc = new Scanner(System.in);

    public static String[] llenarProperties(){
        String[] params;
        Properties props = new Properties();
        try (InputStream input = UtilesConexion.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("❌ No se encontró el archivo db.properties");
            }
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Obtener datos de conexión
         url = props.getProperty("db.url");
         user = props.getProperty("db.user");
         password = props.getProperty("db.password");

        params = new String[]{url, user, password};
        return params;
    }


    public static void probarConexion(){
        llenarProperties();
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Conexión establecida con éxito a la base de datos.");

            // Mostrar metadatos
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("🔹 Driver: " + meta.getDriverName());
            System.out.println("🔹 Versión del driver: " + meta.getDriverVersion());
            System.out.println("🔹 Base de datos: " + meta.getDatabaseProductName());
            System.out.println("🔹 Versión BD: " + meta.getDatabaseProductVersion());
            System.out.println("🔹 Usuario conectado: " + meta.getUserName());
            System.out.println("🔹 URL de conexión: " + meta.getURL());

        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static void saltolinea(){
        System.out.println("Pulsa enter para seguir");
        new Scanner(System.in).nextLine();
    }
    }

