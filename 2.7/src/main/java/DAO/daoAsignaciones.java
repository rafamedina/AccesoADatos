package DAO;

import java.sql.*;

import static Utiles.utilesConexion.*;
import static Utiles.Utiles.*;


public class daoAsignaciones {
    public void mostrarAsignaciones(){
        try(Connection con = getConexion()){
            String linea = "SELECT * FROM asignaciones";
            Statement st = con.prepareStatement(linea);
            ResultSet rs = st.executeQuery(linea );
            System.out.println("---------------Asignaciones---------------");
            while (rs.next()) {
                System.out.printf("ID: %d | ID Empleado: %d |  ID Proyecto: %d |  Fecha Asignación: %s\n",
                        rs.getInt("id"),
                        rs.getInt("id_empleado"),
                        rs.getInt("id_proyecto"),
                        rs.getDate("fecha_asignacion"));
            }

        } catch (RuntimeException | SQLException e){
            e.getMessage();
        }
    }

    public void llamarProcedimiento(){
        boolean encontrado = false;
        daoEmpleados dE = new daoEmpleados();
        daoProyectos dP = new daoProyectos();
        try(Connection con = getConexion()){

            dE.mostrarEmpleados();
            int idEmpleado = pedirInt("Que empleado quieres subirle el sueldo");

            saltolinea();

            dP.mostrarProyectos();
            int idProyecto = pedirInt("Que Proyecto quieres quitarle presupuesto");


            CallableStatement cs = con.prepareCall("{call AsignarEmpleadoAProyecto(?,?)}");
            cs.setInt(1, idEmpleado);
            cs.setInt(2, idProyecto);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                System.out.println("---------------Asignaciones---------------");
                System.out.printf("ID: %d | ID Empleado: %d |  ID Proyecto: %d |  Fecha Asignación: %s\n",
                        rs.getInt("id"),
                        rs.getInt("id_empleado"),
                        rs.getInt("id_proyecto"),
                        rs.getDate("fecha_asignacion"));
                encontrado = true;
            }
        } catch(SQLException e){

            System.out.println(e.getMessage());
        }
        if(!encontrado){
            System.out.println("El id no existe en nuestra base de datos.");
        }
    }
}
