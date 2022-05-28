package BlossomAlgorithm;

import de.fhstralsund.vinets.control.GraphApplication;

public class Start {

    public static void main(String[] unused) {
        GraphApplication GA = GraphApplication.getInstance();
        GA.attachAlgorithm("BlossomAlgorithm.EdmondsBlossom");
    }
}
