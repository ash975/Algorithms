/*
    Create By ASH 09 - 27
 */

import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] blocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        validation(blocks);
        this.blocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            this.blocks[i] = blocks[i].clone();
        }
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        int missed = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i * blocks.length + j + 1) {
                    missed += 1;
                }
            }
        }
        return missed;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int totalDistance = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != 0) {
                    totalDistance += Math.abs(j - (blocks[i][j] - 1) % blocks.length)
                            + Math.abs(i - (blocks[i][j] - 1) / blocks.length);
                }
            }
        }
        return totalDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() <= 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int x = StdRandom.uniform(dimension());
        int y = StdRandom.uniform(dimension());
        int nx = StdRandom.uniform(dimension());
        int ny = StdRandom.uniform(dimension());
        while (blocks[x][y] == 0 || blocks[nx][ny] == 0 || blocks[nx][ny] == blocks[x][y]) {
            x = StdRandom.uniform(dimension());
            y = StdRandom.uniform(dimension());
            nx = StdRandom.uniform(dimension());
            ny = StdRandom.uniform(dimension());
        }
        return new Board(swap(blocks, x, y, nx, ny));
    }

    private int[][] swap(int[][] blocks, int x, int y, int nx, int ny) {
        int[][] nBlocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            nBlocks[i] = blocks[i].clone();
        }
        int temp = nBlocks[x][y];
        nBlocks[x][y] = nBlocks[nx][ny];
        nBlocks[nx][ny] = temp;
        return nBlocks;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return false;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.toString().equals(that.toString())) {
            return true;
        } else {
            return false;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new neighbors(blocks);
    }

    private class neighbors implements Iterable<Board> {
        private final int[][] blocks;

        public neighbors(int[][] blocks) {
            this.blocks = blocks;
        }

        @Override
        public Iterator<Board> iterator() {
            return new neighborIterator(this.blocks);
        }

        private class neighborIterator implements Iterator<Board> {
            // 这里也可用 queue 或者 stack
            private int n;    // 邻居数量
            private final Board[] neighbors;    // 邻居的数组
            private final int[][] blocks;

            public neighborIterator(int[][] blocks) {
                this.n = 0;
                this.blocks = new int[blocks.length][blocks.length];
                for (int i = 0; i < blocks.length; i++) {
                    this.blocks[i] = blocks[i].clone();
                }
                this.neighbors = findNeighbors(this.blocks);
            }

            @Override
            public boolean hasNext() {
                return n != 0;
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Board neighbor = neighbors[n - 1];
                n -= 1;
                return neighbor;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private Board[] findNeighbors(int[][] blocks) {
                int x = -1, y = -1;
                Board[] neighbors;
                // 定位空白格子的坐标存入 x y
                for (int i = 0; i < blocks.length; i++) {
                    for (int j = 0; j < blocks.length; j++) {
                        if (blocks[i][j] == 0) {
                            x = i;
                            y = j;
                            break;
                        }
                    }
                    if (x != -1 || y != -1) {
                        break;
                    }
                }
                // 根据空白格子的坐标寻找邻居的数量
                if ((x == 0 && y == 0) || (x == blocks.length - 1 && y == blocks.length - 1)
                        || (x == 0 && y == blocks.length - 1) || (y == 0 && x == blocks.length - 1)) {
                    n = 2;
                } else if (x == 0 || x == blocks.length - 1 || y == 0 || y == blocks.length - 1) {
                    n = 3;
                } else {
                    n = 4;
                }
                neighbors = new Board[n];
                switch (n) {
                    // 空格在四个角的情况
                    case 2: {
                        if (x == y) {
                            if (x == 0) {
                                neighbors[0] = new Board(swap(blocks, x, y, x + 1, y));
                                neighbors[1] = new Board(swap(blocks, x, y, x, y + 1));
                            } else {
                                neighbors[0] = new Board(swap(blocks, x, y, x - 1, y));
                                neighbors[1] = new Board(swap(blocks, x, y, x, y - 1));
                            }
                        } else {
                            if (x == 0) {
                                neighbors[0] = new Board(swap(blocks, x, y, x, y - 1));
                                neighbors[1] = new Board(swap(blocks, x, y, x + 1, y));
                            } else {
                                neighbors[0] = new Board(swap(blocks, x, y, x, y + 1));
                                neighbors[1] = new Board(swap(blocks, x, y, x - 1, y));
                            }
                        }
                    }
                    break;
                    // 空格在边框的情况
                    case 3: {
                        if (x == 0) {
                            neighbors[0] = new Board(swap(blocks, x, y, x + 1, y));
                            neighbors[1] = new Board(swap(blocks, x, y, x, y + 1));
                            neighbors[2] = new Board(swap(blocks, x, y, x, y - 1));
                        } else if (x == blocks.length - 1) {
                            neighbors[0] = new Board(swap(blocks, x, y, x - 1, y));
                            neighbors[1] = new Board(swap(blocks, x, y, x, y + 1));
                            neighbors[2] = new Board(swap(blocks, x, y, x, y - 1));
                        } else if (y == 0) {
                            neighbors[0] = new Board(swap(blocks, x, y, x - 1, y));
                            neighbors[1] = new Board(swap(blocks, x, y, x + 1, y));
                            neighbors[2] = new Board(swap(blocks, x, y, x, y + 1));
                        } else if (y == blocks.length - 1) {
                            neighbors[0] = new Board(swap(blocks, x, y, x - 1, y));
                            neighbors[1] = new Board(swap(blocks, x, y, x + 1, y));
                            neighbors[2] = new Board(swap(blocks, x, y, x, y - 1));
                        }
                    }
                    break;
                    // 空格在内部的情况
                    case 4: {
                        neighbors[0] = new Board(swap(blocks, x, y, x - 1, y));
                        neighbors[1] = new Board(swap(blocks, x, y, x + 1, y));
                        neighbors[2] = new Board(swap(blocks, x, y, x, y - 1));
                        neighbors[3] = new Board(swap(blocks, x, y, x, y + 1));
                    }
                    break;
                    default:
                        throw new NoSuchElementException();
                }
                return neighbors;
            }

            private int[][] swap(int[][] blocks, int x, int y, int nx, int ny) {
                int[][] nBlocks = new int[blocks.length][blocks.length];
                for (int i = 0; i < blocks.length; i++) {
                    nBlocks[i] = blocks[i].clone();
                }
                int temp = nBlocks[x][y];
                nBlocks[x][y] = nBlocks[nx][ny];
                nBlocks[nx][ny] = temp;
                return nBlocks;
            }
        }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder result = new StringBuilder(dimension() + "\n" + " ");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                result.append(blocks[i][j]).append("  ");
            }
            result.append("\n" + " ");
        }
        return result.toString();
    }

//     // another way
//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append(n + "\n");
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                s.append(String.format("%2d ", tiles[i][j]));
//            }
//            s.append("\n");
//        }
//        return s.toString();
//    }

    private void validation(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException();
        }
        Integer[] flatBlocks = new Integer[blocks.length * blocks.length];
        int n = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                flatBlocks[n] = blocks[i][j];
                n++;
            }
        }
        Quick.sort(flatBlocks);

        // 粗略检查比较快
        if (flatBlocks[0] != 0 ||
                flatBlocks[blocks.length * blocks.length - 1] != blocks.length * blocks.length - 1) {
            throw new IllegalArgumentException();
        }

        // 详细筛选, 应对重复对象
        for (int i = 0; i < flatBlocks.length; i++) {
            if (i != flatBlocks[i]) {
                throw new IllegalArgumentException();
            }
        }
    }

    // unit tests (not graded)
    public static void main(String[] args) {
//        // create initial board from file
//        In in = new In(args[0]);
//        int n = in.readInt();
//        int[][] blocks = new int[n][n];
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++)
//                blocks[i][j] = in.readInt();
//        Board initial = new Board(blocks);
//
//        // solve the puzzle
//        Solver solver = new Solver(initial);
//
//        // print solution to standard output
//        if (!solver.isSolvable())
//            StdOut.println("No solution possible");
//        else {
//            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
//        }
        int[][] test = new int[2][2];
        test[0][0] = 1;
        test[0][1] = 3;
        test[1][0] = 0;
        test[1][1] = 2;
        Board b = new Board(test);
        Board bw = new Board(test);
        System.out.println(b.equals(bw));
    }
}
