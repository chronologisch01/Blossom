package uebung5;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

import static uebung4.Labels.*;

/**
 * Implementation of Depth-First-Search.
 * The graph should be undirected and connected.
 */
public class DFS implements Algorithm {

  private Graph G;
  private Node first; // start Node
  private int step; // current DFS-number
  private Deque<Node> stack; // contains GRAY Nodes of "open" blocks
  private boolean biconnected;
  private StringBuilder blocks;
  private String resultMessage; // output text

  /**
   * @return  text for the button.
   */
  @Override
  public String getName() {
    return "DFS";
  }

  /**
   * @return  hint about the algorithm.
   */
  @Override
  public String getHint() {
    return "Nummeriert Knoten des ungerichteten Graphen mit Tiefensuche.";
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isUndirected();
  }

  /**
   * DFS - Testet zweifachen Zusammenhang. Findet Artikulationsknoten und Bloecke.
   *
   * @param input encapsulates the Graph shown in the main window (undirected)
   * @return the modified Graph encapsulated together with an output text
   */
  @Override
  public Parameter execute(Parameter input) {
    G = input.getGraph();

    int size = G.countNodes();
    if (size == 0) {
      return new Parameter(G, "Empty Graph!");
    }

    stack = new ArrayDeque<>(size);
    blocks = new StringBuilder();

    biconnected = true;
    first = null;
    init();
    step = 1;

    executeDFS(first);  // falls G nicht zusammenhaengend mehrfach aufrufen!

    generateOutput();

    return new Parameter(G, resultMessage);
  }

// private methods: ----------------------------------------------------
  /** Initialization. */
  private void init() {
    Iterator<Node> nodeIt = G.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      if (first == null) { // Use the first Node as start Node:
        first = v;
      }
      v.setLabel(COLOR, WHITE);
    }
  }

  /**
   * Main part of the DFS-Algorithm.
   * @param u  current start Node or DFS
   * @return LowPoint-number of Node u
   */
  private int executeDFS(Node u) {
    int lp = step; // erst zum Schluss als Label speichern

    /** @todo hier den Algorithmus aus der Vorlesung einfuegen:
     * Baumkanten rot faerben, DFSNO als Namen verwenden */

    u.setIntLabel(LOWPOINT, lp);
    return lp;
  }

  // lists all Nodes of a block
  private void printStack(Node u) {
    HashSet<Node> s = new HashSet<>(); // Kann nur je 1 Exemplar enthalten!
    s.add(u);
    while (!stack.isEmpty() && stack.getFirst() != u) {
      s.add(stack.removeFirst());
    }
    blocks.append("\nBlock: ").append(s); // s.toString() wird aufgerufen
  }

  private void generateOutput() {
    step--; // letzte vergebene Nummer
    resultMessage = step + " Knoten wurden mit DFS erreicht.";
    if (step < G.countNodes()) {
      resultMessage += " Der Graph ist nicht zusammenhaengend.";
    } else if (biconnected) {
      resultMessage += " Der Graph hat keinen Artikulationspunkt.";
    }
    resultMessage += blocks;
  }
}
