package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<String> lld1 = new ArrayDeque2<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		//lld1.removeFirst();
        lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque2<String> lld1 = new ArrayDeque2<String>();
        ArrayDeque2<Double> lld2 = new ArrayDeque2<Double>();
        ArrayDeque2<Boolean> lld3 = new ArrayDeque2<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }
    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void tenTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();

        for (int i=0;i<10;i++) {
            lld1.addFirst(i);
        }
        lld1.printDeque();
        System.out.println(lld1.size());
        System.out.println(lld1.removeLast());
        System.out.println(lld1.removeFirst());
        lld1.printDeque();
        System.out.println(lld1.size());
    }

    @Test
    public void emptyTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();

        int N=10000;
        int l=0;
        for (int i=0;i<N;i++) {
            int op=StdRandom.uniform(0, 4);
            if (op==0) {
                //addFirst
                lld1.addFirst(1);
                l+=1;
                //System.out.println("addFirst");
                //System.out.println(lld1.length());
                //System.out.println(l);
                //System.out.println(lld1.size());
                //System.out.println(lld1.begin());
                //System.out.println(lld1.end());
                assertEquals("size() wrong", l, lld1.size());
            }
            if (op==1) {
                //addLast
                lld1.addLast(1);
                l+=1;
                assertEquals("size() wrong", l, lld1.size());
            }
            if (op==2 && lld1.size()!=0) {
                //removefirst
                lld1.removeFirst();
                l-=1;
                assertEquals("size() wrong", l, lld1.size());
            }
            if (op==3 && lld1.size()!=0) {
                //removeLast
                lld1.removeLast();
                l-=1;
                assertEquals("size() wrong", l, lld1.size());
            }
        }
    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void getindextest() {

        //System.out.println("Make sure to uncomment the lines below (and delete this print statement).");
        ArrayDeque2<Integer> lld1 = new ArrayDeque2<Integer>();
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addLast(4);
        lld1.addLast(5);
        lld1.printDeque();
        for (int i=0;i<5;i++) {
            assertEquals("Should have the same value",i+1 ,(int)lld1.get(i));
        }
    }
    
}
