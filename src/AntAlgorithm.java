import java.util.*;
import java.util.stream.IntStream;

public class AntAlgorithm {
    private int c = 1;
    private double alpha = 1;
    private double beta = 5;
    private double evaporation = 0.5; //ile feromonow odparowuje w kazdej iteracji
    private double amountOfAntPheromones = 1000; //liczba feromonow zostawiana przez mrowke
    private double amountOfAntsPerNode = 0.8; //średnia liczba mrowek w jednym wierzcholku
    private double randomFactor = 0.01;

    private int numberOfIterations = 1000;

    private int numberOfNodes;
    private int numberOfAnts;
    private double graph[][];
    private int trails[][];
    private List<Ant> ants = new ArrayList<>();
    private Random random;
    private double probabilities[];
    private int startNode;
    private int endNode;

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    AntAlgorithm( double[][] m , int startNode , int endNode, int seed ) {
        graph = m;
        this.numberOfNodes = graph.length;
        this.startNode = startNode - 1;
        this.endNode = endNode - 1;

        numberOfAnts = (int) (numberOfNodes * amountOfAntsPerNode);

        trails = new int[numberOfNodes][numberOfNodes];
        probabilities = new double[numberOfNodes];
        for( int i = 0; i < numberOfAnts; ++i )
        {
            ants.add(new Ant(numberOfNodes));
        }
        random = new Random(seed);
    }

    public int[] getPath()
    {
        int i = 0;
        while(bestTourOrder[i] != -1)
            ++i;
        int array[] = Arrays.copyOfRange(bestTourOrder, 0, i);
        for(int j = 0; j < array.length; ++j )
        {
            array[j] += 1;
        }
        return array;
    }

    public double getPathLenght()
    {
        return bestTourLength;
    }

    /**
     * uruchamia algorytm
     * @return kolejnosc odwiedzanych wierzcholkow na najlepszej sciezce
     */
    public void solve() {
        clearTrails();
        for( int i = 0; i < numberOfIterations; ++i ) // numberOfIterations
        {
            setupAnts();
            moveAnts();
            updateTrails();
            updateBest();
        }
        List<Integer> citiesOrder = new ArrayList<>();
        for(int i = 0 ; i<bestTourOrder.length;i++){
            if(bestTourOrder[i]!=-1)
                citiesOrder.add(bestTourOrder[i]+1);
        }
    }


    /**
     * przygotowanie mrowek do symulacji
     */
    private void setupAnts() {
            for(Ant ant :ants){
                ant.clearVisitedArray();
                ant.visitNode(-1, startNode);
            }
        currentIndex = 0;
    }

    /**
     * przesowamy kazda mrowke
     */
    private void moveAnts() {

        for(int i = currentIndex ; i<numberOfNodes - 1;i++ ){
        for(Ant ant : ants){
            if(!ant.isVisited(endNode))
                ant.visitNode(currentIndex,selectNextNode(ant));
        }
        currentIndex++;
        }
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
                    .filter(i -> i == t && !ant.isVisited(i) && graph[ant.trail[currentIndex]][i] > 0)
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
        for(int i = 0 ;i<numberOfNodes;i++){
            if(!ant.isVisited(i)){
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
            if (!ant.isVisited(j) && graph[i][j] > 0 ) {
                pheromone += Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
            }
        }
        for (int j = 0; j < numberOfNodes; j++)
        {
            if (ant.isVisited(j) || graph[i][j] < 0)
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
                if(ant.trail[i]!=-1 && ant.trail[i+1]!=-1)
                    trails[ant.trail[i]][ant.trail[i + 1]] += contribution;
            }
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
        for( int i = 0; i < numberOfNodes; ++i )
        {
            for( int j = 0; j < numberOfNodes; ++j )
            {
                trails[i][j] = c;
            }
        }
    }
}
