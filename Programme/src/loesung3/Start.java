package loesung3;

import de.fhstralsund.vinets.control.GraphApplication;

public class Start {

  public static void main(String[] unused) {
    GraphApplication GA = GraphApplication.getInstance();
    GA.attachAlgorithm("uebung1.KnotenAnzahl");
    GA.attachAlgorithm("loesung1.Degree");
    GA.attachAlgorithm("loesung1.Matrix");
    GA.attachAlgorithm("loesung2.Warshall");
    GA.attachAlgorithm("loesung2.WarshallMatrix");
    GA.attachAlgorithm("loesung2.WarshallMatrixNeu");
    GA.attachAlgorithm("loesung3.InDegreeTopSort");
    GA.attachAlgorithm("loesung3.SortedMatrix");
  }
}
