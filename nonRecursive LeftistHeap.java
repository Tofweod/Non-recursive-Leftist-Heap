package chapter6.leftistHeap;

import java.nio.BufferUnderflowException;

/**
 * @author Tofweod
 */
public class NonRecursiveLeftistHeap<T extends Comparable<? super T>> {

    private Node<T> root;


    private static class Node<T>{

        private T value;
        private Node<T> left;
        private Node<T> right;


        private Node(T value, Node<T> left,Node<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        private Node(T value){
            this(value,null,null);
        }

        private Node(){}
    }


    public NonRecursiveLeftistHeap(){
        root = null;
    }

    public NonRecursiveLeftistHeap(T value){
        root = new Node<>(value);
    }


    public void merge(NonRecursiveLeftistHeap<T> rhs){
        if(this == rhs)
            return;

        root = merge(root,rhs.root);
        rhs.root = null;
    }

    private Node<T> merge(Node<T> h1,Node<T> h2){
        if(h1 == null)
            return h2;
        if(h2 == null)
            return h1;

        if(h1.value.compareTo(h2.value) > 0) {
            Node<T> tmp = h1;
            h1 = h2;
            h2 = tmp;
        }

        root = merge1(h1,h2);
        return checkSon();
    }


    public void insert(T value){
        if(root == null){
            root = new Node<>(value);
            return;
        }
        root = merge(new Node<>(value),root);
    }

    public T deleteMin(){

        if(isEmpty())
            throw new BufferUnderflowException();

        T minItem = root.value;

        root = merge(root.left,root.right);

        return minItem;
    }

    public boolean isEmpty(){
        return root == null;
    }

    // 先将所有右路径拼接在一起
    private Node<T> merge1(Node<T> h1,Node<T> h2){
        if(h1.right == null){
            h1.right= h2;
            return h1;
        }

        Node<T> p = h1;
        Node<T> n2 = h2;
        while(n2 != null){ // 结束时为n2 == null，而不是p
            if(p.right == null){
                p.right = n2;
                break;
            }

            while( p.right.value.compareTo(n2.value) <0 ){
                p = p.right;
                if(p.right == null)
                    break;
            }
            Node<T> tmp = p.right;
            p.right = n2;
            n2 = tmp;

            p = p.right;
        }

        return h1;
    }


    private void swapArrayItem(Node<T> h){
        Node<T> tmp = h.left;
        h.left = h.right;
        h.right = tmp;
    }


    // 检查右路径上的点是否违背了堆序性
    private Node<T> checkSon(){
        int size = npl(root)+1;
        Node<T>[] nodes =  new Node[size];
        Node<T> p = root;
        for (int i = 0; p != null; p = p.right,i++) {
            nodes[i] = p;
        }

        for (int i = size-1; i >= 0 ; i--) {
            int leftLength = npl(nodes[i].left);
            int rightLength = npl(nodes[i].right);
            if(leftLength < rightLength)
                swapArrayItem(nodes[i]);
        }


        p = root;
        for (int i = 0; p != null; p = p.right,i++) {
            p = nodes[i];
        }

        return root;
    }

    private int npl(Node<T> h){
        if(h == null)
            return -1;

        int count = 0;
        Node<T> p = h.right;
        while(p != null){
            p = p.right;
            count++;
        }

        return count;
    }

    public static void main( String [ ] args ) {
        int numItems = 100;
        NonRecursiveLeftistHeap<Integer> h  = new NonRecursiveLeftistHeap<>( );
        NonRecursiveLeftistHeap<Integer> h1 = new NonRecursiveLeftistHeap<>( );
        int i = 37;

        for( i = 37; i != 0; i = ( i + 37 ) % numItems )
            if( i % 2 == 0 )
                h1.insert( i );
            else
                h.insert( i );

        h.merge( h1 );

        for( i = 1; i < numItems; i++ )
            if(i != h.deleteMin())
                System.out.println("Oops " + i);
    }
}