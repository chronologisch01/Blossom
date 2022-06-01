package uebung4;

/**
 * Contains constants to be used as NetElement labels in graph algorithms.
 */
public enum Labels {
  // keys for labels:
  COLOR, BFSNO, DEGREE, DFSNO, DFS_F, DISTANCE, FATHER, LOWPOINT, TYPE, VISITED, MATE,
  // values for labels:
  WHITE, GRAY, BLACK, TREE, BACK, FORWARD, CROSS
}

/* alte Loesung - ohne Aufzaehlungstyp:
public interface Labels {
   // keys:
   Object COLOR = new Object();
   Object BFSNO = new Object();
   Object DEGREE = new Object();
   Object DFSNO = new Object();
   Object DFS_F = new Object();
   Object DISTANCE = new Object();
   Object FATHER = new Object();
   Object LOWPOINT = new Object();
   Object TYPE = new Object();
   String ROOT = "ROOT";
  // values:
   Object WHITE = new Object(); // unvisited node
   Object GRAY = new Object(); // visited but not done
   Object BLACK = new Object(); // vertex done

   Object TREE = new Object();  // tree edge
   Object BACK = new Object();  // back edge
   Object FORWARD = new Object();  // forward edge
   Object CROSS = new Object();  // cross edge
}
*/