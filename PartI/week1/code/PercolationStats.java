/**
 * Created by ASH on 2017/9/15.
 */

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private final int trials;
    private final int volume;
    private final double[] trialsX;

    // 构造方法
    public PercolationStats(int n, int trials) {
        this.trials = trials;
        this.volume = n;
        validation();
        this.trialsX = new double[trials];
        for (int i = 0; i < trials; i++) {
            this.trialsX[i] = (double) oneTrial();
        }
    }

    // 求均值
    public double mean() {
        return StdStats.mean(this.trialsX) / this.volume / this.volume;
    }

    // 求方差
    public double stddev() {
        return StdStats.stddev(this.trialsX) / this.volume / this.volume;
    }

    // 置信区间左
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / sqrt((double) this.trials);
    }

    // 置信区间右
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / sqrt((double) this.trials);
    }


    // 牛顿法开根号
    private double sqrt(double x) {
        double e = 0.000001;
        double z = 1;
        double k = 0;
        while (true) {
            z = z - (z * z - x) / (2 * z);
            if (z - k <= e && z - k >= -e) return z;
            k = z;
        }
    }

    // 验证 n 和 trials 的值
    private void validation() {
        if (this.volume <= 0 || this.trials <= 0) {
            throw new IllegalArgumentException();
        }
    }

    // 一个周期的值
    private int oneTrial() {
        Percolation percolation = new Percolation(this.volume);
        while (!percolation.percolates()) {
            percolation.open(StdRandom.uniform(1, this.volume + 1), StdRandom.uniform(1, this.volume + 1));
        }
        return percolation.numberOfOpenSites();
    }

    // 测试方法
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(200, 100);
        System.out.println(percolationStats.volume + "\t " + percolationStats.trials);
        System.out.println("mean = \t" + percolationStats.mean());
        System.out.println("stddev = \t" + percolationStats.stddev());
        System.out.println("95% confidence interval = \t" + "[ " + percolationStats.confidenceHi()
                + ", " + percolationStats.confidenceLo() + " ]");
    }
}
