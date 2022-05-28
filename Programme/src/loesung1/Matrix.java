package loesung1;

import java.util.HashMap;
import java.util.Iterator;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;
import java.util.Arrays;

/**
 * Die Adjazenzmatrix wird bestimmt und als Message zurueckgegeben.
 */
public class Matrix implements Algorithm {

  @Override
  public Parameter execute(Parameter par) {
    Graph G = par.getGraph();

    int size = G.countNodes(); // Knotenanzahl
    boolean[][] A = new boolean[size][size]; // Adjazenzmatrix

    // Zuordnung (Knoten, Nr) merken:
    HashMap<Node, Integer> nodeNumbers = new HashMap<>(size);

    // Alle Knoten fortlaufend nummerieren:
    int nr = 0;
    Iterator<Node> nodeIt = G.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      v.setName("" + nr);  // Fuer die Anzeige der Knotennummer.
      nodeNumbers.put(v, nr); // Autoboxing ergaenzt zu: new Integer(nr)
      nr++;
    }

    // Alle Kanten in der Adjazenzmatrix eintragen:
    Iterator<Edge> edgeIt = G.edges();
    while (edgeIt.hasNext()) {
      Edge e = edgeIt.next();

      Node u = e.getSource();
      Node v = e.getTarget();

      int zeile = nodeNumbers.get(u); // Auto-Unboxing
      int spalte = nodeNumbers.get(v);

      A[zeile][spalte] = true;
      // Ungerichtete Kanten erzeugen 2 Einsen in der Matrix:
      if (e.isUndirected()) {
        A[spalte][zeile] = true;
      }
    }

    // Textausgabe zusammenbasteln:
    StringBuilder s = new StringBuilder("\n");
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (A[i][j]) {
          s.append("   1");
        } else {
          s.append("   0");
        }
      }
      s.append("\n");
    }

    String resultMessage = new String(s);
	
//  Alternative Standardmethode:
//  String resultMessage = java.util.Arrays.deepToString(A);
	
    return new Parameter(G, resultMessage);
  }

  @Override
  public boolean accept(Parameter p) {
    if (p.getGraph().isHyperGraph()) {
      p.setMessage("Can not process HyperGraphs");
      return false;
    } else {
      return true;
    }
  }

  @Override
  public String getName() {
    return "Matrix";
  }

  @Override
  public String getHint() {
    return "Berechnet die Adjazenzmatrix.";
  }
}
