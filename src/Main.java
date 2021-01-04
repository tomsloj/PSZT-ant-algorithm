import sndlib.core.io.*;
import sndlib.core.model.Model;
import sndlib.core.network.Network;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Reader networkReader = null;
        try {
            networkReader = new FileReader("norway--D-B-E-N-C-A-N-N-native\\norway--D-B-E-N-C-A-N-N\\norway.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Reader modelReader = null;
        try {
            modelReader = new FileReader("norway--D-B-E-N-C-A-N-N-native\\norway--D-B-E-N-C-A-N-N\\D-B-E-N-C-A-N-N.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // get the appropriate parser, e.g. for the native format
        SNDlibParser parser = SNDlibIOFactory.newParser(SNDlibIOFormat.NATIVE);

        Network network = null;
        Model model = null;
        try {
            network = parser.parseNetwork(networkReader);
            model = parser.parseModel(modelReader);
        }
        catch(SNDlibParseException spx) {
            System.err.println("could not parse network or model file: " + spx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get the appropriate writer, e.g. for the native format
        SNDlibWriter writer = SNDlibIOFactory.newWriter(SNDlibIOFormat.NATIVE);

        Writer networkWriter = null;
        Writer modelWriter = null;
        try {
            networkWriter = new FileWriter("network_out\\network.txt");
            modelWriter = new FileWriter("network_out\\model.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.writeNetwork(network, networkWriter);
            writer.writeModel(model, modelWriter);
            networkWriter.close();
            modelWriter.close();
        }
        catch(SNDlibWriteException swx) {
            System.err.println("could not write network or model file: " + swx);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double graph[][] = readGraph("network_out\\network.txt",2);
        for(double[] x :graph){
            for(double y :x){
              //  System.out.print(y);
              //  System.out.print("\t");
            }
           //System.out.println();
        }



        AntAlgorithm aA = new AntAlgorithm(graph);
        int result[] = aA.solve();
       // for (int i: result )
       // {
       //     System.out.println(i);
       // }

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
                            System.out.println(words[4] +" "+words[5]);
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
                            graph[x][y] = 0;
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
