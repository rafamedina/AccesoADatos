import DAO.clientesDAO;
import DAO.cuentaDAO;

public class Main {
    public static void main(String[] args) {
        clientesDAO cliente = new clientesDAO();
        cuentaDAO cuenta = new cuentaDAO();
        cuenta.transcacion();
        cuenta.transcacionConLogsYSavepoint();
    }

}
