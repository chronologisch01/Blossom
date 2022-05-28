package uebung2;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;

/**
 * Warshall-Algorithmus ohne Verwendung der Adjazenzmatrix.
 * Es wird direkt ueber die Kantenlisten iteriert.
 * Der Graph sollte nur gerichtete Kanten enthalten.
 * Der Graph sollte kreisfrei sein - Schlingen koennen z.Z. noch nicht
 * gezeichnet werden.
 */
public class Warshall implements Algorithm {

  private Graph G;

  @Override
  public String getName() {
    return "Warshall";
  }

  @Override
  public String getHint() {
    return "Berechnet die transitive H�lle.";
  }

  @Override
  public boolean accept(Parameter par) {
    if (par.getGraph().isDirected()) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Parameter execute(Parameter p) {
    G = p.getGraph();

    int anz = 0; // Zaehler fuer zugefuegte Kanten

    /** @todo Hier bitte den Algorithmus implementieren! */
    return new Parameter(G, anz + " transitive Kanten zugefuegt");
  }
}
