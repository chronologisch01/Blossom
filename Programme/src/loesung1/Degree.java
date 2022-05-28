package loesung1;

import java.util.Iterator;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;
import java.util.ArrayList;
import java.util.List;

public class Degree implements Algorithm {

  /**
   * @return  Beschriftung des Items.
   */
  @Override
  public String getName() {
    return "Knotengrad";
  }

  /**
   * @return Hilfe zur Funktion des Algorithmus.
   */
  @Override
  public String getHint() {
    return "Bestimmt den maximalen Knotengrad.";
  }

  /**
   * @param p
   * @return true if the input graph is not mixed.
   */
  @Override
  public boolean accept(Parameter p) {
//        Graph g = p.getGraph();
//        if (g.isDirected() || g.isUndirected()) {
//            return true;
//        } else {
//            return false;
//        }
    return true;
  }

  /**
   * Algorithmus wird auf den Graph im input angewendet.
   * Das Ergebnis ist ein Parameter-Objekt = ein Paar, bestehend aus einem
   * (neuen bzw. veraenderten) Graphen und einer Meldung.
   * Konkret hier: Der alte Graph wird leicht veraendert wieder zurueckgegeben.
   * Jeder Graphknoten bekommt vom Algorithmus die Anzahl seiner inzidenten
   * Kanten als Name. Als Meldung wird der Maximalgrad im Graphen
   * zurueckgegeben. Der Knoten mit dem maximalen Knotengrad wird rot gefaerbt.
   * In gerichteten Graphen werden fuer den Grad die herausfuehrenden und die
   * hereinfuehrenden Kanten zusammen gezaehlt.
   *
   * @param input   der gegebene Graph - wird gerade im Editor angezeigt
   * @return        der Ergebnisgraph (mit Knotennamen) und "Maximalgrad = ..."
   */
  @Override
  public Parameter execute(Parameter input) {
    Graph theGraph = input.getGraph();

    if (theGraph.countEdges() == 0) {
      return new Parameter(theGraph, "Der Graph hat keine Kanten.");
    }

    // else:
    // Algorithmus nur ausführen, wenn mindestestens eine Kante vorhanden

    int maxDegree = 0;
    Node maxNode = null;

    //Schleife ueber alle Knoten:
    Iterator<Node> i = theGraph.nodes();
    while (i.hasNext()) {
      Node v = i.next();

//            int d = 0;
//            if (theGraph.isUndirected()) {
//                d = v.degree();
//            } else if (theGraph.isDirected()) {
//                d = v.indegree() + v.outdegree();
//            }

      int d = v.degree() + v.indegree() + v.outdegree();
      v.setName(""+d);
      if (d > maxDegree) {
        maxDegree = d;
        maxNode = v;
      }
    }

    maxNode.getRepresentation().setColor(java.awt.Color.RED);
    String resultMessage = "Der Maximalgrad ist " + maxDegree + ".";

    return new Parameter(theGraph, resultMessage);
  }
}
