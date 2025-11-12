package DAO;

import static Utiles.utilesConexion.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Savepoint;

public class cuentaDAO {

    public void transcacion() {
        try (Connection con = getConexion()) {
            // Desactiva el auto-commit para manejar la transacción manualmente
            con.setAutoCommit(false);

            // PreparedStatement para retirar dinero: resta saldo a la cuenta indicada
            PreparedStatement retirar = con.prepareStatement("UPDATE cuentas SET saldo = saldo - ? WHERE id = ?");
            // PreparedStatement para ingresar dinero: suma saldo a la cuenta indicada
            PreparedStatement ingresar = con.prepareStatement("UPDATE cuentas SET saldo = saldo + ? WHERE id = ?");

            // Configura el primer parámetro (cantidad) y el segundo (id) para la primera actualización
            retirar.setDouble(1, 500);
            retirar.setInt(2, 1);
            // Ejecuta la actualización que retira dinero de la cuenta 1
            retirar.executeUpdate();

            // Configura los parámetros para la segunda actualización
            ingresar.setDouble(1, 500);
            ingresar.setInt(2, 2);
            // Ejecuta la actualización que ingresa dinero en la cuenta 2
            ingresar.executeUpdate();

            // Confirma la transacción: ambas actualizaciones se hacen permanentes
            con.commit();
            System.out.println("Transacción realizada con éxito.");

        } catch (Exception e) {
            // Si ocurre cualquier excepción, imprime la traza
            e.printStackTrace();
            try {
                // Nota: aquí se llama a ConexionBD.getConexion().rollback()
                // Esto obtiene una nueva conexión y llama rollback en ella, lo cual no revierte
                // la transacción de la conexión original. Se mantiene la lógica original sin cambiarla.
                getConexion().rollback();
            } catch (Exception ex) {
                // Imprime la traza si el rollback falla
                ex.printStackTrace();
            }
        }
    }
    public void transcacionConLogsYSavepoint() {
        Connection con = null;
        Savepoint spLog1 = null;

        try {
            con = getConexion();
            con.setAutoCommit(false); // Transacción manual

            // --- TRANSFERENCIA ---
            PreparedStatement retirar = con.prepareStatement("UPDATE cuentas SET saldo = saldo - ? WHERE id = ? AND saldo >= ?");
            PreparedStatement ingresar = con.prepareStatement("UPDATE cuentas SET saldo = saldo + ? WHERE id = ?");

            // Debitar cuenta 1 (con control de saldo)
            retirar.setDouble(1, 500);
            retirar.setInt(2, 1);
            retirar.setDouble(3, 500);
            int filasDebito = retirar.executeUpdate();

            // Acreditar cuenta 2
            ingresar.setDouble(1, 500);
            ingresar.setInt(2, 2);
            int filasCredito = ingresar.executeUpdate();

            if (filasDebito != 1 || filasCredito != 1) {
                con.rollback();
                System.out.println("Error en la transferencia. Rollback total.");
                retirar.close();
                ingresar.close();
                return;
            }

            // --- LOGS ---
            PreparedStatement log = con.prepareStatement("INSERT INTO logs (mensaje) VALUES (?)");

            // LOG 1: débito correcto
            log.setString(1, "Debitado 500€ de la cuenta 1");
            log.executeUpdate();

            // SAVEPOINT después del primer log
            spLog1 = con.setSavepoint("LOG1_OK");

            // LOG 2: abono correcto (si falla, conservamos LOG1 y la transferencia)
            log.setString(1, "Abonado 500€ en la cuenta 2");

            // Simula un fallo aquí si quieres probar el savepoint:
            // if (true) throw new SQLException("Fallo simulado en el segundo log");

            log.executeUpdate();

            // Si todo fue bien: liberar savepoint y commit final
            con.releaseSavepoint(spLog1);
            con.commit();
            System.out.println("Transacción y logs completados.");

            // Cierres (en tu mismo estilo)
            retirar.close();
            ingresar.close();
            log.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    if (spLog1 != null) {
                        // Si falló el segundo log: deshacer solo lo posterior al savepoint
                        con.rollback(spLog1);
                        con.commit(); // confirmamos transferencia + LOG1
                        System.out.println("Fallo en LOG2. Se mantiene la transferencia y el primer log.");
                    } else {
                        // Si falló antes del savepoint: rollback total
                        con.rollback();
                        System.out.println("Rollback total aplicado.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


