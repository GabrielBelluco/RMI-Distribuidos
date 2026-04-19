import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface remota que define os métodos de cálculo de distância
 * disponibilizados pelo servidor RMI.
 */
public interface DistanceInterface extends Remote {

    /**
     * Calcula a distância Euclidiana entre dois objetos.
     *
     * @param obj1 vetor de atributos do primeiro objeto
     * @param obj2 vetor de atributos do segundo objeto
     * @return distância Euclidiana entre os dois objetos
     * @throws RemoteException em caso de falha na comunicação remota
     */
    double euclideanDistance(double[] obj1, double[] obj2) throws RemoteException;

    /**
     * Calcula a distância City Block (Manhattan) entre dois objetos.
     *
     * @param obj1 vetor de atributos do primeiro objeto
     * @param obj2 vetor de atributos do segundo objeto
     * @return distância City Block entre os dois objetos
     * @throws RemoteException em caso de falha na comunicação remota
     */
    double cityBlockDistance(double[] obj1, double[] obj2) throws RemoteException;
}
