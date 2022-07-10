package BlossomAlgorithm;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.control.GraphApplication;
import de.fhstralsund.vinets.graphics.NodeRepresentation;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;


import static uebung4.Labels.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EdmondsBlossom implements Algorithm {

    private Graph graph;
    private Stack<Node> superNodes;

    private Map<List, List<List>> cycles;
    private boolean usedSuperNode;

    @Override
    public String getName() {
        return "Blossom";
    }

    @Override
    public String getHint() {
        return "Noch am Tryen";
    }

    @Override
    public boolean accept(Parameter p) {

        return p.getGraph().isUndirected();
    }

    @Override
    public Parameter execute(Parameter input) {
        superNodes = new Stack<>();
        cycles = new HashMap<>();
        graph = input.getGraph();
        usedSuperNode = false;

        findMaximumMatching();

        changeGeometry();
        if (usedSuperNode) return new Parameter(graph, "Supernodes have been used!");
        return new Parameter(graph, "finished!");
    }

    public void changeGeometry() {
        Set<Node> nodeSet = graph.nodeSet();
        for (Node node : nodeSet) {
            if (node.getLabel(MATE) != null) {
                Iterator<Edge> edgeIterator = node.undirectedEdges();

                while (edgeIterator.hasNext()) {
                    Edge edge = edgeIterator.next();
                    if (!graph.nodeSet().contains(edge.getOtherEnd(node))) continue;
                    if (edge.getOtherEnd(node) == node.getLabel(MATE)) {
                        GraphApplication.getInstance().getDeployer().deployLink(edge);
                        edge.getRepresentation().setColor(java.awt.Color.RED);
                        break;
                    }
                }
            }
        }
    }


    private void invertPath(List<Node> nodeList) {
        if (nodeList == null) {
            return;
        }
        for (int i = 0; i < nodeList.size() - 1; i += 2) {
            Node firstNode = nodeList.get(i);
            Node secondNode = nodeList.get(i + 1);
            firstNode.setLabel(MATE, secondNode);
            secondNode.setLabel(MATE, firstNode);
        }
    }

    private List<Node> constructAugmentingPath(Node node) {
        List<Node> path = new ArrayList<>(List.of(node));
        node = (Node) node.getLabel(FATHER);
        path.add(node);
        while (node.getLabel(MATE) != null) {
            node = (Node) node.getLabel(FATHER);
            path.add(node);
        }

        while (!superNodes.isEmpty()) {
            Node blossom = superNodes.pop();
            expandBlossom(blossom);
            replacePath(blossom, path);
        }

        while (path.get(0).getLabel(MATE) != null) {
            path.add(0, (Node) path.get(0).getLabel(FATHER));
        }

        while (path.get(path.size() - 1).getLabel(MATE) != null) {
            path.add((Node) path.get(path.size() - 1).getLabel(FATHER));
        }

        return path;
    }


    private void clearNodes() {
        Iterator<Node> nodes = graph.nodes();
        while (nodes.hasNext()) {
            Node node = nodes.next();
            node.setLabel(FATHER, null);
            node.setBooleanLabel(VISITED, false);
        }
    }

    private List<Node> findAugmentingPath(Node root) {
        clearNodes();
        // Do need a deque here, because I want to add at the beginning later!
        Deque<Node> queue = new ArrayDeque<>(List.of(root));

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            currentNode.setBooleanLabel(VISITED, true);
            Iterator<Edge> edges = currentNode.undirectedEdges();
            while (edges.hasNext()) {
                Node node = edges.next().getOtherEnd(currentNode);

                if (node == currentNode.getLabel(FATHER)) {
                    continue;
                }

                if (node.getBooleanLabel(VISITED)) {
                    //usedSuperNode=true;
                    List<Node> cycle = getCycle(node, currentNode);
                    if (cycle.size() % 2 == 1) {
                        Node superNode = shrinkBlossom(cycle);

                        for (Node n : cycle) {
                            if (queue.contains(n)) {
                                queue.remove(n);
                            }
                        }

                        while (cycle.contains((Node) node.getLabel(FATHER))) {
                            node = (Node) node.getLabel(FATHER);
                        }
                        superNode.setLabel(FATHER, (Node) node.getLabel(FATHER));
                        superNode.setLabel(MATE, (Node) node.getLabel(MATE));
                        superNode.setBooleanLabel(VISITED,true);
                        queue.addFirst(superNode);
                        superNodes.add(superNode);
                        break;
                    }


                    continue;
                }
                if (node.getLabel(MATE) == null) {
                    node.setLabel(FATHER, currentNode);


                    return constructAugmentingPath(node);
                }
                Node mate = (Node) node.getLabel(MATE);
                if (mate != currentNode) {
                    node.setBooleanLabel(VISITED, true);
                    mate.setLabel(VISITED, true);
                    node.setLabel(FATHER, currentNode);
                    mate.setLabel(FATHER, node);
                    queue.add(mate);
                }


            }

        }
        return null;
    }

    private void findMaximumMatching() {
        boolean foundPath = true;
        while (foundPath) {
            foundPath = false;
            for (Node node : (Set<Node>)graph.nodeSet()) {
                if (node.getLabel(MATE) != null) continue;

                List<Node> path = findAugmentingPath(node);
                if (path == null) {
                    continue;
                }
                invertPath(path);
                foundPath = true;
                break;
            }
        }
    }

    private List<Node> getCycle(Node node1, Node node2) {
        List<Node> ancestors1 = findAncestors(node1);
        List<Node> ancestors2 = findAncestors(node2);
        int i = ancestors1.size() - 1;
        int j = ancestors2.size() - 1;
        // Important: ancestors are LinkedLists!
        ListIterator<Node> ancestors1Iterator= ancestors1.listIterator(ancestors1.size());
        ListIterator<Node> ancestors2Iterator= ancestors2.listIterator(ancestors2.size());


        while (i >= 0 && j >= 0 && ancestors1Iterator.previous() == ancestors2Iterator.previous()) {
            i--;
            j--;
        }
        List<Node> cycle = new ArrayList<>();
        cycle.addAll(ancestors1.subList(0, i + 2));
        cycle.addAll(ancestors2.subList(0, j+1));
        return cycle;
    }

    private List<Node> findAncestors(Node node) {
        List<Node> nodeList = new LinkedList<>(List.of(node));
        while (node.getLabel(FATHER) != null) {
            node = (Node) node.getLabel(FATHER);
            nodeList.add(node);
        }
        return nodeList;
    }

    private Node shrinkBlossom(List<Node> blossom) {
        usedSuperNode=true;
        // Uses UUID because name has to be unique
        Node superNode = graph.createNode("supernode" + UUID.randomUUID().toString());
        HashMap<Node, Set<Node>> nodeMapping = new HashMap<>();
        Set<Edge> toBeRemoved = new HashSet<>();
        for (Node node : blossom) {
            nodeMapping.putIfAbsent(node, new HashSet<>());

            Iterator<Edge> edgeIterator = node.undirectedEdges();
            while (edgeIterator.hasNext()) {
                Edge edge = edgeIterator.next();
                Node other = edge.getOtherEnd(node);
                if (blossom.contains(other)) continue;
                nodeMapping.get(node).add(other);

                toBeRemoved.add(edge);
                try {
                    graph.createEdge(superNode, other);
                } catch(ConcurrentModificationException e){
                    System.out.println("Edge already exists!");
                }

                if (blossom.contains(other.getLabel(FATHER))) {
                    other.setLabel(FATHER, superNode);
                }
            }
        }

        for(Edge edge: toBeRemoved){
            graph.remove(edge);
        }

        superNode.setLabel(INITIAL_MAPPING, nodeMapping);
        return superNode;
    }

    public void expandBlossom(Node blossom) {
        graph.remove(blossom);
        HashMap<Node, Set<Node>> nodeMapping = (HashMap<Node, Set<Node>>) blossom.getLabel(INITIAL_MAPPING);

        for(Map.Entry<Node, Set<Node>> entry : nodeMapping.entrySet()){
            Node key = entry.getKey();
            for(Node value: entry.getValue()){
                graph.createEdge(key, value);
            }
        }

    }

    private void replacePath(Node blossom, List<Node> path) {
        int index = path.indexOf(blossom);
        if (index == -1)return;
        List<Node> tempPath = path.subList(0, index);

        Node current = tempPath.get(tempPath.size() - 1);
        HashMap<Node, Set<Node>> mapping = (HashMap<Node, Set<Node>>) blossom.getLabel(INITIAL_MAPPING);
        for (Node[] edge : getEdges(mapping)) {
            if (edge[0] == current) {
                current = edge[1];
                break;
            }
            if (edge[1] == current) {
                current = edge[0];
                break;
            }
        }

        while (current.getLabel(FATHER) != blossom.getLabel(FATHER)) {
            tempPath.add(current);
            tempPath.add((Node) current.getLabel(MATE));


            Iterator<Edge> edgeIterator = ((Node) current.getLabel(MATE)).undirectedEdges();
            while (edgeIterator.hasNext()) {
                Node other = edgeIterator.next().getOtherEnd((Node) current.getLabel(MATE));
                if (other != current && mapping.containsKey(other)) {
                    current = other;
                    break;
                }
            }

        }
        tempPath.add(current);
        tempPath.addAll(path.subList(index + 1, path.size()));
        path = tempPath;


    }

    private List<Node[]> getEdges(Map<Node, Set<Node>> mapping) {
        List<Node[]> edges = new ArrayList<>();
        for(Map.Entry<Node, Set<Node>> entry : mapping.entrySet()){
            Node key = entry.getKey();
            for(Node value : entry.getValue()){
                edges.add(new Node[]{key, value});
            }
        }
        return edges;
    }

}
