import java.util.Arrays;

public class Ant
{
    private int nodesNumber;

    private boolean visited[];
    int trail[];

    private boolean active = true;

    Ant(int nodesNumber)
    {
        this.nodesNumber = nodesNumber;

        visited = new boolean[nodesNumber];
        trail = new int[nodesNumber];

        Arrays.fill(visited, false);
        Arrays.fill(trail, -1);
    }

    public boolean isActive()
    {
        return active;
    }

    public void deactivation()
    {
        active = false;
    }

    /**
     * oblicza dlugosc sciezki mrowki
     * @param graph wagi krawedzi w grafie
     * @return dlugosc sciezki
     */
    public double trailLength( double[][] graph ) {
        double length = 0;//graph[trail[nodesNumber - 1]][trail[0]];
        for (int i = 0; i < nodesNumber - 1; i++)
        {
            if(trail[i+1]!=-1){
                length += graph[trail[i]][trail[i + 1]];
            }

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
        Arrays.fill(trail, -1);
        active = true;
    }


}