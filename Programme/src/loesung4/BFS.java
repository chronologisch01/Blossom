package loesung4;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import static uebung4.Labels.*;

/**
 * Implementation of Breadth-First-Search.
 * The Graph should be undirected and connected.
 */
public class BFS implements Algorithm {

  protected Graph G;
  protected String resultMessage; // output text
  
  protected int step; // current BFS-number 
  protected Queue<Node> Q; // contains GRAY nodes
  
  protected Node[] V; // all nodes in BFS-order
  protected int[] width; // number of nodes in level (index = distance = level)
  protected int max; // maximal distance from root

  /**
   * @return  text for the item.
   */
  @Override
  public String getName() {
    return "BFS";
  }

  /**
   * @return  hint about the Algorithm.
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
   * BFS
   * @param input encapsulates the Graph shown in the main window.
   * @return the modified Graph encapsulated together with an output text.
   */
  @Override
  public Parameter execute(Parameter input) {
    G = input.getGraph();
    int n = G.countNodes();
    if (n == 0) {
      return new Parameter(G, "Empty Graph!");
    }

    Q = new ArrayDeque<>();
    V = new Node[n + 1];   // Knotennummern 1 bis n vergeben
    width = new int[n];

    init();

    executeBFS();

    generateOutput();

    return new Parameter(G, resultMessage);
  }
  
// protected methods: ------------------------------------------------------ 
  /**
   * Changes the geometry of the Graph according to the distance to root.
   */
  protected void changeGeometry() {
    double h = G.getGeometry().getHeight();
    double w = G.getGeometry().getWidth();
    double deltaY = h / (max + 2);

    // Assign x according to distance:
    int i = 1;
    for (int dist = 0; dist <= max; dist++) {
      double deltaX = w / (width[dist] + 1);
      // Place all Nodes with the same distance to the start Node horizontally:
      for (int j = 0; j < width[dist]; j++) {
        V[i].getGeometry().setY(deltaY * (dist + 1));
        V[i].getGeometry().setX(deltaX * (j + 1));
        // Color tree Edges BLUE:
        if (i != 1) {
          Edge e = (Edge) V[i].getLabel(FATHER);
          e.getRepresentation().setColor(java.awt.Color.BLUE);
        }
        V[i].clearLabels();
        i++;
      }
    }
  }

  /** Initialization. */
  protected void init() {
    max = 0;
    step = 1;

    Iterator<Node> nodeIt = G.nodes();

    // Use the first Node as start Node - G is not empty:
    Node v = nodeIt.next();
    v.setLabel(COLOR, GRAY);
    v.setLabel(DISTANCE, 0);
    width[0] = 1; // the first level contains one Node
    v.setLabel(FATHER, null);
    Q.add(v);

    // Other Nodes are undiscovered:
    while (nodeIt.hasNext()) {
      v = nodeIt.next();
      v.setLabel(COLOR, WHITE);
    }
  }
  
// private methods: -------------------------------------------------
  /** Main part of the BFS-Algorithm. */
  private void executeBFS() {
    while (!Q.isEmpty()) {
      Node u = Q.element();
      V[step] = u;
      u.setLabel(BFSNO, step);
      u.setName("BFS-#" + step++);

      // Proceed all neighbours of u:
      Iterator<Edge> edgeIt = u.undirectedEdges();
      while (edgeIt.hasNext()) {
        Edge e = edgeIt.next();
        Node v = e.getOtherEnd(u); // second end of the Edge

        if (v.getLabel(COLOR) == WHITE) { // new Node ?
          v.setLabel(COLOR, GRAY);
          v.setLabel(FATHER, e); // reference to the tree Edge

          max = u.getIntLabel(DISTANCE) + 1;
          v.setIntLabel(DISTANCE, max);
          width[max]++;

          Q.add(v);
        }
      }

      u.setLabel(COLOR, BLACK);
      Q.remove(); // remove current head (Node u) from Queue
    }
  }

  private void generateOutput() {
    step--; // letzte vergebene BFS-Nummer
    resultMessage = step + " Knoten wurden mit BFS erreicht.";
    if (step < G.countNodes()) {
      resultMessage += " Der Graph ist nicht zusammenhaengend.";
    } else {
      changeGeometry();
    }
  }
}
