/**
 * Created by ASH on 2017/9/15.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*----------------------------------------------------------------------------
  note:
  1. The API specifies that valid row and column indices are between 1 and n.
  2. if (i <= 0 || i > n) throw new IndexOutOfBoundsException("row index i out
        of bounds");
  3. What should stddev() return if trials equals 1? The sample standard
        deviation is undefined. We recommend returning Double.NaN.
  4. We recommend writing a private method with a signature along the lines of
        int xyTo1D(int, int) that performs this conversion. You will need to
        utilize the percolation grid size when writing this method.
  5. The open() method should do three things. First, it should validate the
        indices of the site that it receives. Second, it should somehow mark the
        site as open. Third, it should perform some sequence of WeightedQuickUnionUF
        operations that links the site in question to its open neighbors.
        The constructor and instance variables should facilitate the open() method's
        ability to do its job.
  ----------------------------------------------------------------------------*/

public class Percolation {
    private int[][] grid;    // 建立网格图形
    // The private instance (or static) variable 'volume' can be made 'final';
    // it is initialized only in the declaration or constructor. [ImmutableField]
    private final int volume;    // 图形边长
    private final WeightedQuickUnionUF percolationPipe;    // 渗滤管道
    private final int pipeVolume;    // 管道大小
    private int nOpenSites;    // 激活的方块数目

    // 构造函数
    public Percolation(int n) {
        this.nOpenSites = 0;
        this.volume = n;
        // 在最后多建立一个节点 作为下界点, 0 则作为上界点,
        // 管道默认建立是 0 ~ max - 1, 所以 + 2
        this.pipeVolume = n * n + 2;
        // 因为要从 1:n , 所以建立 n+1
        this.grid = new int[n + 1][n + 1];
        this.percolationPipe = new WeightedQuickUnionUF(pipeVolume);
    }

    // 激活方块并连接邻居
    public void open(int row, int col) {
        validation(row);
        validation(col);
        int position = xyTo1D(row, col);
        if (position > 0 && position <= this.volume) { // 判断连接上界
            this.percolationPipe.union(position, 0);
        } else if (position > pipeVolume - this.volume - 2 &&
                position <= pipeVolume - 2) { // 判断连接下界
            this.percolationPipe.union(position, this.pipeVolume - 1);
        }
        unionNeighbour(row + 1, col, position); // 上邻居
        unionNeighbour(row - 1, col, position); // 下邻居
        unionNeighbour(row, col + 1, position); // 右邻居
        unionNeighbour(row, col - 1, position); // 左邻居
        if (this.grid[row][col] == 0) {
            this.grid[row][col] = 1; // 标记已经激活
            this.nOpenSites += 1; // 记录激活数
        }
    }

    // 判断方块是否激活
    public boolean isOpen(int row, int col) {
        validation(row);
        validation(col);
        if (this.grid[row][col] != 0) {
            return true;
        } else {
            return false;
        }
    }

    // 判断方块是否连通上边界
    public boolean isFull(int row, int col) {
        validation(row);
        validation(col);
        int position = xyTo1D(row, col);
        return this.percolationPipe.connected(position, 0);
    }

    // 返回激活的方块数
    public int numberOfOpenSites() {
        return this.nOpenSites;
    }

    // 返回是否渗滤
    public boolean percolates() {
        return this.percolationPipe.connected(0, this.pipeVolume - 1);
    }

    // 判断是否越界
    private void validation(int i) {
        if (i <= 0 || i > this.volume) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
    }

    // 二维平展一维
    private int xyTo1D(int row, int col) {
        validation(row);
        validation(col);
        return col + (row - 1) * this.volume;
    }

    // 连接激活块的上下左右
    private void unionNeighbour(int row, int col, int position) {
        if (0 < row && row <= this.volume && 0 < col && col <= this.volume) {
            if (isOpen(row, col)) {
                int neighbour = xyTo1D(row, col);
                this.percolationPipe.union(position, neighbour);
            }
        }
    }


    public static void main(String[] args) {
        int volume = 5;
        Percolation percolation = new Percolation(volume);
        percolation.open(1, 2);
        percolation.open(1, 1);
        System.out.print(percolation.percolationPipe.connected(0, 1));
    }
}
