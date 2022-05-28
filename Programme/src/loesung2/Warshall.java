package loesung2;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.control.GraphApplication;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

import java.util.Iterator;

/**
 * Warshall-Algorithmus ohne Verwendung der Adjazenzmatrix.
 * Es wird direkt ueber die Kantenlisten iteriert (while- statt for-Schleifen).
 * Der Graph sollte nur gerichtete Kanten enthalten.
 * Der Graph sollte kreisfrei sein - Schlingen koennen z.Z. noch nicht
 * gezeichnet werden.
 */
public class Warshall implements Algorithm {

  private Graph G;

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isDirected();
  }

  @Override
  public Parameter execute(Parameter p) {
    G = p.getGraph();

    int anz = 0;

    // Alle Knoten als Mitte eines Weges durchlaufen:
    Iterator<Node> k = G.nodes();
    while (k.hasNext()) {
      Node mitte = k.next();

      // Alle hereinfuehrenden Kanten:
      Iterator<Edge> edgeIterIn = mitte.inArcs();
      while (edgeIterIn.hasNext()) {
        Edge in = edgeIterIn.next();
        Node start = in.getSource();

        // mit allen herausfuehrenden Kanten kombinieren:
        Iterator<Edge> edgeIterOut = mitte.outArcs();
        while (edgeIterOut.hasNext()) {
          Edge out = edgeIterOut.next();
          Node end = out.getTarget();

          // Abkuerzende Kante erzeugen, falls noch nicht vorhanden:
          Edge e = (Edge) G.createEdge(start, end);
          if (e != null) {
            // System.out.println(e); // Testausgabe
            anz++;
            // Geometrie und Grafik der Kante erzeugen, dann faerben:
            GraphApplication.getInstance().getDeployer().deployLink(e);
            e.getRepresentation().setColor(java.awt.Color.BLUE);
          }
        }
      }
    }
    return new Parameter(G, anz + " transitive Kanten zugefuegt");
  }

  @Override
  public String getHint() {
    return "Berechnet die transitive Huelle.";
  }

  @Override
  public String getName() {
    return "Warshall";
  }
}
