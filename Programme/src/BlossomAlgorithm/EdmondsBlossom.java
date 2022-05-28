package BlossomAlgorithm;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Configurable;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;

public class EdmondsBlossom implements Algorithm {

    private Graph graph;

    @Override
    public String getName(){return "Blossom";}

    @Override
    public String getHint(){
        return "Noch am Tryen";
    }

    @Override
    public boolean accept(Parameter p){
        graph = p.getGraph();
        return graph.isUndirected();
    }

    @Override
    public Parameter execute(Parameter input){
        return input;
    }


}
