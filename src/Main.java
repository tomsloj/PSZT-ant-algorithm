import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {

        double graph[][] = readGraph("network\\network.txt",2);

        Scanner scanner = new Scanner(System.in);

        System.out.println("podaj wierzcholek poczatkowy (1-" + graph.length + ")");
        int startNode = scanner.nextInt();
        System.out.println("podaj wierzcholek koncowy");
        int endNode = scanner.nextInt();

        System.out.println("Liczę...");

        AntAlgorithm aA = new AntAlgorithm(graph,startNode,endNode);
        int result[] = aA.solve();

    }
    public static double[][] readGraph(String path,int version){


        String[] words = null;
        int numberOfNodes = 0;
        double[][] graph = new double[0][];
        ArrayList<Double> positionX = new ArrayList<>();
        ArrayList<Double> positionY = new ArrayList<>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                words = data.split(" ");
                if(words[0].equals("NODES")){
                    while(!words[0].equals(")")){
                        data = myReader.nextLine();
                        words = data.split(" ");
                        if(!words[0].equals(")")){
                            positionX.add(Double.parseDouble(words[4]));
                            positionY.add(Double.parseDouble(words[5]));
                        }
                        numberOfNodes = numberOfNodes +1;
                    }
                    numberOfNodes = numberOfNodes -1;
                }
                if(words[0].equals("LINKS")){
                    graph = new double[numberOfNodes][numberOfNodes];
                    for(int x = 0 ; x<graph.length;x++){
                        for(int y=0 ; y<graph.length;y++){
                            graph[x][y] = -1.0;
                        }
                    }
                    while(!words[0].equals(")")){
                        data = myReader.nextLine();
                        words = data.split(" ");
                        if(!words[0].equals(")")){
                            String left = words[4].substring(1);
                            String right = words[5].substring(1);
                            graph[Integer.parseInt(left)-1][Integer.parseInt(right)-1] = weight(version,positionX,positionY,left,right);
                            graph[Integer.parseInt(right)-1][Integer.parseInt(left)-1] = weight(version,positionX,positionY,left,right);
                        }
                    }
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return graph;
    }
    public static double weight(int version,ArrayList<Double> posX , ArrayList<Double> posY ,String left , String right ){
        if(version==1)
            return 1;
        double lengthX = Math.abs(posX.get(Integer.parseInt(right)-1) -posX.get(Integer.parseInt(left)-1) );
        double lengthY = Math.abs(posY.get(Integer.parseInt(right)-1) -posY.get(Integer.parseInt(left)-1) );
        return Math.sqrt(Math.pow(lengthX,2) +Math.pow(lengthY,2));
    }
}
