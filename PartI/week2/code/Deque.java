/**
 * Created by ASH on 2017/9/17.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int sz;
    private final Node first;
    private final Node last;

    private class Node {
        private final Item item;
        private Node next;
        private Node prior;

        public Node(Item item) {
            this.item = item;
            this.prior = null;
            this.next = null;
        }

        public Node(Item item, Node prior, Node next) {
            this.item = item;
            this.prior = prior;
            this.next = next;
        }
    }

    // construct an empty deque
    public Deque() {
        first = new Node(null);
        last = new Node(null);

        // sentinel nodes
        first.next = last;
        last.prior = first;
        sz = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first.next == last;
    }

    // return the number of items on the deque
    public int size() {
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item, first, first.next);
        first.next.prior = node;
        first.next = node;
        sz += 1;
        assert check();
    }

    // add the item to the last
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item, last.prior, last);
        last.prior.next = node;
        last.prior = node;
        sz += 1;
        assert check();
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.next.item;
        if (sz >= 2) {
            Node oldnext = first.next.next;
            first.next.next = null;
            first.next.prior = null;
            first.next = oldnext;
            oldnext.prior = first;
        } else {
            first.next = last;
            last.prior = first;
        }
        sz -= 1;
        assert check();
        return item;
    }

    // remove and return the item from the last
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.prior.item;
        if (sz >= 2) {
            Node oldprior = last.prior.prior;
            last.prior.prior = null;
            last.prior.next = null;
            last.prior = oldprior;
            oldprior.next = last;
        } else {
            last.prior = first;
            first.next = last;
        }
        sz -= 1;
        assert check();
        return item;
    }

    // return an iterator over items in order from front to last
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first.next;

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // check internal invariants
    private boolean check() {

        // check a few properties of instance variable 'first'
        if (sz < 0) {
            return false;
        }
        if (sz == 0 && first.next != last && last.prior != first) {
            return false;
        } else if (sz == 1) {
            if (first.next == null) return false;
            if (first.next.next != last) return false;
            if (last.prior == null) return false;
            if (last.prior.prior != first) return false;
        } else if(sz >= 2){
            if (first.next == null) return false;
            if (first.next.next == null) return false;
            if (last.prior == null) return false;
            if (last.prior.prior == null) return false;
        }

        // check internal consistency of instance variable n
//        int numberOfNodes = 0;
//        for (Node x = first; x != null && numberOfNodes <= sz; x = x.next) {
//            numberOfNodes++;
//        }
//        if (numberOfNodes != sz) return false;

        return true;
    }

    public static void main(String[] args) {

    }

}
