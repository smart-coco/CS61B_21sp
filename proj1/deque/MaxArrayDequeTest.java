package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MaxArrayDequeTest{
    
    @Test
    public void threeTest() {
        Comparator<Integer> cmp=new Comparator<Integer>() {
            @Override
            public int compare(Integer a,Integer b) {
                if (a>b) return 1;
                else return -1;
            }
        };
        MaxArrayDeque<Integer> MaxArrayDeque=new MaxArrayDeque<Integer>(cmp);
        MaxArrayDeque.addLast(0);
        MaxArrayDeque.addFirst(1);
        MaxArrayDeque.removeLast();
        MaxArrayDeque.addLast(3);
        MaxArrayDeque.addLast(4);
        MaxArrayDeque.addFirst(5);
        MaxArrayDeque.addLast(6);
        MaxArrayDeque.removeLast();
        MaxArrayDeque.removeFirst();
        MaxArrayDeque.removeFirst();
        MaxArrayDeque.addLast(10);
        MaxArrayDeque.get(1);
        MaxArrayDeque.addLast(12);
        MaxArrayDeque.removeFirst();
        MaxArrayDeque.addLast(14);
        MaxArrayDeque.addLast(15);
        MaxArrayDeque.addLast(16);
        MaxArrayDeque.get(2);
        MaxArrayDeque.addFirst(18);
        MaxArrayDeque.addFirst(19);
        MaxArrayDeque.printDeque();
        MaxArrayDeque.removeLast();
        MaxArrayDeque.get(1);
        System.out.println(MaxArrayDeque.removeFirst()); 
    }    
}
