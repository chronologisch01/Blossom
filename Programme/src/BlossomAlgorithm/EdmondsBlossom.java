package BlossomAlgorithm;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Configurable;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.graphics.NodeRepresentation;
import de.fhstralsund.vinets.structure.Edge;
import de.fhstralsund.vinets.structure.Graph;
import de.fhstralsund.vinets.structure.Node;
import uebung4.Labels;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;

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
        node.setIntLabel(Labels.DISTANCE, 0);

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
            System.out.println(node.getIntLabel(Labels.DISTANCE));
            if(node.getIntLabel(Labels.DISTANCE)%2==0){
                nodeRepresentation.setColor(Color.RED);
            } else {
                nodeRepresentation.setColor(Color.BLUE);
            }
        }
    }

    private void BFS(){
        while(!queue.isEmpty()){
            System.out.println("test");
            Node node = queue.poll();

            Iterator<Edge> edgeIterator = node.undirectedEdges();
            while (edgeIterator.hasNext()){
                Edge edge = edgeIterator.next();
                Node other = edge.getOtherEnd(node);

                if(other.getBooleanLabel(Labels.VISITED)) continue;

                other.setIntLabel(Labels.DISTANCE, node.getIntLabel(Labels.DISTANCE)+1);
                if (other.getIntLabel(Labels.DISTANCE) % 2 == 0) {
                    other.setLabel(Labels.COLOR, Labels.WHITE);
                } else {
                    other.setLabel(Labels.COLOR, Labels.BLACK);
                }
                other.setBooleanLabel(Labels.VISITED, true);



                queue.add(other);
            }
        }
    }




}
