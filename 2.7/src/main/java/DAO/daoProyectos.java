package DAO;

import java.sql.*;

import static Utiles.utilesConexion.getConexion;
import static Utiles.utilesConexion.sc;

public class daoProyectos {

    public void mostrarProyectos(){
        try(Connection con = getConexion()){
            String linea = "SELECT * FROM proyectos";
            Statement st = con.prepareStatement(linea);
            ResultSet rs = st.executeQuery(linea );
            System.out.println("---------------Proyectos---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | Nombre: %s | Presupuesto: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("presupuesto"));
            }

        } catch (RuntimeException | SQLException e){
            e.getMessage();
        }
    }

    public void insertarProyecto() {
        try (Connection con = getConexion()) {
            System.out.println("Dime el nombre del proyecto");
            String nombre = sc.nextLine();
            System.out.println("Dime el presupuesto:");
            String presupuestoString = sc.nextLine().replace(",",".");
            double presupuesto = Double.parseDouble(presupuestoString);
            if (con != null) {
                String linea = "INSERT INTO proyectos (nombre, presupuesto) VALUES (?,?)";
                PreparedStatement ps = con.prepareStatement(linea);
                ps.setString(1, nombre);
                ps.setDouble(2, presupuesto);
                int resultado = ps.executeUpdate();
                if(resultado>0){
                    System.out.println("proyecto insertado correctamente");
                    mostrarProyectos();
                }else {
                    System.out.println("El proyecto no se ha podido insertar");
                }
            } else {
                System.out.println("Problema con la conexión");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
