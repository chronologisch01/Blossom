package loesung2;

import java.util.HashMap;
import java.util.Iterator;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.control.GraphApplication;
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
  private Node[] nodeByIndex; // Zugriff auf Knotenobjekte

  @Override
  public String getHint() {
    return "Berechnet die transitive Huelle.";
  }

  @Override
  public String getName() {
    return "WarshallMatrix";
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isDirected();
  }

  @Override
  public Parameter execute(Parameter par) {
    G = par.getGraph();
    initMatrix();

    int anz = 0;
    for (int k = 0; k < size; k++) {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (!A[i][j] && A[i][k] && A[k][j]) {
            anz++;
            A[i][j] = true;
            Edge e = (Edge) G.createEdge(nodeByIndex[i], nodeByIndex[j]);
            // Geometrie und Grafik der Kante erzeugen, dann faerben:
            GraphApplication.getInstance().getDeployer().deployLink(e);
            e.getRepresentation().setColor(java.awt.Color.BLUE);
          }
        }
      }
    }

    return new Parameter(G, anz + " transitive Kanten zugefuegt");
  }

  // Hilfs-Methode erzeugt die Adjazenzmatrix zum Graph
  // Hierbei werden die Knoten - in zufälliger Reihenfolge - nummeriert.
  private void initMatrix() {
    size = G.countNodes();
    A = new boolean[size][size];
    nodeByIndex = new Node[size];
    HashMap<Node, Integer> nodeNumbers = new HashMap<>(size);

    // Alle Knoten fortlaufend nummerieren:
    int nr = 0;
    Iterator<Node> it = G.nodes();
    while (it.hasNext()) {
      Node v = it.next();
      v.setName("" + nr);
      // v.setName(v.getName() + " neu" + nr); // alten Namen auch behalten
      nodeByIndex[nr] = v;
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

      A[zeile][spalte] = true;
    }
  }
}
