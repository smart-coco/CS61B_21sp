package deque;

public class LinkedListDeque<T> implements Deque<T>{
    public class Node {
        public T item;
        public Node next;
        public Node prev;
        public Node(T i,Node m,Node n) {
            item=i;
            prev=m;
            next=n;
        }
        public Node(Node m,Node n) {
            prev=m;
            next=n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel=new Node(null,null);
        sentinel.prev=sentinel;
        sentinel.next=sentinel;
        size=0;
    }

    @Override
    public void addFirst(T item) {
        Node p=sentinel.next;
        sentinel.next=new Node(item,sentinel,p);
        p.prev=sentinel.next;
        size+=1;
    }

    @Override
    public void addLast(T item) {
        Node p=sentinel.prev;
        sentinel.prev=new Node(item,p,sentinel);
        p.next=sentinel.prev;
        size+=1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p=sentinel.next;
        while (p!=sentinel) {
            System.out.print(p.item+" ");
            p=p.next;
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (size==0) return null;
        size-=1;
        Node p=sentinel.next;
        sentinel.next=p.next;
        p.next.prev=sentinel;
        return p.item;
    }

    @Override
    public T removeLast() {
        if (size==0) return null;
        size-=1;
        Node p=sentinel.prev;
        sentinel.prev=p.prev;
        p.prev.next=sentinel;
        return p.item;
    }   
    
    @Override
    public T get(int index) {
        Node p=sentinel.next;
        while (index>0 && p!=sentinel) {
            index-=1;
            p=p.next;
        }
        if (index==0 && p!=sentinel) {
            return p.item;
        }
        return null;
    }

    private T getRecursiveHelp(Node p,int index) {
        if (index==0) return p.item;
        else return getRecursiveHelp(p.next, index-1);
    }
    public T getRecursive(int index) {
        if (index<0 || index>size) return null;
        return getRecursiveHelp(sentinel.next, index);
    }
}
