import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Cliente RMI para cálculo de similaridade entre objetos (flores Íris).
 *
 * O programa recebe:
 *   - Os atributos de três flores Íris (A, B e C): Sepal Length, Sepal Width,
 *     Petal Length e Petal Width.
 *   - A medida de distância a ser utilizada (Euclidiana ou City Block).
 *
 * Em seguida chama o servidor remoto para calcular as distâncias entre cada par
 * (A-B, A-C e B-C) e exibe qual par possui a maior similaridade (menor distância).
 *
 * Uso: java DistanceClient [host] [porta]
 *   host  (opcional) – endereço do servidor RMI (padrão: localhost)
 *   porta (opcional) – porta do registro RMI   (padrão: 1099)
 */
public class DistanceClient {

    // Atributos da Tabela 1 do enunciado (valores padrão pré-carregados)
    // Objeto A – Iris Setosa
    private static final double[] DEFAULT_A = {5.1, 3.5, 1.4, 0.2};
    // Objeto B – Iris Versicolor
    private static final double[] DEFAULT_B = {6.7, 3.1, 4.7, 1.5};
    // Objeto C – Iris Virginica
    private static final double[] DEFAULT_C = {6.3, 3.3, 6.0, 2.5};

    public static void main(String[] args) {
        String host = "localhost";
        int port = 1099;

        if (args.length > 0) host = args[0];
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Porta inválida. Usando porta padrão 1099.");
            }
        }

        Scanner scanner = new Scanner(System.in);
        double[][] objects = new double[3][4];
        String[] labels = {"A", "B", "C"};
        double[][] defaults = {DEFAULT_A, DEFAULT_B, DEFAULT_C};
        String[] attrNames = {"Sepal Length", "Sepal Width", "Petal Length", "Petal Width"};

        System.out.println("=== Sistema de Clustering por Distância (RMI) ===");
        System.out.println("Insira os atributos das 3 flores Íris.");
        System.out.println("Pressione Enter para usar o valor padrão (valores da Tabela 1).\n");

        // Leitura dos atributos de cada objeto
        for (int i = 0; i < 3; i++) {
            System.out.printf("--- Objeto %s ---%n", labels[i]);
            for (int k = 0; k < 4; k++) {
                System.out.printf("  %s [%.1f]: ", attrNames[k], defaults[i][k]);
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    objects[i][k] = defaults[i][k];
                } else {
                    try {
                        objects[i][k] = Double.parseDouble(line.replace(',', '.'));
                    } catch (NumberFormatException e) {
                        System.out.println("  Valor inválido, usando padrão " + defaults[i][k]);
                        objects[i][k] = defaults[i][k];
                    }
                }
            }
            System.out.println();
        }

        // Escolha da medida de distância
        int choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.println("Escolha a medida de distância:");
            System.out.println("  1 - Euclidiana");
            System.out.println("  2 - City Block (Manhattan)");
            System.out.print("Opção [1/2]: ");
            String line = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(line);
                if (choice != 1 && choice != 2) {
                    System.out.println("Opção inválida. Digite 1 ou 2.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite 1 ou 2.\n");
            }
        }

        scanner.close();

        try {
            // Conecta ao registro RMI
            Registry registry = LocateRegistry.getRegistry(host, port);
            DistanceInterface distanceService =
                    (DistanceInterface) registry.lookup("DistanceService");

            double distAB, distAC, distBC;
            String measureName;

            // Calcula as distâncias chamando o método remoto
            if (choice == 1) {
                measureName = "Euclidiana";
                distAB = distanceService.euclideanDistance(objects[0], objects[1]);
                distAC = distanceService.euclideanDistance(objects[0], objects[2]);
                distBC = distanceService.euclideanDistance(objects[1], objects[2]);
            } else {
                measureName = "City Block";
                distAB = distanceService.cityBlockDistance(objects[0], objects[1]);
                distAC = distanceService.cityBlockDistance(objects[0], objects[2]);
                distBC = distanceService.cityBlockDistance(objects[1], objects[2]);
            }

            // Exibe as distâncias calculadas
            System.out.printf("%n=== Distâncias %s ===%n", measureName);
            System.out.printf("d(A, B) = %.4f%n", distAB);
            System.out.printf("d(A, C) = %.4f%n", distAC);
            System.out.printf("d(B, C) = %.4f%n", distBC);

            // Encontra o par com maior similaridade (menor distância)
            double minDist = Math.min(distAB, Math.min(distAC, distBC));
            String mostSimilar;
            if (Double.compare(minDist, distAB) == 0) {
                mostSimilar = "A-B";
            } else if (Double.compare(minDist, distAC) == 0) {
                mostSimilar = "A-C";
            } else {
                mostSimilar = "B-C";
            }

            System.out.printf("%nPar com maior similaridade: %s  (distância = %.4f)%n",
                    mostSimilar, minDist);

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
