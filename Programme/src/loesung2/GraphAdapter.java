package loesung2;

import de.fhstralsund.vinets.geometry.NetElementGeometry;
import de.fhstralsund.vinets.graphics.NetElementRepresentation;
import de.fhstralsund.vinets.structure.*;

import java.util.Iterator;
import java.util.Set;

/**
 * A convenience class for Graph-Decorators.
 * All methods are delegated to the encapsulated Graph, so that a special
 * decorator subclass must override only some of these methods.
 * Any instance of the GraphAdapter class is a transparent object, what
 * means that it has no own ID and all its contained NetElements remain
 * hosted by the encapsulated graph.
 */
public class GraphAdapter
        implements Graph {

  protected Graph G;

  public GraphAdapter(Graph theGraph) {
    G = theGraph;
  }

  public Graph getEncapsulatedGraph() {
    return G;
  }

  @Override
  public boolean isDirected() {
    return G.isDirected();
  }

  @Override
  public boolean isUndirected() {
    return G.isUndirected();
  }

  @Override
  public boolean isMixed() {
    return G.isMixed();
  }

  @Override
  public boolean isHyperGraph() {
    return G.isHyperGraph();
  }

  @Override
  public int countNodes() {
    return G.countNodes();
  }

  @Override
  public int countEdges() {
    return G.countEdges();
  }

  @Override
  public Iterator<Node> nodes() {
    return G.nodes();
  }

  @Override
  public Set<Node> nodeSet() {
    return G.nodeSet();
  }

  @Override
  public Iterator<Edge> edges() {
    return G.edges();
  }

  @Override
  public Set<Edge> edgeSet() {
    return G.edgeSet();
  }

  @Override
  public Node createNode(String name, Object data) {
    return G.createNode(name, data);
  }

  @Override
  public Node createNode(String name) {
    return G.createNode(name);
  }

  @Override
  public Node createNode() {
    return G.createNode();
  }

  @Override
  public Link createEdge(Node start, Node end, boolean directed,
          Object data) {
    return G.createEdge(start, end, directed, data);
  }

  @Override
  public Link createEdge(Node start, Node end, boolean directed) {
    return G.createEdge(start, end, directed);
  }

  @Override
  public Link createEdge(Node start, Node end) {
    return G.createEdge(start, end);
  }

  @Override
  public boolean remove(NetElement elem) {
    return G.remove(elem);
  }

  @Override
  public Object getID() {
    return G.getID();
  }

  @Override
  public String getName() {
    return G.getName();
  }

  @Override
  public void setName(String name) {
    G.setName(name);
  }

  @Override
  public NetElement getHost() {
    return G.getHost();
  }

  @Override
  public NetElement getOriginal() {
    return G.getOriginal();
  }

  @Override
  public NetElementGeometry getGeometry() {
    return G.getGeometry();
  }

  @Override
  public void setGeometry(NetElementGeometry geo) {
    G.setGeometry(geo);
  }

  @Override
  public NetElementRepresentation getRepresentation() {
    return G.getRepresentation();
  }

  @Override
  public void setRepresentation(NetElementRepresentation repres) {
    G.setRepresentation(repres);
  }

  @Override
  public Object getData() {
    return G.getData();
  }

  @Override
  public void setData(Object data) {
    G.setData(data);
  }

  @Override
  public void setLabel(Object key, Object value) {
    G.setLabel(key, value);
  }

  @Override
  public Object getLabel(Object key) {
    return G.getLabel(key);
  }

  @Override
  public Set<?> getLabelKeySet() {
    return G.getLabelKeySet();
  }

  @Override
  public boolean containsLabel(Object key) {
    return G.containsLabel(key);
  }

  @Override
  public void setIntLabel(Object key, int i) {
    G.setIntLabel(key, i);
  }

  @Override
  public int getIntLabel(Object key) {
    return G.getIntLabel(key);
  }

  @Override
  public void setDoubleLabel(Object key, double i) {
    G.setDoubleLabel(key, i);
  }

  @Override
  public double getDoubleLabel(Object key) {
    return G.getDoubleLabel(key);
  }

  @Override
  public boolean getBooleanLabel(Object key) {
    return G.getBooleanLabel(key);
  }

  @Override
  public void setBooleanLabel(Object key, boolean i) {
    G.setBooleanLabel(key, i);
  }

  @Override
  public Object removeLabel(Object key) {
    return G.removeLabel(key);
  }

  @Override
  public void clearLabels() {
    G.clearLabels();
  }

  @Override
  public GraphType getGraphType() {
    return G.getGraphType();
  }

  @Override
  public Graph clone() {
    GraphAdapter copy = null;

    try {
      copy = (GraphAdapter) super.clone();
    } catch (CloneNotSupportedException e) {
    }

    copy.G = (Graph) G.clone();

    return copy;
  }
}
