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

    public void visitNode(int currentIndex, int node) {
        trail[currentIndex + 1] = node;
        visited[node] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    public double trailLength(double graph[][]) {
        double length = graph[trail[nodesNumber - 1]][trail[0]];
        for (int i = 0; i < nodesNumber - 1; i++)
        {
            length += graph[trail[i]][trail[i + 1]];
        }
        return length;
    }

    public void clearVisited()
    {
        Arrays.fill(visited, false);
    }


}