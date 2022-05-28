package loesung4;

import de.fhstralsund.vinets.structure.NetElement;
import java.util.Comparator;

/**
 * Compares the name of two different nodes or edges.
 * It is used to sort them by their name in a configuration dialog where
 * you can choose the source or target node for example.
 */
public class NameComparator implements Comparator<NetElement> {

  /**
   * * Compares the two nodes for order.
   *
   * @param n1 the first node to be compared.
   * @param n2 the second node to be compared.
   * @return a negative integer, zero, or a positive integer as the first
   *   argument is less than, equal to, or greater than the second.
   */
  @Override
  public int compare(NetElement n1, NetElement n2) {
    return n1.getName().compareTo(n2.getName());
  }
}
