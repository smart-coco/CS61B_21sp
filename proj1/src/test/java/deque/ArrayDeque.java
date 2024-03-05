package deque;

public class ArrayDeque<T> {
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
    //help function length()
    public int size() {
        return (end-begin+length)%length;
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

    public void addFirst(T item) {
        //resize
        if (this.size()==(length-1)) {
            resize();
        }
        begin=(begin-1)<0?length-1:begin-1;
        array[begin]=item;
    }

    public void addLast(T item) {
        //resize
        if (this.size()==(length-1)) {
            resize();
        }
        array[end]=item;
        end=(end+1)>=length?0:end+1;
    }

    public boolean isEmpty() {
        if (size()==0) return true;
        else return false;
    }

    public void printDeque() {
        int i=begin;
        while (i!=end) {
            System.out.print(array[i]+" ");
            i=(i+1)==length?0:i+1;
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if (size()==0) return null;
        if (size()<=(length/2)) shrink();
        T ret=array[begin];
        begin=(begin+1)>=length?0:begin+1;
        return ret;
    }

    public T removeLast() {
        if (size()==0) return null;
        if (size()<=(length/2)) shrink();
        end=(end-1)<0?length-1:end-1;
        T ret=array[end];
        return ret;
    }   
    
    public T get(int index) {
        if (index>size()) return null;
        return array[(begin+index)%length];
    }
}
