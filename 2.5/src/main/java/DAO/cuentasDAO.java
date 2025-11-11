package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Utiles.utilesConexion.getConexion;

public class cuentasDAO {

    public void transferenciaSimple() {
        int cuentaOrigen = 1;
        int cuentaDestino = 2;
        double importe = 500.0;

        Connection con = null;

        try {
            con = getConexion();
            con.setAutoCommit(false); // Transacción manual

            // 1. Debitar cuenta origen (solo si tiene saldo suficiente)
            String sqlDebitar = "UPDATE cuentas SET saldo = saldo - ? " +
                    "WHERE id = ? AND saldo >= ?";
            try (PreparedStatement psDebitar = con.prepareStatement(sqlDebitar)) {
                psDebitar.setDouble(1, importe);
                psDebitar.setInt(2, cuentaOrigen);
                psDebitar.setDouble(3, importe);

                int filasDebito = psDebitar.executeUpdate();

                // 2. Acreditar cuenta destino
                String sqlAcreditar = "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?";
                try (PreparedStatement psAcreditar = con.prepareStatement(sqlAcreditar)) {
                    psAcreditar.setDouble(1, importe);
                    psAcreditar.setInt(2, cuentaDestino);

                    int filasCredito = psAcreditar.executeUpdate();

                    // Validar que ambos pasos se realizaron
                    if (filasDebito == 1 && filasCredito == 1) {
                        con.commit();
                        System.out.println("Transferencia realizada correctamente (500€ de 1 a 2).");
                    } else {
                        con.rollback();
                        System.out.println("Error en la transferencia. Operación revertida.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en la transferencia: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Se ha realizado rollback.");
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error cerrando conexión: " + e.getMessage());
                }
            }
        }
    }

}
