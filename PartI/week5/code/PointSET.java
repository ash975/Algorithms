import edu.princeton.cs.algs4.*;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        this.set = new SET<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        assert inRange(p);
        if (set.contains(p)) {
            return;
        }
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p :
                set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        return inners(rect);
    }

    private Bag<Point2D> inners(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Bag<Point2D> inner = new Bag<>();
        for (Point2D p1 :
                set) {
            if (rect.contains(p1)) {
                inner.add(p1);
            }
        }
        return inner;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;
        Point2D minPoint = null;
        for (Point2D p1 :
                set) {
            if (minPoint == null){
                minPoint = p1;
            }
            if (p1.distanceTo(p) < minPoint.distanceTo(p)) {
                minPoint = p1;
            }
        }
        return minPoint;
    }

    private boolean inRange(Point2D p) {
        if (p.x() < 0 || p.x() > 1 || p.y() < 0 || p.y() > 0) return false;
        return true;
    }

    public static void main(String[] args) {
    }
}
