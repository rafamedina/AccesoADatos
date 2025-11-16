package com.techdam.Service;
import static com.techdam.Utiles.utilesConexion.*;
import java.math.BigDecimal;
import java.sql.*;

public class ProcedimientosService {

    // Método para actualizar salario por departamento (llama al procedimiento almacenado)
    public int actualizarSalariosDepartamento(String departamento, double porcentaje) {
        int empleadosActualizados = 0;
        try (Connection con = getConexion();
             CallableStatement cstmt = con.prepareCall("{call actualizar_salario_departamento(?, ?, ?)}")) {

            cstmt.setString(1, departamento);               // Parámetro IN: Departamento
            cstmt.setBigDecimal(2, BigDecimal.valueOf(porcentaje)); // Parámetro IN: Porcentaje
            cstmt.registerOutParameter(3, Types.INTEGER);   // Parámetro OUT: Empleados actualizados

            // Ejecutar el procedimiento almacenado
            cstmt.execute();

            // Obtener el resultado del parámetro OUT
            empleadosActualizados = cstmt.getInt(3);

        } catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento: " + e.getMessage());
        }
        return empleadosActualizados;
    }

    // Método para transferir presupuesto entre proyectos (llama al procedimiento almacenado)
    public boolean actualizarPresupuestoProyecto(int proyectoId, double nuevoPresupuesto) {
        try (Connection con = getConexion();
             CallableStatement cstmt = con.prepareCall("{call actualizar_presupuesto(?, ?)}")) {

            cstmt.setInt(1, proyectoId);          // Parámetro IN: ID del proyecto
            cstmt.setBigDecimal(2, BigDecimal.valueOf(nuevoPresupuesto));  // Parámetro IN: Nuevo presupuesto

            // Ejecutar el procedimiento almacenado
            cstmt.execute();

            System.out.println("Presupuesto del proyecto actualizado correctamente.");
            return true;

        } catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento: " + e.getMessage());
            return false;
        }
    }
}

