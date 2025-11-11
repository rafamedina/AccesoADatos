import DAO.clientesDAO;

public class Main {
    public static void main(String[] args) {
        clientesDAO cliente = new clientesDAO();
        cliente.listarEmpleados();
        cliente.incrementar_salario(2,400);
    }

}
