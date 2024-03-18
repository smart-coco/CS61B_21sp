package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmp;
    public MaxArrayDeque(Comparator<T> c) {
        cmp=c;
    }
    public T max() {
        if (isEmpty()) return null;
        T maxItem=get(0);
        for (int i=0;i<size();i++) {
            if (cmp.compare(get(i), maxItem)>0) {
                maxItem=get(i);
            }
        }
        //for (T i : this) {
            //if (cmp.compare(i, maxItem) > 0) {
                //maxItem = i;
            //}
        //}
        return maxItem;
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) return null;
        T maxItem=get(0);
        for (int i=0;i<size();i++) {
            if (c.compare(get(i), maxItem)>0) {
                maxItem=get(i);
            }
        }
        return maxItem;
    }
}
