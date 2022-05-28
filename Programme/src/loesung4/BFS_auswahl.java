package loesung4;

import de.fhstralsund.vinets.algorithm.Configurable;
import de.fhstralsund.vinets.algorithm.Configurator;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.structure.Graph;
import java.util.Iterator;

import de.fhstralsund.vinets.structure.Node;
import java.util.Arrays;

import static uebung4.Labels.*;

/**
 * Tests whether the graph is bipartite.
 * The graph should be undirected and connected.
 * Method: BFS
 */
public class BFS_auswahl extends BFS implements Configurable {
  private static final String FIRST_NODE = "FIRST_NODE";
  private Node start;

  /**
   * @return  text for the item.
   */
  @Override
  public String getName() {
    return "BFS(start)";
  }

  /**
   * @return  hint about the algorithm.
   */
  @Override
  public String getHint() {
    return "Nummeriert Knoten des ungerichteten Graphen mit Breitensuche. "+
            "Nutzer bestimmt Startknoten.";
  }
  
  @Override
  public Parameter execute(Parameter p) {
    // Startknoten ablesen:
    start = (Node) p.getProperty(FIRST_NODE);
    return super.execute(p);
  }
    
    
// method of interface Configurable: -----------------------------------
  /**
   * Erzeugt Nutzerdialog zur Auswahl des Startknotens.
   *
   * @param config   Configurator for the dialog provided by the framework
   * @param graph    the Graph
   * @return Parameter containing the Graph with option values as Label.
   */
  @Override
  public Parameter queryConfigurationOptions(Configurator config, Graph graph) {
     int size = graph.countNodes();
    // Knoten nach Namen sortiert ins Array speichern:
    Node[] nodes = (Node[]) graph.nodeSet().toArray(new Node[size]);
    Arrays.sort(nodes, new NameComparator());

    // Namen der Knoten in gleicher Reihenfolge - zur Anzeige:
    String[] nodeNames = new String[size];
    for (int i = 0; i < size; i++) {
      nodeNames[i] = nodes[i].toString();
    }

    config.addSelectOption(FIRST_NODE, nodes[0], nodes, nodeNames,
            "Startknoten", "Startknoten angeben!");
    return config.getParameterObject();
  }
  
// protected methods: ------------------------------------------------------  
@Override
protected void init() {
    max = 0;
    step = 1;
 
    // Use the start Node selected by user:
    start.setLabel(COLOR, GRAY);
    start.setLabel(DISTANCE, 0);
    width[0] = 1; // the first level contains one Node
    start.setLabel(FATHER, null);
    Q.add(start);

    // Other Nodes are undiscovered:
    Iterator<Node> nodeIt = G.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      if (v!=start) v.setLabel(COLOR, WHITE);
    }
  }
}