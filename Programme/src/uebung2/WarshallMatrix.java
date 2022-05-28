package uebung2;

import java.util.HashMap;
import java.util.Iterator;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

/**
 * Warshall-Algorithmus mit Adjazenzmatrix.
 * Die Knoten werden zuerst nummeriert.
 * Der Graph sollte kreisfrei sein - Schlingen koennen z.Z. noch nicht
 * gezeichnet werden. Neue Kanten sind immer gerichtet.
 */
public class WarshallMatrix implements Algorithm {

  private Graph G;
  private int size; // Knotenanzahl
  private boolean[][] A; // Adjazenzmatrix

  @Override
  public String getName() {
    return "WarshallMatrix";
  }

  @Override
  public String getHint() {
    return "Berechnet die transitive Huelle.";
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isDirected();
  }

  @Override
  public Parameter execute(Parameter par) {
    G = par.getGraph();
    initMatrix();

    int anz = 0; // Zaehler fuer zugefuegte Kanten

    /** @todo Hier bitte den Algorithmus implementieren! */


    return new Parameter(G, anz + " transitive Kanten zugefuegt");
  }

  // Hilfs-Methode erzeugt die Adjazenzmatrix zum Graph - s. 1.Uebung
  private void initMatrix() {
    size = G.countNodes();
    A = new boolean[size][size];

    HashMap<Node, Integer> nodeNumbers = new HashMap<>(size);

    /** @todo
     * Fuer die umgekehrte Zuordnung (nr, Knoten) koennen Sie ebenfalls
     * eine HashMap oder aber ein einfaches Array benutzen.
     */

    // Alle Knoten fortlaufend nummerieren:
    int nr = 0;
    Iterator<Node> it = G.nodes();
    while (it.hasNext()) {
      Node v = it.next();

      v.setName("" + nr);
      nodeNumbers.put(v, nr);
      nr++;
    }

    // Alle Kanten in der Adjazenzmatrix eintragen:
    Iterator<Edge> edgeIt = G.edges();
    while (edgeIt.hasNext()) {
      Edge e = edgeIt.next();

      Node u = e.getSource();
      Node v = e.getTarget();

      int zeile = nodeNumbers.get(u);
      int spalte = nodeNumbers.get(v);

      A[zeile][spalte] = true; // es gibt nur gerichtete Kanten!
    }
  }
}
