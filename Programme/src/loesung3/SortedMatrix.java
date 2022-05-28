package loesung3;

import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Node;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Berechnet die Adjazenzmatrix bei topologisch sortierten Knoten
 */
public class SortedMatrix extends InDegreeTopSort {

  @Override
  public Parameter execute(Parameter par) {
    Parameter p = super.execute(par);

    if (topSortNode == null) {
      return p; // Graph ist nicht azyklisch.
    }

    String s = p.getMessage() + "\n" + matrix();
    return new Parameter(G, s);
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isDirected();
  }

  @Override
  public String getName() {
    return "sortierte Matrix";
  }

  @Override
  public String getHint() {
    return "Berechnet die Adjazenzmatrix in topologischer Ordnung.";
  }

  // Hilfs-Methode erzeugt die Adjazenzmatrix zum Graph
  private String matrix() {
    boolean[][] A = new boolean[size][size]; // Adjazenzmatrix

    HashMap<Node, Integer> nodeNumbers = new HashMap<>(size);

    // Alle Knoten fortlaufend nach topologischer Ordnung nummerieren:
    for (int nr = 0; nr < size; nr++) {
      Node v = topSortNode[nr];
      v.setName("" + nr);
      nodeNumbers.put(v, nr);
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

    // Textausgabe zusammenbasteln:
    StringBuilder s = new StringBuilder("Adjazenzmatrix: \n");
    for (int i = 0; i < size; i++) {
      // s.append(Arrays.toString(A[i]));  // eine Zeile true/false oder:
      for (int j = 0; j < size; j++) {
        if (A[i][j]) {
          s.append("   1");
        } else {
          s.append("   0");
        }
      }

      s.append("\n");
    }

    return new String(s);
  }
}
