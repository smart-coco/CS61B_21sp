package deque;

public class ArrayDeque<T> {
    private T[] array;
    private int size;
    private int begin;
    private int end;

    public ArrayDeque() {
        array=(T [])new Object[8];
        size=8;
        begin=0;
        end=0;
    }
    //help function length()
    public int length() {
        return (end-begin+size)%size;
    }
    //help function resize
    public void resize() {
        T[] a=(T[]) new Object[size*2];
        if (begin<=end) {
            System.arraycopy(array, 0, a, 0, size);
        } else {
            System.arraycopy(array, begin, a, begin, size-begin);
            System.arraycopy(array, 0, a, size, end-1);
            end=size+end;
        }
        size*=2;
        array=a;
        return;
    }
    //help function shrink
    public void shrink() {
        T[] a=(T[]) new Object[size/2];
        int l=length();
        if (begin<=end) {
            System.arraycopy(array, begin, a, 0, length());
            begin=0;
            end=l;
        } else {
            System.arraycopy(array, begin, a, 0, size-begin);
            System.arraycopy(array, 0, a, 0, end);
            begin=0;
            end=l;
        }
        size/=2;
        array=a;
        return;
    }

    public void addFirst(T item) {
        //resize
        if (this.length()==(size-1)) {
            resize();
        }
        begin=(begin-1)<=0?size-1:begin-1;
        array[begin]=item;
    }

    public void addLast(T item) {
        //resize
        if (this.length()==(size-1)) {
            resize();
        }
        array[end]=item;
        end=(end+1)>=size?0:end+1;
    }

    public boolean isEmpty() {
        if (length()==0) return true;
        else return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int i=begin;
        while (i!=end) {
            System.out.print(array[i]+" ");
            i=(i+1)==size?0:i+1;
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if (length()==0) return null;
        if (length()<=(size/2)) shrink();
        T ret=array[begin];
        begin=(begin+1)>=size?0:begin+1;
        return ret;
    }

    public T removeLast() {
        if (length()==0) return null;
        if (length()<=(size/2)) shrink();
        end=(end-1)<0?size-1:size-1;
        T ret=array[end];
        return ret;
    }   
    
    public T get(int index) {
        if (index>length()) return null;
        return array[(begin+index)%size];
    }
}
