package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>,Iterable<T>{
    private T[] array;
    private int length;
    private int begin;
    private int end;

    public ArrayDeque() {
        array=(T [])new Object[8];
        //总空间大小
        length=8;
        begin=0;
        end=0;
    }
    //help function size()
    @Override
    public int size() {
        return (end-begin+length)%length;
    }
    //help function length()
    public int length() {
        return length;
    }
    //help function begin()
    public int begin() {
        return begin;
    }
    //help function end()
    public int end() {
        return end;
    }
    //help function resize
    public void resize() {
        T[] a=(T[]) new Object[length*2];
        if (begin<=end) {
            System.arraycopy(array, 0, a, 0, length);
        } else {
            System.arraycopy(array, begin, a, begin, length-begin);
            System.arraycopy(array, 0, a, length, end);
            end=length+end;
        }
        length*=2;
        array=a;
        return;
    }
    //help function shrink
    public void shrink() {
        T[] a=(T[]) new Object[length/2];
        int l=size();
        if (begin<=end) {
            System.arraycopy(array, begin, a, 0, size());
            begin=0;
            end=l;
        } else {
            System.arraycopy(array, begin, a, 0, length-begin);
            System.arraycopy(array, 0, a, 0, end);
            begin=0;
            end=l;
        }
        length/=2;
        array=a;
        return;
    }

    @Override
    public void addFirst(T item) {
        //resize
        if (this.size()==(length-1)) {
            resize();
        }
        begin=(begin-1)<0?length-1:begin-1;
        array[begin]=item;
    }

    @Override
    public void addLast(T item) {
        //resize
        if (this.size()==(length-1)) {
            resize();
        }
        array[end]=item;
        end=(end+1)>=length?0:end+1;
    }

    @Override
    public void printDeque() {
        int i=begin;
        while (i!=end) {
            System.out.print(array[i]+" ");
            i=(i+1)==length?0:i+1;
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (size()==0) return null;
        if (size()<=(length/2-1) && length>8) shrink();
        T ret=array[begin];
        begin=(begin+1)>=length?0:begin+1;
        return ret;
    }

    @Override
    public T removeLast() {
        if (size()==0) return null;
        if (size()<=(length/2-1) && length>8) shrink();
        end=(end-1)<0?length-1:end-1;
        T ret=array[end];
        return ret;
    }   
    
    @Override
    public T get(int index) {
        if (index>size()) return null;
        return array[(begin+index)%length];
    }

   public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        private ArrayDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size();
        }

        public T next() {
            T item = get(wizPos);
            wizPos += 1;
            return item;
        }
    }
}
