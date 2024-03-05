package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MaxArrayDequeTest {
    
    @Test
    public void threeTest() {
        Comparator<Integer> cmp=new Comparator<Integer>() {
            @Override
            public int compare(Integer a,Integer b) {
                if (a>b) return 1;
                else return -1;
            }
        };
        MaxArrayDeque<Integer> test=new MaxArrayDeque<Integer>(cmp);
        test.addFirst(3);
        test.addFirst(2);
        test.addFirst(5);
        test.addFirst(10);
        test.addFirst(1);
        test.removeLast();
        System.out.println(test.max());
    }    
}
