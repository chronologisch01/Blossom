package uebung1;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;

public class KnotenGrad implements Algorithm {

  @Override
  public boolean accept(Parameter p) {
    return p.getGraph().isUndirected();
  }

  @Override
  public Parameter execute(Parameter p) {
    Graph g = p.getGraph();
    int n = g.countNodes();
	
// Hier Code einfügen...

    String s = "Der Graph hat " + n + " Knoten.";
    return new Parameter(g, s);
  }

  @Override
  public String getHint() {
    return "Knotengrad bestimmen";
  }

  @Override
  public String getName() {
    return "Knotengrad";
  }
}
