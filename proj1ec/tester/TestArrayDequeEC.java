package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {

    @Test
    public void testrandomTest() {
        Integer a, b;

        a = 10;
        b = 20;
        System.out.println(a);
        assertEquals(a, b);
    }

    @Test
    public void randomTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this
        // print statement).");
        StudentArrayDeque<Integer> student = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<Integer>();

        String prompt = "";
        int N = 10000;
        int boundary = 10000;
        for (int i = 0; i < N; i++) {
            int op = StdRandom.uniform(0, 4);
            if (op == 0) {
                Integer num = StdRandom.uniform(-boundary, boundary);
                // addFirst
                student.addFirst(num);
                solution.addFirst(num);
                prompt += "addFirst(" + num + ")\n";
                assertEquals("size() wrong", solution.size(), student.size());
            }
            if (op == 1) {
                Integer num = StdRandom.uniform(-boundary, boundary);
                // addLast
                student.addLast(num);
                solution.addLast(num);
                prompt += "addLast(" + num + ")\n";
                assertEquals("size() wrong", solution.size(), student.size());
            }

            if (op == 2) {
                assertEquals("size() wrong", solution.size(), student.size());
                if (solution.isEmpty()) {
                    continue;
                }
                // removefirst
                int expect = solution.removeFirst();
                int actual = student.removeFirst();
                prompt += "removeFirst()\n";
                assertEquals(prompt, expect, actual);
            }
            if (op == 3) {
                assertEquals("size() wrong", solution.size(), student.size());
                if (solution.isEmpty()) {
                    continue;
                }
                // removeLast
                int expect = solution.removeLast();
                int actual = student.removeLast();
                prompt += "removeLast()\n";
                assertEquals(prompt, expect, actual);
            }
        }
    }
}
