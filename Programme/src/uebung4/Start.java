package uebung4;

import de.fhstralsund.vinets.control.GraphApplication;

public class Start {

  public static void main(String[] unused) {
    GraphApplication GA = GraphApplication.getInstance();
    GA.attachAlgorithm("uebung1.KnotenAnzahl");
    GA.attachAlgorithm("uebung4.BFS");
  }
}
