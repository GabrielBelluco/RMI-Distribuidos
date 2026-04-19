# RMI-Distribuidos – Clustering por Distância

Implementação em **Java RMI** do cálculo de distância entre objetos para análise
de agrupamento (*clustering*), utilizando as medidas **Euclidiana** e
**City Block (Manhattan)**.

---

## Contexto

A análise de agrupamentos é uma técnica de Aprendizado de Máquina que organiza
dados em grupos de acordo com sua similaridade. Neste trabalho utilizamos medidas
de distância para identificar quais objetos (flores Íris) são mais parecidos entre
si a partir de quatro atributos: *Sepal Length*, *Sepal Width*, *Petal Length* e
*Petal Width*.

### Equações utilizadas

| Medida | Fórmula |
|---|---|
| **Euclidiana** | `dij = √( Σk (aik − ajk)² )` |
| **City Block** | `dij = Σk \| aik − ajk \|` |

Onde `L` é a quantidade de atributos e `aik` / `ajk` são os k-ésimos atributos
dos objetos `i` e `j`.

---

## Estrutura do projeto

```
RMI-Distribuidos/
├── src/
│   ├── DistanceInterface.java   # Interface remota (contrato do serviço)
│   ├── DistanceImpl.java        # Implementação dos cálculos no servidor
│   ├── DistanceServer.java      # Registra o serviço no RMI Registry
│   └── DistanceClient.java      # Envia objetos, chama o servidor e exibe resultado
├── Makefile                     # Automatiza compilação e execução
├── .gitignore
└── README.md
```

---

## Pré-requisitos

- Java JDK 8 ou superior (testado com OpenJDK 17)
- `make` (opcional; os comandos `javac`/`java` também funcionam diretamente)

---

## Compilação

```bash
make compile
# ou manualmente:
mkdir -p out
javac -d out src/*.java
```

---

## Execução

> Abra dois terminais (ou duas máquinas) separados.

### Terminal 1 – Servidor

```bash
make server
# ou manualmente:
java -cp out DistanceServer
```

Saída esperada:
```
Servidor RMI iniciado na porta 1099.
Serviço 'DistanceService' registrado. Aguardando conexões...
```

### Terminal 2 – Cliente

```bash
make client
# ou manualmente:
java -cp out DistanceClient
```

Quando executado em outra máquina, passe o endereço do servidor como argumento:

```bash
java -cp out DistanceClient 192.168.0.10
# ou com porta personalizada:
java -cp out DistanceClient 192.168.0.10 1099
```

---

## Exemplo de uso

O cliente exibe os atributos padrão da **Tabela 1** do enunciado entre colchetes.
Pressione **Enter** para aceitá-los ou digite um novo valor.

```
=== Sistema de Clustering por Distância (RMI) ===
Insira os atributos das 3 flores Íris.
Pressione Enter para usar o valor padrão (valores da Tabela 1).

--- Objeto A ---
  Sepal Length [5.1]: 
  Sepal Width  [3.5]: 
  Petal Length [1.4]: 
  Petal Width  [0.2]: 

--- Objeto B ---
  Sepal Length [6.7]: 
  ...

Escolha a medida de distância:
  1 - Euclidiana
  2 - City Block (Manhattan)
Opção [1/2]: 1

=== Distâncias Euclidiana ===
d(A, B) = 3.9115
d(A, C) = 5.2849
d(B, C) = 1.7000

Par com maior similaridade: B-C  (distância = 1.7000)
```

---

## Arquitetura RMI

```
Cliente                           Servidor
───────                           ────────
DistanceClient                    DistanceServer
    │  lookup("DistanceService")      │
    │─────────────────────────────────▶│
    │                                  │  DistanceImpl
    │  euclideanDistance(obj1, obj2)   │──▶ calcula distância
    │◀─────────────────────────────────│
    │  (repete para cada par)          │
    │                                  │
    │  Exibe resultado ao usuário      │
```

1. **`DistanceInterface`** – Interface `Remote` que define os contratos
   `euclideanDistance` e `cityBlockDistance`.
2. **`DistanceImpl`** – Objeto remoto (estende `UnicastRemoteObject`) que executa
   os cálculos no servidor.
3. **`DistanceServer`** – Cria o `RMI Registry` e registra `DistanceImpl` sob o
   nome `"DistanceService"`.
4. **`DistanceClient`** – Localiza o serviço via `registry.lookup`, envia os
   vetores de atributos e recebe as distâncias calculadas remotamente.

---

## Limpeza

```bash
make clean
```
