/**
 * @Author ASH975
 */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.HashMap;


public class WordNet {
    private final Digraph wordnet;
    private final HashMap<Integer, String> synetsSet;
    private final HashMap<String, Queue<Integer>> synetsSetR;

    public WordNet(String synets, String hypernyms) {
        if (synets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        this.synetsSet = new HashMap<>();
        this.synetsSetR = new HashMap<>();
        buildSets(synets);
        this.wordnet = new Digraph(synetsSet.size());
        buildNets(hypernyms);
        checkDAG(this.wordnet);
    }

    private void buildSets(final String synets) {
        In synetsIn = new In(synets);
        while (!synetsIn.isEmpty()) {
            String[] record = synetsIn.readLine().split(",");
            this.synetsSet.put(Integer.valueOf(record[0]), record[1]);
            for (String s : record[1].split(" ")) {
                // word conflict case
                if (this.synetsSetR.containsKey(s)) {
                    this.synetsSetR.get(s).enqueue(Integer.valueOf(record[0]));
                } else {
                    Queue<Integer> newItem = new Queue<>();
                    newItem.enqueue(Integer.valueOf(record[0]));
                    this.synetsSetR.put(s, newItem);
                }
            }
        }
    }

    private void buildNets(final String hypernyms) {
        In hypernymsIn = new In(hypernyms);
        int synetsID;
        while (!hypernymsIn.isEmpty()) {
            String[] record = hypernymsIn.readLine().split(",");
            synetsID = Integer.parseInt(record[0]);
            for (int i = 1; i < record.length; i++) {
                this.wordnet.addEdge(synetsID, Integer.parseInt(record[i]));
            }
        }
    }

    private void checkDAG(Digraph digraph) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) throw new IllegalArgumentException();
    }

    public Iterable<String> nouns() {
        return this.synetsSetR.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return this.synetsSetR.containsKey(word);
    }

    private class Node implements Comparable<Node> {
        public int length;
        public int ancestor;

        public Node(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }

        @Override
        public int compareTo(Node that) {
            if (this.length > that.length) return 1;
            if (this.length < that.length) return -1;
            return 0;
        }
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Node ancestor = findSAP(nounA, nounB);
        if (ancestor == null) {
            return -1;
        }
        return ancestor.length;
    }

    private Node findSAP(String nounA, String nounB) {
//        MinPQ<Node> candidates = new MinPQ<>();
        Node ancestor = null;
        BreadthFirstDirectedPaths pathToA = new BreadthFirstDirectedPaths(this.wordnet, this.synetsSetR.get(nounA));
        BreadthFirstDirectedPaths pathToB = new BreadthFirstDirectedPaths(this.wordnet, this.synetsSetR.get(nounB));
        for (int i = 0; i < this.wordnet.V(); i++) {
            if (pathToA.hasPathTo(i) && pathToB.hasPathTo(i)) {
//                candidates.insert(new Node(pathToA.distTo(i) + pathToB.distTo(i), i));
                if (ancestor == null) {
                    ancestor = new Node(pathToA.distTo(i) + pathToB.distTo(i), i);
                } else {
                    ancestor = checkMin(ancestor, new Node(pathToA.distTo(i) + pathToB.distTo(i), i));
                }
            }
        }
//        if (candidates.isEmpty()) {
//            return null;
//        }
//        return candidates.min();
        return ancestor;
    }

    private Node checkMin(Node current, Node that) {
        if (current.compareTo(that) > 0) {
            return that;
        } else {
            return current;
        }
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Node ancestor = findSAP(nounA, nounB);
        if (ancestor == null) {
            return null;
        }
        return this.synetsSet.get(ancestor.ancestor);
    }

    public static void main(String[] args) {
//        String[] testArgs = {
//                "./test_data/synsets3.txt",
//                "./test_data/hypernyms3InvalidTwoRoots.txt",
//        };
//        WordNet wordNet = new WordNet(testArgs[0], testArgs[1]);
//        System.out.println(wordNet.synetsSet.size());
//        while (!StdIn.isEmpty()) {
//            String v1 = StdIn.readString();
//            String v2 = StdIn.readString();
//            System.out.println(wordNet.isNoun(v1) + " " + wordNet.isNoun(v2));
//            int distance = wordNet.distance(v1, v2);
//            String ancestor = wordNet.sap(v1, v2);
//            StdOut.printf("distance = %d, ancestor = %s\n", distance, ancestor);
//        }
    }
}
