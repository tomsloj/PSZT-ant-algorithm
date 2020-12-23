import sndlib.core.io.*;
import sndlib.core.model.Model;
import sndlib.core.network.Network;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Reader networkReader = null;
        try {
            networkReader = new FileReader("H:\\uczelnia\\PSZT\\algorytm_mrówkowy\\norway--D-B-E-N-C-A-N-N-native\\norway--D-B-E-N-C-A-N-N\\norway.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Reader modelReader = null;
        try {
            modelReader = new FileReader("H:\\uczelnia\\PSZT\\algorytm_mrówkowy\\norway--D-B-E-N-C-A-N-N-native\\norway--D-B-E-N-C-A-N-N\\D-B-E-N-C-A-N-N.txt");
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
            networkWriter = new FileWriter("H:\\uczelnia\\PSZT\\algorytm_mrówkowy\\network_out\\network.txt");
            modelWriter = new FileWriter("H:\\uczelnia\\PSZT\\algorytm_mrówkowy\\network_out\\model.txt");
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
    }
}
