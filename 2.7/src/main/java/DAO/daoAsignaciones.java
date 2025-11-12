package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Utiles.utilesConexion.getConexion;

public class daoAsignaciones {
    public void llamarProcedimiento(int idEmpleado, int idProyecto){
        boolean encontrado = false;
        try(Connection con = getConexion()){
            CallableStatement cs = con.prepareCall("{call AsignarEmpleadoAProyecto(?,?)}");
            cs.setInt(1, idEmpleado);
            cs.setInt(2, idProyecto);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                System.out.println("---------------Asignaciones---------------");
                System.out.printf("ID: %d | ID Empleado: %s |  ID Proyecto: %s |  Fecha Asignación: %s\n",
                        rs.getInt("id"), rs.getString("id_empleado"),rs.getString("id_proyecto"),rs.getDate("fecha_asignacion"));
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
