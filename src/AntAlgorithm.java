import java.util.*;
import java.util.stream.IntStream;

public class AntAlgorithm {
    private int c = 1;
    private double alpha = 1;
    private double beta = 5;
    private double evaporation = 0.5; //ile feromonow odparowuje w kazdej iteracji
    private double amountOfAntPheromones = 500; //liczba feromonow zostawiana przez mrowke
    private double amountOfAntsPerNode = 0.8; //średnia liczba mrowek w jednym wierzcholku
    private double randomFactor = 0.01;

    private int numberOfIterations = 1000000;

    private int numberOfNodes;
    private int numberOfAnts;
    private int graph[][];
    private int trails[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random(0);
    private double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    AntAlgorithm(int numberOfNodes) {
        graph = generateRandomMatrix(numberOfNodes);
        this.numberOfNodes = numberOfNodes;
        numberOfAnts = (int) (numberOfNodes * amountOfAntsPerNode);

        trails = new int[numberOfNodes][numberOfNodes];
        probabilities = new double[numberOfNodes];
        for( int i = 0; i < numberOfAnts; ++i )
        {
            ants.add(new Ant(numberOfNodes));
        }
//        IntStream.range(0, numberOfAnts)
//                .forEach(i -> ants.add(new Ant(numberOfNodes)));
        random = new Random();
    }

    /**
     * tworzy losowy graf
     */
    public int[][] generateRandomMatrix(int n) {
        int[][] randomMatrix = new int[n][n];
        IntStream.range(0, n)
                .forEach(i -> IntStream.range(0, n)
                        .forEach(j -> randomMatrix[i][j] = Math.abs(random.nextInt(100) + 1)));
        return randomMatrix;
    }

    /**
     * uruchamia algorytm
     * @return kolejnosc odwiedzanych wierzcholkow na najlepszej sciezce
     */
    public int[] solve() {
        setupAnts();
        clearTrails();
        for( int i = 0; i < numberOfIterations; ++i )
        {
            moveAnts();
            updateTrails();
            updateBest();
        }
        System.out.println("Best tour length: " + (bestTourLength - numberOfNodes));
        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));
        return bestTourOrder.clone();
    }


    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        IntStream.range(0, numberOfAnts)
                .forEach(i -> {
                    ants.forEach(ant -> {
                        ant.clearVisitedArray();
                        ant.visitNode(-1, random.nextInt(numberOfNodes));
                    });
                });
        currentIndex = 0;
    }

    /**
     * przesowamy kazda mrowke
     */
    private void moveAnts() {
        IntStream.range(currentIndex, numberOfNodes - 1)
                .forEach(i -> {
                    ants.forEach(ant -> ant.visitNode(currentIndex, selectNextNode(ant)));
                    currentIndex++;
                });
    }

    /**
     * znajdujemy nastepny wierzcholek na ktory powinna pojsc mrowka
     * @param ant mrowka dla ktorej szukamy nastepnego wierzcholka
     * @return index nastepnego wierzcholka do ktorego pojdzie mrowka
     */
    private int selectNextNode(Ant ant) {
        int t = random.nextInt(numberOfNodes - currentIndex);
        //bierzemy losowy wierzcholek
        if (random.nextDouble() < randomFactor) {
            OptionalInt nodeIndex = IntStream.range(0, numberOfNodes)
                    .filter(i -> i == t && !ant.isVisited(i))
                    .findFirst();
            if (nodeIndex.isPresent()) {
                return nodeIndex.getAsInt();
            }
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            total += probabilities[i];
            if (total >= r) {
                return i;
            }
        }

        throw new RuntimeException("There are no other cities");
    }

    /**
     * oblicz prawdomodobienstwa dojscia do kolejnego wierzcholka
     */
    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int j = 0; j < numberOfNodes; j++)
        {
            if (!ant.isVisited(j)) {
                pheromone += Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
            }
        }
        for (int j = 0; j < numberOfNodes; j++)
        {
            //TODO dodanie warunku, ze prawdopodobienstwo = 0 gdzu nie ma krawedzi
            if (ant.isVisited(j))
            {
                probabilities[j] = 0.0;
            }
            else
            {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * aktualizujemy sciezki i wyparowane feromony
     */
    private void updateTrails() {
        for (int i = 0; i < numberOfNodes; i++)
        {
            for (int j = 0; j < numberOfNodes; j++)
            {
                trails[i][j] *= evaporation;
            }
        }
        for (Ant ant : ants)
        {
            double contribution = amountOfAntPheromones / ant.trailLength(graph);
            for (int i = 0; i < numberOfNodes - 1; i++)
            {
                trails[ant.trail[i]][ant.trail[i + 1]] += contribution;
            }
            trails[ant.trail[numberOfNodes - 1]][ant.trail[0]] += contribution;
        }
    }

    /**
     * aktualizujemy najlepsze rozwiazanie
     */
    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0)
                    .trailLength(graph);
        }
        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
    }

    /**
     * czyscimy sciezki
     */
    private void clearTrails() {
        IntStream.range(0, numberOfNodes)
                .forEach(i -> {
                    IntStream.range(0, numberOfNodes)
                            .forEach(j -> trails[i][j] = c);
                });
    }
}
