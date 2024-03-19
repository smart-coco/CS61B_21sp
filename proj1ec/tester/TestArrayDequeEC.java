package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {

    @Test
    public void testrandomTest() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<Integer>();
        student.get(2);
        System.out.println(student.get(2));
    }

    @Test
    public void randomTest() {

        // System.out.println("Make sure to uncomment the lines below (and delete this
        // print statement).");
        StudentArrayDeque<Integer> student = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<Integer>();

        String prompt = "";
        int N = 100;
        int boundary = 10000;
        for (int i = 0; i < N; i++) {
            int op = StdRandom.uniform(0, 4);
            if (op == 0) {
                int num = StdRandom.uniform(-boundary, boundary);
                // addFirst
                student.addFirst(num);
                solution.addFirst(num);
                prompt += "addFirst(" + num + ")\n";
                assertEquals("size() wrong", solution.size(), student.size());
            }
            if (op == 1) {
                int num = StdRandom.uniform(-boundary, boundary);
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
                Integer expect = solution.removeFirst();
                Integer actual = student.removeFirst();
                prompt += "removeFirst()\n";
                assertEquals(prompt, expect, actual);
            }
            if (op == 3) {
                assertEquals("size() wrong", solution.size(), student.size());
                if (solution.isEmpty()) {
                    continue;
                }
                // removeLast
                Integer expect = solution.removeLast();
                Integer actual = student.removeLast();
                prompt += "removeLast()\n";
                assertEquals(prompt, expect, actual);
            }
        }
    }
}
