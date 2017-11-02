import java.util.Comparator;

/**
 * Created by ASH on 2017/9/23.
 */


public class FastCollinearPoints {
    private final LineSegment[] mylineSegments;
    private int sz;


    public FastCollinearPoints(Point[] points) {
        validation(points.clone());
        Point[] mypoints = points.clone();
        sort(mypoints); // 先按坐标排个序
        this.sz = 0;
        this.mylineSegments = findLineSegments(mypoints);

    }

    // 返回求得的平面数量
    public int numberOfSegments() {
        return sz;
    }

    // 返回求得的平面
    public LineSegment[] segments() {
        return mylineSegments.clone();
    }

    // 找到所有的平面
    private LineSegment[] findLineSegments(Point[] points) {
        LineSegment[] lineSegments = new LineSegment[0];
        Point[] currentPointOrder;
        Comparator<Point> currentComparator;
        int count;
        double currentSlope;
        double thatSlope;
        LineSegment currentLineSegment;
        for (int i = 0; i < points.length - 3; i++) {
            currentPointOrder = points.clone();
            currentComparator = currentPointOrder[i].slopeOrder();
            sort(currentPointOrder, currentComparator);
            count = 0;
            currentSlope = 0;
            for (int j = 0; j < currentPointOrder.length; j++) {
                thatSlope = points[i].slopeTo(currentPointOrder[j]);
                if (thatSlope == Double.NEGATIVE_INFINITY) {
                    count = 0;
                    currentSlope = thatSlope;
                    continue;
                }
                if (currentSlope == thatSlope) {
                    count += 1;
                    if (j == currentPointOrder.length - 1 && count >= 2) {
                        if (!equalless(points[i], currentPointOrder[j])
                                || !equals(currentPointOrder[j], currentPointOrder[j - 1], currentComparator)) {
                            count = 0;
                            currentSlope = thatSlope;
                            continue;
                        }
                        currentLineSegment = new LineSegment(points[i], currentPointOrder[j]);
                        if (sz > 0 && checkSub(currentLineSegment, lineSegments)) continue;
                        if (sz >= lineSegments.length) {
                            lineSegments = increase(lineSegments);
                        }
                        lineSegments[sz] = currentLineSegment;
                        sz += 1;
                        count = 0;
                        currentSlope = thatSlope;
                    }
                } else if (count >= 2) {
                    if (!equalless(points[i], currentPointOrder[j - 1])
                            || !equals(currentPointOrder[j - 1], currentPointOrder[j - 2], currentComparator)) {
                        count = 0;
                        currentSlope = thatSlope;
                        continue;
                    }
                    currentLineSegment = new LineSegment(points[i], currentPointOrder[j - 1]);

                    if (sz > 0 && checkSub(currentLineSegment, lineSegments)) continue;

                    if (sz >= lineSegments.length) {
                        lineSegments = increase(lineSegments);
                    }
                    lineSegments[sz] = currentLineSegment;
                    sz += 1;
                    count = 0;
                    currentSlope = thatSlope;
                } else {
                    count = 0;
                    currentSlope = thatSlope;
                }
            }
        }

        return lineSegments;
    }

    private boolean checkSub(LineSegment lineSegment, LineSegment[] lineSegments) {
        String segement;
        String currentSegement = lineSegment.toString().replace(
                "(", "").replace(")", "").replace(" ", "").replace(">", "");
        Point currentp = new Point(Integer.parseInt(currentSegement.split("-")[0].split(",")[0]),
                Integer.parseInt(currentSegement.split("-")[0].split(",")[1]));
        Point currentq = new Point(Integer.parseInt(currentSegement.split("-")[1].split(",")[0]),
                Integer.parseInt(currentSegement.split("-")[1].split(",")[1]));
        Point p;
        Point q;
        for (int i = 0; i < sz; i++) {
            segement = lineSegments[i].toString().replace(
                    "(", "").replace(")", "").replace(" ", "").replace(">", "");
            p = new Point(Integer.parseInt(segement.split("-")[0].split(",")[0]),
                    Integer.parseInt(segement.split("-")[0].split(",")[1]));
            q = new Point(Integer.parseInt(segement.split("-")[1].split(",")[0]),
                    Integer.parseInt(segement.split("-")[1].split(",")[1]));
            if (equals(q, currentq) && equals(p, currentp)) {
                return true;
            } else if (equals(q, currentq)) {
                if (equals(p, q, currentp.slopeOrder())) return true;
                else continue;
            }
        }
        return false;
    }

    // 对输入数据校验
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

    // 动态增加数组长度
    private LineSegment[] increase(LineSegment[] lineSegments) {
        LineSegment[] increased = new LineSegment[lineSegments.length + 1];
        for (int i = 0; i < lineSegments.length; i++) {
            increased[i] = lineSegments[i];
        }
        return increased;
    }

    // 比较两个点的坐标值
    private boolean less(Point p1, Point p2) {
        return p1.compareTo(p2) < 0;
    }

    // 比较两个点到一点的斜率
    private boolean less(Point p1, Point p2, Comparator<Point> ct) {
        return ct.compare(p1, p2) < 0;
    }

    private boolean equals(Point p1, Point p2, Comparator<Point> ct) {
        return ct.compare(p1, p2) == 0;
    }

    // 比较两个点的坐标值
    private boolean equals(Point p1, Point p2) {
        return p1.compareTo(p2) == 0;
    }

    // 比较两个点的坐标值
    private boolean equalless(Point p1, Point p2) {
        return p1.compareTo(p2) <= 0;
    }

    // 归并排序 - by 坐标
    private void sort(Point[] c) {
        int n = c.length;
        Point[] aux = new Point[n];
        for (int scale = 1; scale < n; scale = scale + scale) {
            for (int lo = 0; lo < n - scale; lo += scale + scale) {
                merge(c, aux, lo, lo + scale - 1, Math.min(lo + scale + scale - 1, n - 1));
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

    // 归并排序 - by 到一点的斜率 ct 为斜率比较器
    private void sort(Point[] c, Comparator<Point> ct) {
        int n = c.length;
        Point[] aux = new Point[n];
        for (int scale = 1; scale < n; scale = scale + scale) {
            for (int lo = 0; lo < n - scale; lo += scale + scale) {
                merge(c, aux, lo, lo + scale - 1, Math.min(lo + scale + scale - 1, n - 1), ct);
            }
        }
    }

    private void merge(Point[] c, Point[] aux, int lo, int mid, int hi, Comparator<Point> ct) {
        if (hi > mid && less(c[mid], c[mid + 1], ct)) return;

        for (int k = lo; k <= hi; k++) {
            aux[k] = c[k];
        }

        int i = lo, j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            if (i > mid) c[k] = aux[j++];
            else if (j > hi) c[k] = aux[i++];
            else if (less(aux[j], aux[i], ct)) c[k] = aux[j++];
            else c[k] = aux[i++];
        }
    }

}
