import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by ASH on 2017/9/17.
 */

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int sz;
    private int current;

    public RandomizedQueue() {
        s = (Item[]) new Object[1];
        sz = 0;
        current = -1;
    }


    // 初始化是否为 null?
    private void increase() {
        int increasedCap = s.length * 2;
        Item[] increased = (Item[]) new Object[increasedCap];
        for (int i = 0; i < s.length; i++) {
            increased[i] = s[i];
        }
        s = increased;
    }

    private void decrease() {
        int decreaseCap = s.length / 2;
        Item[] decreased = (Item[]) new Object[decreaseCap];
        for (int i = sz - 1; i >= 0; i--) {
            decreased[i] = s[current];
            current -= 1;
            if (current < 0) {
                current = s.length - 1;
            }
        }
        current = sz - 1;
        s = decreased;
    }

    private boolean check() {
        if (sz < 0) {
            return false;
        }
        return true;
    }


    public boolean isEmpty() {
        return sz == 0;
    }


    public int size() {
        return sz;
    }


    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        sz += 1;
        if (sz == s.length) {
            increase();
            current += 1;
        } else if (current == s.length - 1) {
            current = 0;
        } else {
            current += 1;
        }
        s[current] = item;
        assert check();
    }


    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = s[current];
        s[current] = null;
        current -= 1;
        if (current < 0) {
            current = s.length - 1;
        }
        sz -= 1;
        if (sz == s.length / 4) {
            decrease();
        }
        assert check();
        return item;
    }


    // return a random item ( but not remove it )
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int i;
        if (current - sz + 1 >= 0) {
            i = StdRandom.uniform(current - sz + 1, current + 1);
        } else {
            i = StdRandom.uniform(s.length + current - sz + 1,
                    s.length + current + 1);
            if (i >= s.length) {
                i -= s.length;
            }
        }
        return s[i];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int i;
        Item[] sCopy;
        Item result;

        public RandomizedQueueIterator() {
            sCopy = (Item[]) new Object[sz];
            i = 0;
            for (int j = 0; j < s.length; j++) {
                if (s[j] != null && i < sz) {
                    sCopy[i] = s[j];
                    i += 1;
                }
            }
            i = 0;
            StdRandom.shuffle(sCopy, 0, sCopy.length);
        }

        @Override
        public boolean hasNext() {
            return i < sCopy.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (i < sCopy.length - 1 && sCopy[i] == null) {
                i += 1;
            }
            result = sCopy[i];
            i += 1;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }


    public static void main(String[] args) {
    }
}