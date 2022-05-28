package loesung2;

import java.util.HashMap;
import java.util.Iterator;

import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.GraphAdapter;
import de.fhstralsund.vinets.structure.Link;
import de.fhstralsund.vinets.structure.NetElement;
import de.fhstralsund.vinets.structure.Node;

/**
 * View of a Graph as with its adjacency matrix.
 * It is possible to create/remove an Edge in constant time, but
 * create/remove a Node are unsupported operations.
 * The Graph cannot contain parallel Edges.
 * HyperGraphs are not supported.
 */
public class MatrixDecorator extends GraphAdapter {

  private int size; // number of nodes
  private boolean[][] A; // adjacency matrix
  private HashMap<Node, Integer> nodeNumbers; // mapping of Nodes to numbers
  private Node[] V; // reference to Node object by index i
  private Edge[][] E; // reference to Edge object for (i, j)

  /**
   * The constructor computes the adjacency matrix for the given Graph.
   * All parallel Edges are removed from theGraph.
   *
   * @param theGraph  the Graph to decorate with its adjacency matix.
   */
  public MatrixDecorator(Graph theGraph) {
    super(theGraph);
    if (theGraph.isHyperGraph()) {
      throw new IllegalArgumentException("For hypergraphs an adjacency"
              + " matrix makes no sense.");
    }
    enumerateNodes();
    makeMatrix();
  }

  @Override
  public Node createNode(String name, Object data) {
    throw new UnsupportedOperationException(
            "Cannot create a new node in an adjacency matrix.");
  }

  @Override
  public Node createNode(String name) {
    throw new UnsupportedOperationException(
            "Cannot create a new node in an adjacency matrix.");
  }

  @Override
  public Node createNode() {
    throw new UnsupportedOperationException(
            "Cannot create a new node in an adjacency matrix.");
  }

  @Override
  public Link createEdge(Node start, Node end, boolean directed,
          Object data) {
    int row = nodeNumbers.get(start);
    int column = nodeNumbers.get(end);

    if (A[row][column]) {
      throw new IllegalArgumentException("The edge already exists.");
    }

    // zuerst Graph-Adapter-Methode nutzen:
    Edge e = (Edge) super.createEdge(start, end, directed, data);

    // dann Adjazentmatrix aendern:
    A[row][column] = true;
    E[row][column] = e;

    if (!directed) {
      A[column][row] = true;
      E[column][row] = e;
    }

    return e;
  }

  @Override
  public Link createEdge(Node start, Node end, boolean directed) {
    return createEdge(start, end, directed, null);
  }

  @Override
  public Link createEdge(Node start, Node end) {
    return createEdge(start, end, true, null);
  }

  @Override
  public boolean remove(NetElement elem) {
    if (elem instanceof Edge) {
      Edge e = (Edge) elem;
      Node u = e.getSource();
      Node v = e.getTarget();
      int row = nodeNumbers.get(u);
      int column = nodeNumbers.get(v);

      // zuerst Eintrag in Adjazenzmatrix löschen:
      A[row][column] = false;
      E[row][column] = null;

      if (e.isUndirected()) {
        A[column][row] = false;
        E[column][row] = null;
      }

      // dann Graph-Adapter-Methode aufrufen:
      return super.remove(elem);
    } // else: Knoten können aus Adjazenzmatrix nicht entfernt werden!
    throw new IllegalArgumentException("Only edges can be removed.");
  }

  @Override
  public Object clone() {
    // copy the Graph:
    MatrixDecorator copy = (MatrixDecorator) super.clone();

    // Enumerate the Nodes in their original ordering:
    copy.nodeNumbers = new HashMap<>(size);

    Iterator<Node> nodeIt = copy.G.nodes();
    while (nodeIt.hasNext()) {
      Node neu = (Node) nodeIt.next();
      Node alt = (Node) neu.getOriginal();
      Integer nr = nodeNumbers.get(alt);
      copy.nodeNumbers.put(neu, nr);
      copy.V[nr] = neu;
    }

    // Construct the new adjacency and Edge matrices:
    copy.makeMatrix();

    return copy;
  }

  /* Additional methods for the matrix view: ----------------------- */
  public boolean isEdgeTrue(int i, int j) {
    return A[i][j];
  }

  public boolean isEdgeTrue(Node start, Node end) {
    int row = nodeNumbers.get(start);
    int column = nodeNumbers.get(end);
    return A[row][column];
  }

  public Edge setEdgeTrue(int i, int j) {
    if (!A[i][j]) {
      return (Edge) createEdge(V[i], V[j]);
    } else {
      return null;
    }
  }

  public Edge setEdgeTrue(Node start, Node end) {
    int row = nodeNumbers.get(start);
    int column = nodeNumbers.get(end);
    if (!A[row][column]) {
      return (Edge) createEdge(start, end);
    } else {
      return null;
    }
  }

  public void setEdgeFalse(int i, int j) {
    if (A[i][j]) {
      remove(E[i][j]);
    }
  }

  public void setEdgeFalse(Node start, Node end) {
    int row = nodeNumbers.get(start);
    int column = nodeNumbers.get(end);
    if (A[row][column]) {
      remove(E[row][column]);
    }
  }

  public String getMatrixAsString() {
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
    return new String(s);
  }

  /* Auxiliary methods: --------------------------------------------- */
  // Enumerate all nodes sequentially:
  private void enumerateNodes() {
    size = G.countNodes();
    nodeNumbers = new HashMap<>(size);
    V = new Node[size];

    int nr = 0;
    Iterator<Node> nodeIt = G.nodes();
    while (nodeIt.hasNext()) {
      Node v = nodeIt.next();
      v.setName("" + nr);  // Fuer die Anzeige der Knotennummer.
      nodeNumbers.put(v, nr);
      V[nr] = v;
      nr++;
    }
  }

  // Add all Edges to the acdjcency matrix.
  private void makeMatrix() {
    A = new boolean[size][size];
    E = new Edge[size][size];

    Iterator<Edge> edgeIt = G.edges();
    while (edgeIt.hasNext()) {
      Edge e = edgeIt.next();
      Node u = e.getSource();
      Node v = e.getTarget();
      int row = nodeNumbers.get(u);
      int column = nodeNumbers.get(v);

      if (A[row][column]) { // remove parallel Edge
        G.remove(e);
      } else {
        A[row][column] = true;
        E[row][column] = e;
        if (e.isUndirected()) {
          A[column][row] = true;
          E[column][row] = e;
        }
      }
    }
  }
}
