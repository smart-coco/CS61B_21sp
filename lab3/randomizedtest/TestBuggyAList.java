package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void testThreeAddThreeRemove() {
    BuggyAList<Integer> buggy_test=new BuggyAList<>();
    AListNoResizing<Integer> NoRe_test=new AListNoResizing<>();

    buggy_test.addLast(4);
    NoRe_test.addLast(4);
    buggy_test.addLast(5);
    NoRe_test.addLast(5);
    buggy_test.addLast(6);
    NoRe_test.addLast(6);

    assertEquals(buggy_test.removeLast(), NoRe_test.removeLast());
    assertEquals(buggy_test.removeLast(), NoRe_test.removeLast());
    assertEquals(buggy_test.removeLast(), NoRe_test.removeLast());
  }
  @Test
  public void randomizedtest() {
    AListNoResizing<Integer> L = new AListNoResizing<>();
    BuggyAList<Integer> B = new BuggyAList<>();

    int N = 5000;
    for (int i = 0; i < N; i += 1) {
        int operationNumber = StdRandom.uniform(0, 3);
        if (operationNumber == 0) {
            // addLast
            int randVal = StdRandom.uniform(0, 100);
            L.addLast(randVal);
            B.addLast(randVal);
            //System.out.println("addLast(" + randVal + ")");
        } 
        if (operationNumber == 1 && L.size()!=0) {
            // getLast
            int L_n,B_n;
            L_n=L.getLast();
            B_n=B.getLast();
            assertEquals(L_n, B_n);
            //System.out.println("getLast");
        }
        if (operationNumber == 2 && L.size()!=0) {
            // removeLast
            int L_n,B_n;
            L_n=L.removeLast();
            B_n=B.removeLast();
            assertEquals(L_n, B_n);
            //System.out.println("removeLast");
        }
    }
  }
}
