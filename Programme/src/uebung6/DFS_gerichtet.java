package uebung6;

import java.util.Iterator;
import java.util.LinkedList;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Configurable;
import de.fhstralsund.vinets.algorithm.Configurator;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;

/**
 * Implementation of Depth-First-Search. Arcs are classified (tree, backward,
 * forward or cross arc). The graph should be directed.
 */
public class DFS_gerichtet implements Algorithm, Configurable {

  private static final String ROOT = "ROOT"; // used as label
  private Graph G;
  private String resultMessage; // output text
  private int step; // current DFS-number
  private LinkedList<Node> white; // contains undiscovered nodes
  private LinkedList<Node> black; // contains nodes in order of finishing

  // methods of interface Algorithm: ---------------------------------------
  /**
   * @return text for the button.
   */
  @Override
  public String getName() {
    return "DFS ==>";
  }

  /**
   * @return hint about the algorithm.
   */
  @Override
  public String getHint() {
    return "Tiefensuche im gerichteten Graphen.";
  }

  @Override
  public boolean accept(Parameter p) {
    G = p.getGraph();
    return G.isDirected();
  }

  /**
   * DFS - klassifiziert Kanten.
   *
   * @param input  contains the Graph shown in the main window (directed)
   * @return the modified Graph encapsulated together with an output text
   */
  @Override
  public Parameter execute(Parameter input) {
    G = input.getGraph();

    int size = G.countNodes();
    if (size == 0) {
      return new Parameter(G, "Empty Graph!");
    }

    // Startknoten ablesen und rot kennzeichnen:
    Node u = (Node) input.getProperty(ROOT);
    u.getRepresentation().setColor(java.awt.Color.RED);

    init();

    discover(u);

    while (!white.isEmpty()) {
      u = white.getFirst();
      u.getRepresentation().setColor(java.awt.Color.ORANGE);  // neue Wurzel
      discover(u);
    }

    generateOutput();

    return new Parameter(G, resultMessage);
  }

  // method of interface Configurable: -----------------------------------
  /**
   * Erzeugt Nutzerdialog zur Auswahl des Startknotens.
   *
   * @param config   Configurator for the dialog provided by the framework
   * @param gr       the Graph
   * @return Parameter containing the Graph with option values as Label.
   */
  @Override
  public Parameter queryConfigurationOptions(Configurator config, Graph gr) {
    int nr = 0;
    int n = gr.countNodes();
    Node[] nodes = new Node[n];
    String[] nodeNames = new String[n];

    Iterator<Node> nodeIt = gr.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      nodes[nr] = v;
      nodeNames[nr++] = v.getName();
    }

    config.addSelectOption(ROOT, nodes[0], nodes, nodeNames,
            "start node: ", "Choose start vertex for DFS.");

    return config.getParameterObject();
  }

  // private methods: ----------------------------------------------------
  /** Initialization. */
  private void init() {
    /**@todo Diese Methode implementieren*/
  }

  /**
   * Recolors a Node from WHITE to GRAY and starts DFS at u.
   * The method is called when the Node is visited for the first time.
   *
   * @param u  discovered Node
   */
  private void discover(Node u) {
    /**@todo Diese Methode implementieren*/
  }

  /**
   * Classifies an Edge when it is visited.
   *
   * @param e  discovered Edge
   */
  private void discover(Edge e) {
    /**@todo Diese Methode implementieren*/
  }

  /**
   * Main part of the DFS-Algorithm, called recursivly.
   *
   * @param u  current start Node for DFS
   */
  private void executeDFS(Node u) {
    // Proceed all neighbours of u:
    Iterator<Edge> edgeIt = u.outArcs();
    while (edgeIt.hasNext()) {
      discover(edgeIt.next());
    }

    finish(u);
  }

  /**
   * Recolors a Node from Gray to Black.
   * The method is called when the Node and its neigbourhood is visited completly.
   *
   * @param u   finished Node
   */
  private void finish(Node u) {
    /**@todo Diese Methode implementieren*/
  }

  private void generateOutput() {
    step--; // letzte vergebene Nummer
    resultMessage = step / 2 + " Knoten wurden mit DFS erreicht.";
  }
}
