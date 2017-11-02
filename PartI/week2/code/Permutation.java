import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by ASH on 2017/9/17.
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int count = 1;
        if (k < 0) {
            throw new IndexOutOfBoundsException();
        }
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        if (k > randomizedQueue.size()) {
            throw new IndexOutOfBoundsException();
        }
        for (String item :
                randomizedQueue) {
            if (count <= k) {
                StdOut.println(item);
                count += 1;
            }else {
                break;
            }
        }
    }
}
