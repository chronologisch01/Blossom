package loesung1;

import de.fhstralsund.vinets.control.GraphApplication;

public class Start {

  public static void main(String[] unused) {
    GraphApplication GA = GraphApplication.getInstance();
    GA.attachAlgorithm("uebung1.KnotenAnzahl");
    GA.attachAlgorithm("loesung1.Degree");
    GA.attachAlgorithm("loesung1.Matrix");
  }
}
