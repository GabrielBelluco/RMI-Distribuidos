import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementação dos métodos de distância no servidor RMI.
 * Estende UnicastRemoteObject para ser exportado automaticamente.
 */
public class DistanceImpl extends UnicastRemoteObject implements DistanceInterface {

    public DistanceImpl() throws RemoteException {
        super();
    }

    /**
     * Valida que os dois vetores são não-nulos e possuem o mesmo comprimento.
     *
     * @throws RemoteException se algum vetor for nulo ou os comprimentos diferirem
     */
    private void validateInputs(double[] obj1, double[] obj2) throws RemoteException {
        if (obj1 == null || obj2 == null) {
            throw new RemoteException("Os vetores de atributos não podem ser nulos.");
        }
        if (obj1.length != obj2.length) {
            throw new RemoteException(
                    "Os vetores de atributos devem ter o mesmo comprimento: "
                    + obj1.length + " != " + obj2.length);
        }
    }

    /**
     * Distância Euclidiana: raiz quadrada da soma dos quadrados das diferenças
     * entre os atributos dos dois objetos.
     *
     * Fórmula: dij = sqrt( sum_k (aik - ajk)^2 )
     */
    @Override
    public double euclideanDistance(double[] obj1, double[] obj2) throws RemoteException {
        validateInputs(obj1, obj2);
        double sum = 0.0;
        for (int k = 0; k < obj1.length; k++) {
            double diff = obj1[k] - obj2[k];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * Distância City Block (Manhattan): soma dos valores absolutos das diferenças
     * entre os atributos dos dois objetos.
     *
     * Fórmula: dij = sum_k |aik - ajk|
     */
    @Override
    public double cityBlockDistance(double[] obj1, double[] obj2) throws RemoteException {
        validateInputs(obj1, obj2);
        double sum = 0.0;
        for (int k = 0; k < obj1.length; k++) {
            sum += Math.abs(obj1[k] - obj2[k]);
        }
        return sum;
    }
}
