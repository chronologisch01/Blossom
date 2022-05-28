package uebung1;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;

public class KnotenAnzahl implements Algorithm {

  @Override
  public boolean accept(Parameter p) {
    return true;
  }

  @Override
  public Parameter execute(Parameter p) {
    Graph g = p.getGraph();
    int n = g.countNodes();

    String s = "Der Graph hat " + n + " Knoten.";
    return new Parameter(g, s);
  }

  @Override
  public String getHint() {
    return "Knotenanzahl bestimmen";
  }

  @Override
  public String getName() {
    return "Knoten";
  }
}
