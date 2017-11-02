import edu.princeton.cs.algs4.StdStats;

import java.util.Comparator;

/**
 * Created by ASH on 2017/9/20.
 */

public class BruteCollinearPoints {
    private final LineSegment[] mylineSegments;
    private int sz;


    public BruteCollinearPoints(Point[] points) {
        validation(points.clone());
        Point[] mypoints = points.clone();
        this.sz = 0;
        sort(mypoints);
//        Point[] aux = new Point[this.mypoints.length];
//        sort(this.mypoints, aux, 0, mypoints.length - 1);
        this.mylineSegments = findSegements(mypoints);
    }

    public int numberOfSegments() {
        return sz;
    }

    public LineSegment[] segments() {
        return mylineSegments.clone();
    }

    private LineSegment[] findSegements(Point[] points) {
        Point p;
        Point q;
        String segement;
        Comparator<Point> pointComparator;
        LineSegment[] lineSegments = new LineSegment[0];
        for (int i = 0; i < points.length - 3; i++) {
            pointComparator = points[i].slopeOrder();
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    if (!equals(points[j], points[k], pointComparator)) {
                        continue;
                    }
                    for (int indexThat = k + 1; indexThat < points.length; indexThat++) {
                        if (!equals(points[k], points[indexThat], pointComparator)) {
                            continue;
                        }
                        /*
                            It is bad style to write code that depends on the particular format of
                            the output from the toString() method, especially if your reason for
                            doing so is to circumvent the public API (which intentionally does not
                            provide access to the x- and y-coordinates).
                         */
                        if (sz > 0) {
                            segement = lineSegments[sz - 1].toString().replace(
                                    "(", "").replace(")", "").replace(" ", "").replace(">", "");
                            p = new Point(Integer.parseInt(segement.split("-")[0].split(",")[0]),
                                    Integer.parseInt(segement.split("-")[0].split(",")[1]));
                            q = new Point(Integer.parseInt(segement.split("-")[1].split(",")[0]),
                                    Integer.parseInt(segement.split("-")[1].split(",")[1]));
                            if (equals(p, points[i]) && equalless(points[indexThat], q)) {
                                continue;
                            }
                        }
                        if (sz >= lineSegments.length) {
                            lineSegments = increase(lineSegments);
                        }
                        lineSegments[sz] = new LineSegment(points[i], points[indexThat]);
                        sz += 1;

                    }
                }
            }
        }
        return lineSegments;
    }

    private void validation(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (equals(points[i], points[j])) throw new IllegalArgumentException();
            }
        }
        if (points[points.length - 1] == null) throw new IllegalArgumentException();
    }

    private LineSegment[] increase(LineSegment[] lineSegments) {
        LineSegment[] increased = new LineSegment[lineSegments.length + 1];
        for (int i = 0; i < lineSegments.length; i++) {
            increased[i] = lineSegments[i];
        }
        return increased;
    }

    private boolean equals(Point o1, Point o2, Comparator<Point> ct) {
        return ct.compare(o1, o2) == 0;
    }

    private boolean less(Point p1, Point p2) {
        return p1.compareTo(p2) < 0;
    }

    private boolean equals(Point p1, Point p2) {
        return p1.compareTo(p2) == 0;
    }

    private boolean equalless(Point p1, Point p2) {
        return p1.compareTo(p2) <= 0;
    }

    private void sort(Point[] c) {
        int n = c.length;
        int[] myMin = new int[2];
        Point[] aux = new Point[n];
        for (int scale = 1; scale < n; scale = scale + scale) {
            for (int lo = 0; lo < n - scale; lo += scale + scale) {
                myMin[0] = lo + scale + scale - 1;
                myMin[1] = n - 1;
                merge(c, aux, lo, lo + scale - 1, StdStats.min(myMin));
            }
        }
    }

    private void merge(Point[] c, Point[] aux, int lo, int mid, int hi) {
        if (hi > mid && less(c[mid], c[mid + 1])) return;

        for (int k = lo; k <= hi; k++) {
            aux[k] = c[k];
        }

        int i = lo, j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            if (i > mid) c[k] = aux[j++];
            else if (j > hi) c[k] = aux[i++];
            else if (less(aux[j], aux[i])) c[k] = aux[j++];
            else c[k] = aux[i++];
        }
    }
}
