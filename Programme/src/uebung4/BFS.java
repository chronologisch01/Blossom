package uebung4;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import static uebung4.Labels.BLACK;
import static uebung4.Labels.COLOR;

/**
 * Implementation of Breadth-First-Search. The graph should be undirected and
 * connected.
 */
public class BFS implements Algorithm {

  private Graph G; // current graph
  private int step; // current BFS-number
  private Queue<Node> Q; // contains GRAY nodes

  /**
   * @return text for the button.
   */
  @Override
  public String getName() {
    return "BFS";
  }

  /**
   * @return hint about the algorithm.
   */
  @Override
  public String getHint() {
    return "Nummeriert Knoten des ungerichteten Graphen mit Breitensuche.";
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isUndirected();
  }

  /**
   * @param input
   * @return
   * @todo ergaenzen Sie diese Methode ! */
  @Override
  public Parameter execute(Parameter input) {
    G = input.getGraph();
    Q = new ArrayDeque<>();

    init();

    step = 1;

    executeBFS();

    generateOutput();

    return new Parameter(G, "noch nicht implementiert");
  }

  // private methods: -----------------------------------------------------
  /** @todo Implementieren Sie diese Methode! */
  /** Label initialisieren und Startknoten waehlen. */
  private void init() {
  }

  /** @todo ergaenzen Sie diese Methode ! */
  /** Hauptteil des BFS-Algorithmus. */
  private void executeBFS() {
    while (!Q.isEmpty()) {
      Node u = Q.element();
      // Behandle u:
      // ...

      // Nachbarn von u:
      Iterator<Edge> edgeIt = u.undirectedEdges();
      while (edgeIt.hasNext()) {
        Edge e = edgeIt.next();
        Node v = e.getOtherEnd(u); // second end of the edge

        // Behandle v:
        // ...

        Q.add(v);
      }
      u.setLabel(COLOR, BLACK);
      Q.remove(); // remove current head (Node u) from Queue
    }
  }

  /**
   * @todo Implementieren Sie diese Methode!
   * Zeigen Sie die BFS-Nr als Knotennamen an.
   * Ordnen Sie die Knoten nach ihrer Entfernung vom Startknoten an.
   */
  private void generateOutput() {
  }
}
