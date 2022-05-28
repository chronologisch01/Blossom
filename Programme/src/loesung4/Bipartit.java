package loesung4;

import java.util.Iterator;

import de.fhstralsund.vinets.graphics.NodeRepresentation;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Node;

import static uebung4.Labels.*;

/**
 * Tests whether the graph is bipartite.
 * The graph should be undirected and connected.
 * Method: BFS
 */
public class Bipartit extends BFS {

  /**
   * @return  text for the item.
   */
  @Override
  public String getName() {
    return "Bipartition";
  }

  /**
   * @return  hint about the algorithm.
   */
  @Override
  public String getHint() {
    return "2-Faerbung, falls moeglich.";
  }

// protected methods: ------------------------------------------------------
  /**
   * Checks bipartiteness and colors the Nodes:
   */
  @Override
  protected void changeGeometry() {
    Iterator<Edge> it = G.edges();
    while (it.hasNext()) {
      Edge e = it.next();
      Node u = e.getSource();
      Node v = e.getTarget();
      if (v.getIntLabel(DISTANCE) == u.getIntLabel(DISTANCE)) {
        resultMessage += " Der Graph ist nicht bipartit.";
        return;
      }
    }
    // Assign color according to distance:
    Iterator<Node> nodeIt = G.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      NodeRepresentation img = (NodeRepresentation) v.getRepresentation();
      img.setFilled(true);
      if (v.getIntLabel(DISTANCE) % 2 == 0) {
        img.setColor(java.awt.Color.RED);
      } else {
        img.setColor(java.awt.Color.BLUE);
      }
    }
  }
}