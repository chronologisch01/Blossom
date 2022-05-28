package loesung2;

import de.fhstralsund.vinets.algorithm.Algorithm;
import de.fhstralsund.vinets.algorithm.Parameter;
import de.fhstralsund.vinets.control.GraphApplication;
import de.fhstralsund.vinets.structure.Edge;

/**
 * Warshall-Algorithmus mit Adjazenzmatrix.
 * Der Graph sollte kreisfrei sein - Schlingen koennen z.Z. noch nicht
 * gezeichnet werden. Neue Kanten sind immer gerichtet.
 */
public class WarshallMatrixNeu implements Algorithm {

  @Override
  public String getHint() {
    return "Berechnet die transitive Huelle.";
  }

  @Override
  public String getName() {
    return "WarshallMatrixNeu";
  }

  @Override
  public boolean accept(Parameter par) {
    return par.getGraph().isDirected();
  }

  @Override
  public Parameter execute(Parameter par) {
    MatrixDecorator G = new MatrixDecorator(par.getGraph());
    String resultMessage = "Adjazenzmatrix des Inputgraphs: " + G.getMatrixAsString();
    int size = G.countNodes();

    int anz = 0;
    for (int k = 0; k < size; k++) {
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (!G.isEdgeTrue(i, j)
                  && G.isEdgeTrue(i, k) && G.isEdgeTrue(k, j)) {
            anz++;
            Edge e = G.setEdgeTrue(i, j);
            // Geometrie und Grafik der Kante erzeugen, dann faerben:
            GraphApplication.getInstance().getDeployer().deployLink(e);
            e.getRepresentation().setColor(java.awt.Color.BLUE);
          }
        }
      }
    }
    resultMessage = resultMessage + anz +
            " transitive Kanten zugefügt. Neue Adjazenzmatrix: "+ G.getMatrixAsString();
    par.setMessage(resultMessage);
    return par;
  }
}
