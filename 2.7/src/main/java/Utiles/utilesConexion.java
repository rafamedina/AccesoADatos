package Utiles;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class utilesConexion {
    private static HikariDataSource dataSource;

    static  {
        // Configuración del pool
        try {
            Properties props = new Properties();

            // try-with-resources para cerrar automáticamente el InputStream
            // Se utiliza ConexionBD.class.getClassLoader() para obtener el classloader.
            try (InputStream input = utilesConexion.class.getClassLoader().getResourceAsStream("db.properties")) {
                // Carga de las propiedades desde el fichero de configuración.
                // Se espera que 'db.url', 'db.user' y 'db.password' estén presentes.
                props.load(input);
            } catch (Exception e) {
                throw new RuntimeException();
            }
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setMaximumPoolSize(5); // máximo de conexiones simultáneas
            config.setMinimumIdle(2);     // conexiones mínimas en espera
            config.setIdleTimeout(30000); // tiempo de inactividad antes de liberar
            //       config.setMaxLifetime(1800000); // vida máxima de una conexión

            dataSource = new HikariDataSource(config);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConexion(){
        try{
            return dataSource.getConnection();
        } catch (SQLException e){
            throw new RuntimeException();
        }

    }

    /**
     * Cierra el HikariDataSource liberando recursos asociados (conexiones físicas, hilos, ...).
     * <p>
     * Uso recomendado:
     * - Invocar al finalizar la aplicación, por ejemplo en un shutdown hook o en el lifecycle
     * del contenedor que despliegue la aplicación.
     */
    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones cerrado.");
        }
    }



}
