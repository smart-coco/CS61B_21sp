package deque;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    public MaxArrayDeque(Comparator<T> c) {

    }
    public T max() {
        if (size()==0) return null;
        else {
            return 1;
        }
    }
}
