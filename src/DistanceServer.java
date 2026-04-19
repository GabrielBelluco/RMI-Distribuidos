import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Servidor RMI que registra o serviço de cálculo de distância.
 * Deve ser iniciado antes do cliente.
 *
 * Uso: java DistanceServer [porta]
 *   porta (opcional) – porta do registro RMI (padrão: 1099)
 */
public class DistanceServer {

    public static void main(String[] args) {
        int port = 1099;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Porta inválida. Usando porta padrão 1099.");
            }
        }

        try {
            // Cria e exporta o objeto remoto
            DistanceImpl distanceService = new DistanceImpl();

            // Cria o registro RMI na porta especificada
            Registry registry = LocateRegistry.createRegistry(port);

            // Registra o serviço com o nome "DistanceService"
            registry.bind("DistanceService", distanceService);

            System.out.println("Servidor RMI iniciado na porta " + port + ".");
            System.out.println("Serviço 'DistanceService' registrado. Aguardando conexões...");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
