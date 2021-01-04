import java.util.Arrays;

public class Ant
{
    private int nodesNumber;

    private boolean visited[];
    int trail[];

    Ant(int nodesNumber)
    {
        this.nodesNumber = nodesNumber;

        visited = new boolean[nodesNumber];
        trail = new int[nodesNumber];

        Arrays.fill(visited, false);
    }

    /**
     * oblicza dlugosc sciezki mrowki
     * @param graph wagi krawedzi w grafie
     * @return dlugosc sciezki
     */
    public int trailLength(int[][] graph) {
        int length = graph[trail[nodesNumber - 1]][trail[0]];
        for (int i = 0; i < nodesNumber - 1; i++)
        {
            length += graph[trail[i]][trail[i + 1]];
        }
        return length;
    }

    /**
     * oznacza wierzcholek jako odwiedzony i dodaje go do sciezki mrowki
     * @param currentIndex liczba wierzcholkow ktore juz sa na sciezce
     * @param node index wierzcholka do odznaczenia
     */
    public void visitNode(int currentIndex, int node) {
        trail[currentIndex + 1] = node;
        visited[node] = true;
    }

    public boolean isVisited(int i) {
        return visited[i];
    }

    public void clearVisitedArray()
    {
        Arrays.fill(visited, false);
    }


}