/*
    Create By ASH 09 - 27
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int moves;
    private final Board initial;
    private boolean solvable;
    private final Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.moves = 0;
        this.initial = initial;
        SearchNode last = searchPath(this.initial);
        this.solution = generateSolution(last);
    }

    private class SearchNode implements Comparable<SearchNode> {
        public final int moves;
        private final int manhattan;
        public final int priority;
        public final Board board;
        public final SearchNode cameFrom;

        public SearchNode(Board board, int moves, SearchNode cameFrom) {
            this.board = board;
            this.moves = moves;
            this.manhattan = board.manhattan();
            this.priority = moves + this.manhattan;
            this.cameFrom = cameFrom;
        }

        private Iterable<Board> getNeighbor() {
            return board.neighbors();
        }

        @Override
        public int compareTo(SearchNode that) {
            if (priority > that.priority) {
                return 1;
            }
            if (priority < that.priority) {
                return -1;
            }

            // 如果 priority 相等 优先排列 深度
            if (moves > that.moves) {
                return -1;
            }
            if (moves < that.moves) {
                return 1;
            }

            // 如果 priority 和 moves 都相等 优先距离短的
            if (manhattan < that.manhattan) {
                return -1;
            }
            if (manhattan > that.manhattan) {
                return 1;
            }

            return 0;
        }
    }

    private SearchNode searchPath(Board initial) {
        MinPQ<SearchNode> openSet = new MinPQ<>();
        MinPQ<SearchNode> testSolvable = new MinPQ<>();
        SearchNode current;
        SearchNode testCurrent;
        openSet.insert(new SearchNode(initial, 0, null));
        testSolvable.insert(new SearchNode(initial.twin(), 0, null));
        while (!openSet.isEmpty()) {
            current = openSet.delMin();

            testCurrent = testSolvable.delMin();
            if (testCurrent.board.isGoal()) {
                moves = -1;
                solvable = false;
                return null;
            }

            for (Board neighbor :
                    testCurrent.getNeighbor()) {
                if (testCurrent.cameFrom != null && neighbor.equals(testCurrent.cameFrom.board)) {
                    continue;
                }
                testSolvable.insert(new SearchNode(neighbor, testCurrent.moves + 1, testCurrent));
            }


            if (current.board.isGoal()) {
                moves = current.moves;
                solvable = true;
                return current;
            }

            for (Board neighbor :
                    current.getNeighbor()) {
                if (current.cameFrom != null && neighbor.equals(current.cameFrom.board)) {
                    continue;
                }
                openSet.insert(new SearchNode(neighbor, current.moves + 1, current));

            }
        }
        solvable = false;
        moves = -1;
        return null;
    }

    private Stack<Board> generateSolution(SearchNode last) {
        if (last == null) {
            return null;
        }
        Stack<Board> solution = new Stack<>();
        solution.push(last.board);
        while (last.cameFrom != null) {
            solution.push(last.cameFrom.board);
            last = last.cameFrom;
        }
        return solution;
    }


    /*
    8-puzzle问题中凡是无解的图，将除了空白栏之外的任意两个格子交换，都能得到可解图。而可解图通过一次交换得到的是无解图。
    To detect such situations, use the fact that boards are divided into two equivalence classes with respect
    to reachability:
    (i) those that lead to the goal board and (ii) those that lead to the goal board if we modify the initial
    board by swapping any pair of blocks (the blank square is not a block).
     */
    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In("C:\\Users\\ASH\\IdeaProjects\\algorithm_ex\\algorithm_ex4\\data\\puzzle44.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        long start = System.currentTimeMillis();
        // solve the puzzle
        Solver solver = new Solver(initial);
        System.out.println(System.currentTimeMillis() - start + " ms");

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
        }
//        int[][] test = new int[2][2];
//        test[0][0] = 0;
//        test[0][1] = 1;
//        test[1][0] = 3;
//        test[1][1] = 2;
//        Board b = new Board(test);
//        System.out.println((new Solver(b)).solution);
    }
}