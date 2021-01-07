import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wybierz rodzaj wag:");
        System.out.println("1 - wszystkie wagi równe 1");
        System.out.println("2 - wagi równe odległości wierzchołków");
        try {
            int version = scanner.nextInt();

            double graph[][] = readGraph("network\\network.txt", version);


            System.out.println("podaj wierzcholek poczatkowy (1-" + graph.length + ")");
            int startNode = scanner.nextInt();
            System.out.println("podaj wierzcholek koncowy");
            int endNode = scanner.nextInt();
            System.out.println("podaj liczbe iteracji");
            int numberOfIterations = scanner.nextInt();
            System.out.println("podaj ziarno");
            int seed = scanner.nextInt();

            System.out.println("Wybierz tryb");
            System.out.println("1 - pojedyncze uruchomienie");
            System.out.println("2 - testowanie wydajnosci");
            int mode = scanner.nextInt();

            if (mode == 1) {
                System.out.println("Liczę...");

                AntAlgorithm aA = new AntAlgorithm(graph, startNode, endNode, numberOfIterations, seed);
                aA.solve();

                System.out.println("Długość najlepszej ścieżki: " + aA.getPathLenght());
                System.out.println("Najlepsza ścieżka: " + Arrays.toString(aA.getPath()));
            } else if (mode == 2) {
                System.out.println("podaj minimalną wartość parametru alfa");
                double minAlfa = scanner.nextDouble();
                System.out.println("podaj maksymalną wartość parametru alfa");
                double maxAlfa = scanner.nextDouble();
                System.out.println("podaj liczbę progów wartosci alfa");
                int alfaSteps = scanner.nextInt();
                System.out.println("podaj minimalną wartość parametru beta");
                double minBeta = scanner.nextDouble();
                System.out.println("podaj maksymalną wartość parametru beta");
                double maxBeta = scanner.nextDouble();
                System.out.println("podaj liczbę progów wartosci beta");
                int betaSteps = scanner.nextInt();
                System.out.println("podaj minimalną ilość wyparowywowania feromonów");
                double minEvaporation = scanner.nextDouble();
                System.out.println("podaj maksymalną wartość wyparowywowania feromonów");
                double maxEvaporation = scanner.nextDouble();
                System.out.println("podaj liczbę progów wyparowywowania feromonów");
                int evaporationSteps = scanner.nextInt();
                System.out.println("podaj minimalną ilość wyparowywowania feromonów");
                double minRandomFactor = scanner.nextDouble();
                System.out.println("podaj maksymalną wartość wyparowywowania feromonów");
                double maxRandomFactor = scanner.nextDouble();
                System.out.println("podaj liczbę progów wyparowywowania feromonów");
                int randomFactorSteps = scanner.nextInt();

                FileWriter myWriter = new FileWriter("filename.txt");
                //myWriter.write("alfa\tbeta\twyparowywanie\tczynnikLosowy\tczas\twynik\n");
                System.out.println("alfa\tbeta\twyparowywanie\tczynnikLosowy\tczas\twynik");
                for (int i = 0; i < alfaSteps; ++i) {
                    for (int j = 0; j < betaSteps; ++j) {
                        for (int k = 0; k < evaporationSteps; ++k) {
                            for( int l = 0; l < randomFactorSteps; ++l ){
                                for (int m = 0; m < 5; ++m) {
                                    double alfa = minAlfa + i * (maxAlfa - minAlfa) / (alfaSteps - 1);
                                    double beta = minBeta + j * (maxBeta - minBeta) / (betaSteps - 1);
                                    double evaporation = minEvaporation + k * (maxEvaporation - minEvaporation) / (evaporationSteps - 1);
                                    double randomFactor = minRandomFactor + l * (maxRandomFactor - minRandomFactor) / (randomFactorSteps - 1);

                                    long millisActualTime = System.currentTimeMillis();
                                    AntAlgorithm aA = new AntAlgorithm(graph, startNode, endNode, numberOfIterations,
                                                                seed + m, alfa, beta, evaporation, randomFactor);
                                    aA.solve();
                                    long executionTime = System.currentTimeMillis() - millisActualTime;

                                    //myWriter.write(alfa + "\t" + beta + "\t" + evaporation + "\t" +
                                    //        randomFactor + "\t" + executionTime + "\t" + aA.getPathLenght() + "\n");
                                    System.out.println(alfa + "\t" + beta + "\t" + evaporation + "\t" +
                                            randomFactor + "\t" + executionTime + "\t" + aA.getPathLenght());
                                }
                            }
                        }
                    }
                }

            }
        }
        catch(InputMismatchException e)
        {
            e.printStackTrace();
            System.out.println("Błąd podczas wczytywania");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
