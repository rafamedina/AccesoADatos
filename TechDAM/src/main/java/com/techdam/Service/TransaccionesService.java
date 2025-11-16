package com.techdam.Service;
import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import static com.techdam.Utiles.utilesConexion.*;

public class TransaccionesService {

    // Método para transferir presupuesto entre proyectos con transacción (con commit y rollback)
    public boolean transferirPresupuesto(int proyectoOrigenId, int proyectoDestinoId, BigDecimal monto) {
        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);  // Iniciar transacción

            // 1. Restar presupuesto al proyecto origen
            if (!restarPresupuesto(con, proyectoOrigenId, monto)) {
                con.rollback();  // Rollback si falla
                return false;
            }

            // 2. Sumar presupuesto al proyecto destino
            if (!sumarPresupuesto(con, proyectoDestinoId, monto)) {
                con.rollback();  // Rollback si falla
                return false;
            }

            con.commit();  // Confirmar cambios si todo va bien
            System.out.println("Presupuesto transferido correctamente.");
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();  // Rollback en caso de excepción
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.out.println("Error al transferir presupuesto: " + e.getMessage());
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);  // Restablecer auto-commit
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    // Método para asignar empleados a un proyecto con savepoints (control parcial de rollback)
    public void asignarEmpleadosConSavepoint(int proyectoId, List<Integer> empleadoIds) {
        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);  // Iniciar transacción

            // Iterar sobre la lista de empleados para asignarlos al proyecto
            for (int empId : empleadoIds) {
                Savepoint savepoint = con.setSavepoint("SP_" + empId);  // Crear savepoint
                try {
                    if (!asignarEmpleado(con, empId, proyectoId)) {
                        con.rollback(savepoint);  // Rollback parcial si la asignación falla
                    }
                } catch (SQLException e) {
                    con.rollback(savepoint);  // Rollback parcial en caso de excepción
                }
            }

            con.commit();  // Confirmar los cambios si todo va bien
            System.out.println("Asignación de empleados completada.");

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();  // Rollback completo en caso de error
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            System.out.println("Error al asignar empleados: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);  // Restablecer auto-commit
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    // Método para restar presupuesto a un proyecto
    private boolean restarPresupuesto(Connection con, int proyectoId, BigDecimal monto) throws SQLException {
        String sql = "UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, monto);
            ps.setInt(2, proyectoId);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // Método para sumar presupuesto a un proyecto
    private boolean sumarPresupuesto(Connection con, int proyectoId, BigDecimal monto) throws SQLException {
        String sql = "UPDATE proyectos SET presupuesto = presupuesto + ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, monto);
            ps.setInt(2, proyectoId);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // Método para asignar un empleado a un proyecto
    private boolean asignarEmpleado(Connection con, int empId, int proyectoId) throws SQLException {
        String sql = "INSERT INTO asignaciones(empleado_id, proyecto_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setInt(2, proyectoId);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }


}

