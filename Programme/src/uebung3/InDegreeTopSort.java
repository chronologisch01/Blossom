package uebung3;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

/**
 * Die Knoten werden topologisch sortiert, falls der Graph azyklisch ist.
 * Die Knoten-Nummer wird als Name des Knotens angezeigt.
 * Die Geometrie wird veraendert:
 * Die Kanten sollen alle von links nach rechts verlaufen, dabei sollen
 * die Knoten abwechselnd oben und unten liegen.
 */
public class InDegreeTopSort implements Algorithm {

  private static final Object DEGREE = new Object(); // als key fuer Knoten-Label
  private Graph G;
  private int size; // Knotenanzahl
  protected Node[] topSortNode; // Knoten in topologisch sortierter Reihenfolge

  @Override
  public String getName() {
    return "TopSort";
  }

  @Override
  public String getHint() {
    return "Sortiert Knoten topologisch.";
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isDirected();
  }

  @Override
  public Parameter execute(Parameter parm1) {
    /**@todo Diese Methode implementieren*/
    throw new java.lang.UnsupportedOperationException(
            "Methode execute() noch nicht implementiert.");
  }

  /* Hilfsmethoden: ------------------------------------------------- */

  /* Methode darf erst aufgerufen werden, wenn das Knoten-Array gefuellt ist.
  Knoten werden dann in 2 waagerechten Reihen von links nach rechts angeordnet. */
  private void changeGeometry() {
    double h = G.getGeometry().getHeight();
    double dy = h / 3;
    double w = G.getGeometry().getWidth();
    double dx = w / (size + 1);

    for (int nr = 1; nr <= size; nr++) {
      Node v = topSortNode[nr - 1];
      v.getGeometry().setX(dx * nr);
      v.getGeometry().setY(dy * (nr % 2 + 1));
      v.setName("" + nr);
    }
  }
}
