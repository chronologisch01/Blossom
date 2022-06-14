package BlossomAlgorithm;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
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
        graph = input.getGraph();
        usedSuperNode = false;

        findMaximumMatching(graph.nodeSet());

        changeGeometry();
        if (usedSuperNode) return new Parameter(graph, "Supernodes have been used!");
        return new Parameter(graph, "finished!");
    }

    public void change() {
        Set<Node> nodeSet = graph.nodeSet();

        for (Node node : nodeSet) {
            if (node.getName().equals("#_981")) {
                Iterator<Edge> edgeIterator = node.undirectedEdges();
                while (edgeIterator.hasNext()) {
                    Node other = edgeIterator.next().getOtherEnd(node);
                    if (other.getName().equals("#_987")) {
                        other.setLabel(MATE, node);
                        node.setLabel(MATE, other);
                    }
                }
            }
        }

    }

    public void changeGeometry() {
        Set<Node> nodeSet = graph.nodeSet();
        // This is really inefficient. But what would be better?
        // We would have to find a way to
        int counter = 0;
        for (Node node : nodeSet) {
            if (node.getLabel(MATE) != null) {
                Iterator<Edge> edgeIterator = node.undirectedEdges();

                while (edgeIterator.hasNext()) {
                    Edge edge = edgeIterator.next();
                    // TODO just for testing purposes, this has to be fixed. Label Mate should always exist in the graph!
                    if (!graph.nodeSet().contains(edge.getOtherEnd(node))) continue;
                    System.out.println(edge);
                    if (edge.getOtherEnd(node) == node.getLabel(MATE)) {
                        counter++;
                        System.out.println("Edge is :" + edge);
                        System.out.println("Representation is :" + edge.getRepresentation());

                        // Somehow edge.getRepresentation is null even though the edge exists!?;
//                        edge.getRepresentation().setColor(java.awt.Color.RED);
                        break;
                    }
                }

            }
        }
        System.out.println("Matching is:" + counter / 2);
        System.out.println("NodeSet is: " + Arrays.toString(graph.nodeSet().toArray()));
        System.out.println("Supernodes are :" + Arrays.toString(superNodes.toArray()));
    }


    private void invertPath(List<Node> nodeList) {
        if (nodeList == null) {
            System.out.println("NodeList is null!!!");
            return;
        }
        if (nodeList.size() % 2 == 0) System.out.println("Size assertion Failed!");
        for (int i = 0; i < nodeList.size() - 1; i += 2) {
            Node firstNode = nodeList.get(i);
            Node secondNode = nodeList.get(i + 1);
            firstNode.setLabel(MATE, secondNode);
            secondNode.setLabel(MATE, firstNode);
            System.out.println("Path constructed between: " + firstNode.getName() + " and " + secondNode.getName());
        }
    }

    private List<Node> constructAugmentingPath(Node node) {
        List<Node> path = new ArrayList<>(List.of(node));
        System.out.println("FATHER: " + node.getLabel(FATHER));
        node = (Node) node.getLabel(FATHER);
        System.out.println("this should not be null:" + node);
        path.add(node);
        System.out.println(node.getLabel(MATE));
        while (node.getLabel(MATE) != null) {
            System.out.println("mate:" + node.getLabel(MATE));
            System.out.println("father:" + node.getLabel(FATHER));
            node = (Node) node.getLabel(FATHER);
            path.add(node);
            System.out.println(node.getLabel(MATE));
        }

        while (superNodes.size() > 0) {
            System.out.println("Supernodes before: " + superNodes);
            Node blossom = superNodes.pop();
            System.out.println("Supernode that should have been deleted :" + blossom);
            System.out.println("Supernodes after: " + superNodes);
            expandBlossom(blossom);
            System.out.println("Path before: " + Arrays.toString(path.toArray()) + " and trying to remove " + blossom);
            replacePath(blossom, path);
            System.out.println("Path after: " + Arrays.toString(path.toArray()));
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
            System.out.println("Queue size before" + queue.size());
            Node currentNode = queue.poll();
            System.out.println("Queue size after" + queue.size());
            currentNode.setBooleanLabel(VISITED, true);
            Iterator<Edge> edges = currentNode.undirectedEdges();
            while (edges.hasNext()) {
                Node node = edges.next().getOtherEnd(currentNode);

                if (node == currentNode.getLabel(FATHER)) {
                    continue;
                }

                if (node.getBooleanLabel(VISITED)) {
                    //usedSuperNode=true;
                    System.out.println("already visited!");
                    List<Node> cycle = getCycle(node, currentNode);
                    System.out.println("Cycle found! : " + cycle);
                    if (cycle.size() % 2 == 1) {
                        System.out.println("Not bipartit!:" + cycle);
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
                        System.out.println("Supernode has been added!");
                        break;
                    }


                    continue;
                }
                System.out.println("findAugmenting (120): " + node.getLabel(MATE));
                if (node.getLabel(MATE) == null) {
                    System.out.println("Father is going to be: " + currentNode);
                    node.setLabel(FATHER, currentNode);


                    return constructAugmentingPath(node);
                }
                Node mate = (Node) node.getLabel(MATE);
                if (mate != currentNode) {
                    System.out.println("CurrentNode and mate are: " + currentNode + node.getLabel(MATE));
                    node.setBooleanLabel(VISITED, true);
                    mate.setLabel(VISITED, true);
                    node.setLabel(FATHER, currentNode);
                    mate.setLabel(FATHER, node);
                    queue.add(mate);
                }


            }

        }
        System.out.println("OUTSIDE OF LOOP");
        System.out.println("AND SUPERNODES ARE:" + Arrays.toString(superNodes.toArray()));
        System.out.println("findAugmentingPath: none found!");
        return null;
    }

    private void findMaximumMatching(Set<Node> nodeSet) {
        // Do I maybe have to reuse Edges?
        HashSet<Node> used = new HashSet<>();
        for (Node node : nodeSet) {
            node.setLabel(MATE, null);
        }
        // change();
        boolean foundPath = true;

        while (foundPath) {

            foundPath = false;
            List<Node> nodeList = new ArrayList<>();
            nodeList.addAll((Set<Node>) graph.nodeSet());
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.get(i);
                // The second one literally cost me 5 hours
                if (used.contains(node) || node.getLabel(MATE) != null) continue;
//                if (node.getLabel(MATE)!=null) continue;


                List<Node> path = findAugmentingPath(node);
                System.out.println("Did found an augmenting path and it is :" + path);
                if (path == null) {
                    System.out.println("findMaximumMatching no path found!");
                    continue;
                }
                invertPath(path);
                used.add(path.get(0));
                used.add(path.get(path.size() - 1));
                foundPath = true;
                break;
            }
        }
        System.out.println("matching found !");
    }


    private List<Node> getCycle(Node node1, Node node2) {
        // TODO: this looks really weird!
        List<Node> ancestors1 = findAncestors(node1);
        List<Node> ancestors2 = findAncestors(node2);

        int i = ancestors1.size() - 1;
        int j = ancestors2.size() - 1;

        while (i >= 0 && j > 0 && ancestors1.get(i) == ancestors2.get(j)) {
            i--;
            j--;
        }

        List<Node> cycle = new ArrayList<>();
        cycle.addAll(ancestors1.subList(0, i + 1));
        cycle.addAll(ancestors2.subList(j, ancestors2.size() - 1));
        return cycle;
    }

    private List<Node> findAncestors(Node node) {
        List<Node> nodeList = new ArrayList<>(List.of(node));
        while (node.getLabel(FATHER) != null) {
//            System.out.println("node is :" + node.getName());
//            System.out.println("father is :" + node.getLabel(FATHER));
//            System.out.println("father of father is :" + ((Node)node.getLabel(FATHER)).getLabel(FATHER));
            if (node == ((Node) node.getLabel(FATHER)).getLabel(FATHER)) {
                System.out.println("Node is same as father of father!!! Node is"+ node+ "and father is "+ node.getLabel(FATHER));
            }
            node = (Node) node.getLabel(FATHER);
            nodeList.add(node);
        }
        return nodeList;
    }

    private Node shrinkBlossom(List<Node> blossom) {
        // Uses UUID because name has to be unique
        Node superNode = graph.createNode("supernode" + UUID.randomUUID().toString());
        // Not sure wether or not the Node item has an equals or hash function implemented, so I'm just using the string as a key
        HashMap<String, Set<Node>> nodeMapping = new HashMap<>();
        for (Node node : blossom) {

            Iterator<Edge> edgeIterator = node.undirectedEdges();
            while (edgeIterator.hasNext()) {
                Node other = edgeIterator.next().getOtherEnd(node);
                if (blossom.contains(other)) continue;
                if (!nodeMapping.containsKey(node.getName())) nodeMapping.put(node.getName(), new HashSet<>());
                nodeMapping.get(node.getName()).add(other);

//                nodeMapping.putIfAbsent(node.getName(), new HashSet<>()).add(other);
                // Could make a HashSet for that since this will be O(1) in time instead of O(n)
                if (!blossom.contains(other.getLabel(FATHER))) {
                    other.setLabel(FATHER, superNode);
                }
            }
        }

        for (Node node : blossom) {
            if (nodeMapping.containsKey(node.getName())) {
                for (Node other : nodeMapping.get(node.getName())) {
                    try {
                        System.out.println("Trying to create node between: " + superNode + " and " + other);
                        graph.remove(getEdgeBetween(node, other));
                        graph.createEdge(superNode, other);
                    } catch (ConcurrentModificationException e) {
                        System.out.println("Edge already exists!");
                    }
                }

            }

        }
        superNode.setLabel(INITIAL_NODES, blossom);
        superNode.setLabel(INITIAL_MAPPING, nodeMapping);
        System.out.println(nodeMapping);
        return superNode;
    }

    public void expandBlossom(Node blossom) {
        usedSuperNode = true;
        List<Node> blossomNodes = (List<Node>) blossom.getLabel(INITIAL_NODES);
        HashMap<String, Set<Node>> nodeMapping = (HashMap<String, Set<Node>>) blossom.getLabel(INITIAL_MAPPING);
        Iterator<Edge> edgeIterator = blossom.undirectedEdges();
        // Cannot remove edges while iterating over them!
        List<Edge> toBeRemoved = new ArrayList<>();
        while (edgeIterator.hasNext()) {
            toBeRemoved.add(edgeIterator.next());
        }
        for (Edge edge : toBeRemoved) {
            graph.remove(edge);
        }


        for (Node node : blossomNodes) {
            // Otherwise NullPointerException is thrown, because key does not exist.
            if (!nodeMapping.containsKey(node.getName())) continue;

            for (Node other : nodeMapping.get(node.getName())) {
                if (other.getLabel(MATE) == blossom) {
                    other.setLabel(MATE, node);
                    node.setLabel(MATE, other);
                }
                System.out.println("Trying to recreate edge between :" + node + "and " + other);
                System.out.println("Current supernodes are :" + superNodes);
                System.out.println("Current blossom is :" + blossom);
                System.out.println("Nodeset :" + Arrays.toString(graph.nodeSet().toArray()));
                graph.createEdge(node, other);
            }
        }

        System.out.println("Nodeset before remove: " + Arrays.toString(graph.nodeSet().toArray()));
        // It seems to be possible to make edges to the blossom in the loop before
        graph.remove(blossom);
        System.out.println("Nodeset after remove: " + Arrays.toString(graph.nodeSet().toArray()));

        System.out.println("Successfully recreated supernode!");

    }

    private void replacePath(Node blossom, List<Node> path) {
        int index = path.indexOf(blossom);
        System.out.println(blossom);
        System.out.println("The index is: " + index);
        System.out.println(Arrays.toString(path.toArray()));
        // TODO path.size() or -1 ?

        // Definetly need to think about this!
        if (index == -1) index = path.size() - 1;
        List<Node> tempPath = path.subList(0, index);

        Node current = tempPath.get(tempPath.size() - 1);
        List<Node> blossomNodes = (List<Node>) blossom.getLabel(INITIAL_NODES);
        HashMap<String, Set<Node>> mapping = (HashMap<String, Set<Node>>) blossom.getLabel(INITIAL_MAPPING);

        for (Node[] edge : getEdges(blossomNodes, mapping)) {
            // TODO maybe continue instead of break?
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
                if (other != current && blossomNodes.contains(other)) {
                    current = other;
                    break;
                }
            }

        }
        tempPath.add(current);
        tempPath.addAll(path.subList(index + 1, path.size()));
        path = tempPath;


    }

    private List<Node[]> getEdges(List<Node> blossom, HashMap<String, Set<Node>> mapping) {
        List<Node[]> edges = new ArrayList<>();

        for (Node node : blossom) {
            if (mapping.containsKey(node.getName())) {
                for (Node other : mapping.get(node.getName())) {
                    edges.add(new Node[]{node, other});
                }
            }
        }

        return edges;
    }

    private Edge getEdgeBetween(Node n1, Node n2) {
        Iterator<Edge> edgeIterator = n1.undirectedEdges();
        while (edgeIterator.hasNext()) {
            Edge edge = edgeIterator.next();
            if (edge.getOtherEnd(n1) == n2) {
                return edge;
            }
        }
        System.out.println("No edge found for following nodes: " + n1 + " " + n2);
        return null;
    }

}
