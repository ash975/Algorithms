public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        if (wordNet == null) {
            throw new IllegalArgumentException();
        }
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException();
        int[][] nounRange = new int[nouns.length][nouns.length];
        Node outcastNode = null;
//        MaxPQ<Node> nounLength = new MaxPQ<>();
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                nounRange[i][j] = this.wordNet.distance(nouns[i], nouns[j]);
                nounRange[j][i] = nounRange[i][j];
            }
        }
        for (int i = 0; i < nounRange.length; i++) {
            int length = 0;
            int nonAncersor = 0;
            for (int j = 0; j < nounRange.length; j++) {
                if (nounRange[i][j] == -1) {
                    nonAncersor += 1;
                }
                length += nounRange[i][j];
            }
            if (outcastNode == null) {
                outcastNode = new Node(nouns[i], length, nonAncersor);
            } else {
                outcastNode = checkMax(outcastNode, new Node(nouns[i], length, nonAncersor));
            }
//            nounLength.insert(new Node(nouns[i], length, nonAncersor));
        }
        return outcastNode.noun;
//        return nounLength.max().noun;
    }

    private Node checkMax(final Node current, final Node that) {
        if (current.compareTo(that) < 0) {
            return that;
        } else {
            return current;
        }
    }

    private class Node implements Comparable<Node> {
        public String noun;
        public int length;
        public int nonAncersor;

        public Node(String noun, int length, int nonAncersor) {
            this.noun = noun;
            this.length = length;
            this.nonAncersor = nonAncersor;
        }

        @Override
        public int compareTo(Node that) {
            if (this.nonAncersor < that.nonAncersor || this.length < that.length) return -1;
            if (this.nonAncersor > that.nonAncersor || this.length > that.length) return 1;
            return 0;
        }
    }

    public static void main(String[] args) {
//        String[] testArgs = {
//                "./test_data/synsets.txt",
//                "./test_data/hypernyms.txt",
//                "./test_data/outcast5.txt",
//                "./test_data/outcast8.txt",
//                "./test_data/outcast11.txt",
//        };
////        WordNet wordnet = new WordNet(args[0], args[1]);
//        WordNet wordnet = new WordNet(testArgs[0], testArgs[1]);
//        Outcast outcast = new Outcast(wordnet);
////        for (int t = 2; t < args.length; t++) {
//        for (int t = 2; t < testArgs.length; t++) {
////            In in = new In(args[t]);
//            In in = new In(testArgs[t]);
//            String[] nouns = in.readAllStrings();
////            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
//            StdOut.println(testArgs[t] + ": " + outcast.outcast(nouns));
//    }
}
}
