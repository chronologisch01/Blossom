package loesung3;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Die Knoten werden topologisch sortiert, falls der Graph azyklisch ist.
 * Die Knoten-Nummer wird als Name des Knotens angezeigt.
 * Die Geometrie wird veraendert:
 * Die Kanten sollen alle von links nach rechts verlaufen, dabei sollen
 * die Knoten abwechselnd oben und unten liegen.
 */
public class InDegreeTopSort implements Algorithm {

  private static final Object DEGREE = new Object(); // als key fuer Label
  protected Graph G;
  protected int size; // Knotenanzahl
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
  public Parameter execute(Parameter par) {
    G = par.getGraph();
    size = G.countNodes();
    topSortNode = new Node[size];

    if (enumerateNodes() == size) {
      changeGeometry();
      System.out.println(Arrays.toString(topSortNode));
      return new Parameter(G, "Erfolgreich topologisch sortiert.");
    } else {
      topSortNode = null;   // nur teilweise gefuelltes Array beseitigen
      return new Parameter(G, "Graph ist nicht azyklisch.");
    }
  }

  /* Hilfsmethoden: ------------------------------------------------- */
  // Dies ist der eigentliche Algorithmus TopSort.
  // Rueckgabewert = Anzahl der von der Quelle aus besuchten Knoten.
  private int enumerateNodes() {
    ArrayList<Node> zeroNodes = init();  // Fuer Knoten ohne hereinfuehrende Kanten

    // Knoten in topologischer Ordnung nummerieren:
    int nr = 0;
    while (!zeroNodes.isEmpty()) {
      Node u = zeroNodes.remove(zeroNodes.size() - 1); // letzten entfernen (Effizienz!)
      topSortNode[nr++] = u;

      // Alle Nachbarknoten aktualisieren:
      Iterator<Edge> edgeIt = u.outArcs();
      while (edgeIt.hasNext()) {
        Edge e = edgeIt.next();
        Node v = e.getTarget();
        int newDeg = v.getIntLabel(DEGREE) - 1;
        if (newDeg == 0) {
          v.removeLabel(DEGREE);
          zeroNodes.add(v);
        } else {
          v.setIntLabel(DEGREE, newDeg);
        }
      }
    }
    return nr;
  }

  private ArrayList<Node> init() {
    // Datenstruktur fuer Knoten ohne hereinfuehrende Kanten fuellen:
    ArrayList<Node> zeroNodes = new ArrayList<>();

    Iterator<Node> nodeIt = G.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      // Knotengrad soll spaeter verringert werden, ohne Graph zu zerstoeren
      int inDeg = v.indegree();
      if (inDeg == 0) {
        zeroNodes.add(v); // hinten an die Liste anfuegen
      } else {
        v.setIntLabel(DEGREE, inDeg);
      }
    }
    return zeroNodes;
  }

  /* Methode darf erst aufgerufen werden, wenn das Knoten-Array gefuellt ist.
  Knoten werden in 2 waagerechten Reihen von links nach rechts angeordnet. */
  private void changeGeometry() {
    double h = G.getGeometry().getHeight();
    double dy = h / 3;
    double w = G.getGeometry().getWidth();
    double dx = w / (size + 1);

    for (int nr = 1; nr <= size; nr++) {
      Node v = topSortNode[nr - 1];
      v.getGeometry().setX(dx * nr);
      v.getGeometry().setY(dy * (nr % 2 + 1));
      //v.setName("" + nr);
      v.setName(v.getName() + " neu" + nr); // alten Namen auch behalten
    }
  }
}
