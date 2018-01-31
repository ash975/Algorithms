import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    private final Digraph graph;

    public SAP(Digraph G) {
        if (G == null){
            throw new IllegalArgumentException();
        }
        this.graph = new Digraph(G);
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

    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V()){
            throw new IllegalArgumentException();
        }
        Node sap = findSAP(v, w);
        if (sap == null) {
            return -1;
        }
        return sap.length;
    }

    public int ancestor(int v, int w) {
        checkRange(v, w);
        Node sap = findSAP(v, w);
        if (sap == null) {
            return -1;
        }
        return sap.ancestor;
    }

    private Node findSAP(int v1, int v2) {
//        MinPQ<Node> candidates = new MinPQ<>();
        Node ancersor = null;
        Queue<Integer> q = new Queue<Integer>();

        boolean[] v1marked = new boolean[graph.V()];
        int[] v1distTo = new int[graph.V()];
        v1marked[v1] = true;
        v1distTo[v1] = 0;
        q.enqueue(v1);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : graph.adj(v)) {
                if (!v1marked[w]) {
                    v1distTo[w] = v1distTo[v] + 1;
                    v1marked[w] = true;
                    q.enqueue(w);
                }
            }
        }

        boolean[] v2marked = new boolean[graph.V()];
        int[] v2distTo = new int[graph.V()];
        v2marked[v2] = true;
        v2distTo[v2] = 0;
        q.enqueue(v2);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : graph.adj(v)) {
                if (!v2marked[w]) {
                    v2distTo[w] = v2distTo[v] + 1;
                    v2marked[w] = true;
                    q.enqueue(w);
                }
            }
        }

        for (int i = 0; i < graph.V(); i++) {
            if (v1marked[i] && v2marked[i]) {
//                candidates.insert(new Node(v1distTo[i] + v2distTo[i], i));
                if (ancersor == null) {
                    ancersor = new Node(v1distTo[i] + v2distTo[i], i);
                } else {
                    ancersor = checkMin(ancersor, new Node(v1distTo[i] + v2distTo[i], i));
                }
            }
        }
//        if (candidates.isEmpty()) {
//            return null;
//        }
//        return candidates.min();
        return ancersor;
    }

    private Node checkMin(final Node current, final Node that) {
        if (current.compareTo(that) > 0) {
            return that;
        } else {
            return current;
        }
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        Node sap = findSAP(v, w);
        if (sap == null) {
            return -1;
        }
        return sap.length;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        Node sap = findSAP(v, w);
        if (sap == null) {
            return -1;
        }
        return sap.ancestor;
    }

    private Node findSAP(Iterable<Integer> v1, Iterable<Integer> v2) {
//        MinPQ<Node> candidates = new MinPQ<>();
        Node ancersor = null;
        Queue<Integer> q = new Queue<Integer>();

        boolean[] v1marked = new boolean[graph.V()];
        int[] v1distTo = new int[graph.V()];
        for (int v : v1) {
            checkRange(v);
            v1marked[v] = true;
            v1distTo[v] = 0;
            q.enqueue(v);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : graph.adj(v)) {
                if (!v1marked[w]) {
                    v1distTo[w] = v1distTo[v] + 1;
                    v1marked[w] = true;
                    q.enqueue(w);
                }
            }
        }

        boolean[] v2marked = new boolean[graph.V()];
        int[] v2distTo = new int[graph.V()];
        for (int v : v2) {
            checkRange(v);
            v2marked[v] = true;
            v2distTo[v] = 0;
            q.enqueue(v);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : graph.adj(v)) {
                if (!v2marked[w]) {
                    v2distTo[w] = v2distTo[v] + 1;
                    v2marked[w] = true;
                    q.enqueue(w);
                }
            }
        }

        for (int i = 0; i < graph.V(); i++) {
            if (v1marked[i] && v2marked[i]) {
//                candidates.insert(new Node(v1distTo[i] + v2distTo[i], i));
                if (ancersor == null) {
                    ancersor = new Node(v1distTo[i] + v2distTo[i], i);
                } else {
                    ancersor = checkMin(ancersor, new Node(v1distTo[i] + v2distTo[i], i));
                }
            }
        }
//        if (candidates.isEmpty()) {
//            return null;
//        }
//        return candidates.min();
        return ancersor;
    }

    private void checkRange(int v){
        if (v < 0 || v >= this.graph.V()) throw new IllegalArgumentException();
    }

    private void checkRange(int v, int w){
        if (v < 0 || w < 0|| v >= this.graph.V() || w >= this.graph.V()) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
//        In in = new In("/home/ash975/IdeaProjects/Algorithms/PartII/src/test_data/digraph3.txt");
//        Digraph G = new Digraph(in);
//        SAP sap = new SAP(G);
//        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length = sap.length(v, w);
//            int ancestor = sap.ancestor(v, w);
//            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        }
    }
}


//    private int[] findSAP(Iterable<Integer> sources) {
////        int[] result = new int[2];
////        int minLength = INFINITY;
////        int minAncestor = -1;
//        MinPQ<Node> candidates = new MinPQ<>();
//        boolean[] marked = new boolean[graph.V()];
//        int[] distTo = new int[graph.V()];
//        ST<Integer, Integer> ancestors = new ST<>();
//        Queue<Integer> q = new Queue<Integer>();
//        for (int s : sources) {
//            marked[s] = true;
//            distTo[s] = 0;
//            q.enqueue(s);
//        }
//        while (!q.isEmpty()) {
//            int v = q.dequeue();
//            for (int w : graph.adj(v)) {
//                if (!marked[w]) {
//                    distTo[w] = distTo[v] + 1;
//                    marked[w] = true;
//                    q.enqueue(w);
//                } else if (!ancestors.contains(w)) {
//                    ancestors.put(w, distTo[v] + 1 + distTo[w]);
//                }
//            }
//        }
////        for (int ancestor : ancestors.keys()) {
////            if (ancestors.get(ancestor) < minLength) {
////                minLength = ancestors.get(ancestor);
////                minAncestor = ancestor;
////            }
////        }
////        if (minLength == INFINITY) minLength = -1;
////        result[0] = minLength;
////        result[1] = minAncestor;
//        return result;
//    }