package BlossomAlgorithm;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Configurable;
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
    private Queue<Node> queue;

    @Override
    public String getName(){return "Blossom";}

    @Override
    public String getHint(){
        return "Noch am Tryen";
    }

    @Override
    public boolean accept(Parameter p){

        return p.getGraph().isUndirected();
    }

    @Override
    public Parameter execute(Parameter input){

        graph=input.getGraph();


        queue=new ArrayDeque<>();

        Node node = (Node) graph.nodes().next();
        node.setIntLabel(DISTANCE, 0);

        queue.add(node);

        BFS();

        changeGeometry();

        return new Parameter(graph, "finished!");
    }




    public void changeGeometry(){
        Iterator<Node> nodeIterator = graph.nodes();
        while(nodeIterator.hasNext()){
            Node node = nodeIterator.next();
            NodeRepresentation nodeRepresentation = (NodeRepresentation) node.getRepresentation();
            nodeRepresentation.setFilled(true);
            System.out.println(node.getIntLabel(DISTANCE));
            if(node.getIntLabel(DISTANCE)%2==0){
                nodeRepresentation.setColor(Color.RED);
            } else {
                nodeRepresentation.setColor(Color.BLUE);
            }
        }
    }




    private void invertPath(List<Node> nodeList) {
        if (nodeList.size() % 2 == 0) System.out.println("Size assertion Failed!");
        for (int i = 0; i < nodeList.size() - 1; i += 2) {
            Node firstNode = nodeList.get(i);
            Node secondNode = nodeList.get(i + 1);
            firstNode.setLabel(MATE, secondNode);
            secondNode.setLabel(MATE, firstNode);
            System.out.println("Path constructed between: " + firstNode.getName() + " and " + secondNode.getName());
        }
    }

    private List<Node> constructAugmentingPath(Node node){
        List<Node> path = new ArrayList<>(List.of(node));
        node = (Node) node.getLabel(FATHER);
        path.add(node);

        while(node.getLabel(MATE)!=null){
            System.out.println(node.getLabel(MATE));
            node = (Node) node.getLabel(FATHER);
            path.add(node);
        }

        // TODO something missing here?

        return path;

    }


    private void clearNodes(){
        Iterator<Node> nodes = graph.nodes();
        while(nodes.hasNext()){
            Node node = nodes.next();
            node.setLabel(FATHER, null);
            node.setBooleanLabel(VISITED, false);
        }
    }

    private List<Node> findAugmentingPath(Node root){
        clearNodes();
        Queue<Node> queue = new ArrayDeque<>(List.of(root));

        while(!queue.isEmpty()){
            Node currentNode = queue.poll();
            currentNode.setBooleanLabel(VISITED, true);
            Iterator<Edge> edges = currentNode.undirectedEdges();
            while(edges.hasNext()){
                Node node = edges.next().getOtherEnd(currentNode);

                if(node == (Node) currentNode.getLabel(FATHER)){
                    continue;
                }

                if(node.getBooleanLabel(VISITED)){
                    System.out.println("Already visited!");
                    continue;
                }
                System.out.println("findAugmenting (120): "+ node.getLabel(MATE));
                if(node.getLabel(MATE)!=null){
                    node.setLabel(FATHER, currentNode);
                    return constructAugmentingPath(node);
                }

                if(node.getLabel(MATE)!=currentNode){
                    node.setBooleanLabel(VISITED, true);
                    ((Node)node.getLabel(MATE)).setLabel(VISITED, true);
                    node.setLabel(FATHER, currentNode);
                    ((Node)node.getLabel(FATHER)).setLabel(FATHER, node);
                    queue.add((Node)node.getLabel(MATE));
                }


            }

        }
        System.out.println("findAugmentingPath: none found!");
        return null;
    }

    private void findMaximumMatching(List<Node> nodeList){
        while(!nodeList.isEmpty()){
            for(Node node : nodeList){
                List<Node> path = findAugmentingPath(node);
                if(node==null){
                    System.out.println("findMaximumMatching no path found!");
                    continue;
                }
                invertPath(path);
                nodeList.remove(path.get(0));
                nodeList.remove(path.get(path.size()-1));
            }
        }

        System.out.println("matching foudn !");
    }






    private void BFS(){
        while(!queue.isEmpty()){
            System.out.println("test");
            Node node = queue.poll();

            Iterator<Edge> edgeIterator = node.undirectedEdges();
            while (edgeIterator.hasNext()){
                Edge edge = edgeIterator.next();
                Node other = edge.getOtherEnd(node);

                if(other.getBooleanLabel(VISITED)) continue;

                other.setIntLabel(DISTANCE, node.getIntLabel(DISTANCE)+1);
                if (other.getIntLabel(DISTANCE) % 2 == 0) {
                    other.setLabel(COLOR, WHITE);
                } else {
                    other.setLabel(COLOR, BLACK);
                }
                other.setBooleanLabel(VISITED, true);



                queue.add(other);
            }
        }
    }




}
