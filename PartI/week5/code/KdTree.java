import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.Queue;

public class KdTree {
    private int sz;
    private Node root;
    private static final RectHV CONTAINER = new RectHV(0, 0, 1, 1);

    public KdTree() {
        this.sz = 0;
        this.root = null;
    }

    public boolean isEmpty() {
        return sz == 0;
    }

    public int size() {
        return sz;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        assert inRange(p);
        root = insert(root, p, null, CONTAINER);
    }

    private Node insert(Node x, Point2D p, Node parent, RectHV rect) {
        if (x == null && parent != null) {
            sz += 1;
            return new Node(p, null, null, !parent.orientation, rect);
        } else if (x == null) {
            sz += 1;
            return new Node(p, null, null, true, rect);
        }

        int cmp = x.compareTo(p);
        if (cmp < 0) x.right = insert(x.right, p, x, x.rightRect);
        else if (cmp > 0) x.left = insert(x.left, p, x, x.leftRect);
        else return x;

        return x;
    }

    private class Node implements Comparable<Point2D> {
        private Point2D p;
        private boolean orientation; // true:vertical, false:horizontal
        private Node left;
        private Node right;
        private RectHV leftRect;
        private RectHV rightRect;

        public Node(Point2D p, Node left, Node right) {
            this.p = p;
            this.orientation = true;
            this.left = left;
            this.right = right;
        }

        public Node(Point2D p, Node left, Node right, boolean orientation, RectHV rect) {
            this.p = p;
            this.orientation = orientation;
            this.left = left;
            this.right = right;
            this.leftRect = leftRect(rect);
            this.rightRect = rightRect(rect);
        }

        public void setOrientation(boolean orientation) {
            this.orientation = orientation;
        }

        public Point2D getPoint() {
            return p;
        }

        private RectHV leftRect(final RectHV rect) {
            if (this.orientation) {
                return new RectHV(rect.xmin(), rect.ymin(), this.getPoint().x(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), this.getPoint().y());
            }
        }

        private RectHV rightRect(final RectHV rect) {
            if (this.orientation) {
                return new RectHV(this.getPoint().x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), this.getPoint().y(), rect.xmax(), rect.ymax());
            }
        }

        @Override
        public int compareTo(Point2D that) {
            if (p.x() == that.x() && p.y() == that.y()) return 0;
            if (orientation) {
                if (p.x() > that.x()) return 1;
                else return -1;
            } else {
                if (p.y() > that.y()) return 1;
                else return -1;
            }
        }

    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        while (x != null) {
            int cmp = x.compareTo(p);
            if (cmp < 0) x = x.right;
            else if (cmp > 0) x = x.left;
            else return true;
        }
        return false;
    }

    // draw all of the points to standard draw
    public void draw() {
        StdDraw.setScale(0, 1);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        CONTAINER.draw();

        draw(root, CONTAINER);
    }

    private void draw(final Node node, final RectHV rect) {
        if (node == null) {
            return;
        }

        // draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        new Point2D(node.getPoint().x(), node.getPoint().y()).draw();

        // get the min and max points of division line
        Point2D min, max;
        if (node.orientation) {
            StdDraw.setPenColor(StdDraw.RED);
            min = new Point2D(node.getPoint().x(), rect.ymin());
            max = new Point2D(node.getPoint().x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            min = new Point2D(rect.xmin(), node.getPoint().y());
            max = new Point2D(rect.xmax(), node.getPoint().y());
        }

        // draw that division line
        StdDraw.setPenRadius();
        min.drawTo(max);

        // recursively draw children
//        draw(node.left, leftRect(rect, node));
//        draw(node.right, rightRect(rect, node));
        draw(node.left, node.leftRect);
        draw(node.right, node.rightRect);
    }

//    private RectHV leftRect(final RectHV rect, final Node node) {
//        if (node.orientation) {
//            return new RectHV(rect.xmin(), rect.ymin(), node.getPoint().x(), rect.ymax());
//        } else {
//            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.getPoint().y());
//        }
//    }
//
//    private RectHV rightRect(final RectHV rect, final Node node) {
//        if (node.orientation) {
//            return new RectHV(node.getPoint().x(), rect.ymin(), rect.xmax(), rect.ymax());
//        } else {
//            return new RectHV(rect.xmin(), node.getPoint().y(), rect.xmax(), rect.ymax());
//        }
//    }

//    private Iterable<Node> nodes() {
//        java.util.Queue<Node> queue = new LinkedList<Node>();
//        queue = inorder(root, queue);
//        return queue;
//    }

    private Iterable<Node> nodes(Node node) {
        java.util.Queue<Node> queue = new LinkedList<Node>();
        queue = inorder(node, queue);
        return queue;
    }

    private Queue<Node> inorder(Node x, Queue<Node> queue) {
        if (x.left != null) queue = inorder(x.left, queue);
        assert queue.offer(x);
        if (x.right != null) queue = inorder(x.right, queue);
        return queue;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Bag<Point2D> innerPoints = new Bag<>();
        if (root == null) return innerPoints;
        findInners(root, rect, innerPoints);
        return innerPoints;
//        Node node = root;
//        Node parent = root;
//        while (node != null) {
//            if (node.orientation) {
//                if (node.getPoint().x() >= rect.xmax()) {
//                    parent = node;
//                    node = node.left;
//                } else if (node.getPoint().x() <= rect.xmin()) {
//                    parent = node;
//                    node = node.right;
//                } else {
//                    break;
//                }
//            } else {
//                if (node.getPoint().y() >= rect.ymax()) {
//                    parent = node;
//                    node = node.left;
//                } else if (node.getPoint().y() <= rect.ymin()) {
//                    parent = node;
//                    node = node.right;
//                } else {
//                    break;
//                }
//            }
//        }
//        return inners(parent, rect);
    }

    private void findInners(final Node node, final RectHV rect, Bag<Point2D> innerPoints){
        if (node == null) return;

        if (rect.contains(node.getPoint())){
            innerPoints.add(node.getPoint());
        }

        if (node.leftRect.intersects(rect)){
            findInners(node.left, rect, innerPoints);
        }

        if (node.rightRect.intersects(rect)){
            findInners(node.right, rect, innerPoints);
        }

    }

//    private Bag<Point2D> inners(Node node, RectHV rect) {
//        if (rect == null) throw new IllegalArgumentException();
//        Bag<Point2D> inner = new Bag<>();
//        for (Node n :
//                nodes(node)) {
//            if (rect.contains(n.p)) {
//                inner.add(n.p);
//            }
//        }
//        return inner;
//    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Stack<Node> searchPath = new Stack<>();
        findPath(root, p, searchPath);
        Node nearestNode = traceBack(searchPath, p);
        return nearestNode.getPoint();
    }

    private void findPath(Node node, Point2D p, Stack<Node> searchPath) {
        if (node == null) return;
        int cmp = node.compareTo(p);
        if (cmp > 0) {
            searchPath.push(node);
            findPath(node.left, p, searchPath);
        } else {
            searchPath.push(node);
            findPath(node.right, p, searchPath);
        }
    }

    private Node traceBack(Stack<Node> searchPath, Point2D p) {
        Node nearestNode = searchPath.peek();
        while (!searchPath.isEmpty()) {
            Node currentNode = searchPath.pop();
            if (currentNode.left == null && currentNode.right == null) {
                // 末节点
                if (currentNode.getPoint().distanceTo(p)
                        < nearestNode.getPoint().distanceTo(p)) {
                    nearestNode = currentNode;
                }
            } else {
                // 判断是否相交
                int cmp = currentNode.compareTo(p);
                if (cmp > 0) {
                    if (currentNode.rightRect.distanceTo(p)
                            < nearestNode.getPoint().distanceTo(p)) {
                        findPath(currentNode.right, p, searchPath);
                    }
                } else {
                    if (currentNode.leftRect.distanceTo(p)
                            < nearestNode.getPoint().distanceTo(p)) {
                        findPath(currentNode.left, p, searchPath);
                    }
                }

                if (currentNode.getPoint().distanceTo(p)
                        < nearestNode.getPoint().distanceTo(p)) {
                    nearestNode = currentNode;
                }

            }
        }
        return nearestNode;
    }

    // 建立直线方程, 判断是否相交
//    private boolean checkLineIntersection(Node nearestNode, Node target, Point2D p) {
//        // k = 0
//        if ((p.y() - nearestNode.getPoint().y()) == 0) {
//            if (p.x() - target.getPoint().x() > p.x() - nearestNode.getPoint().x()) return false;
//            else return true;
//        }
//
//        // k = Infinite
//        if (p.x() - nearestNode.getPoint().x() == 0) {
//            if (p.y() - target.getPoint().y() > p.y() - nearestNode.getPoint().y()) return false;
//            else return true;
//        }
//
//        // k = normal
//        double k = (p.y() - nearestNode.getPoint().y()) / (p.x() - nearestNode.getPoint().x());
//        double b = p.y() - k * p.x();
//        double projectionY = k * target.getPoint().x() + b;
//        Point2D projectionPoint = new Point2D(target.getPoint().x(), projectionY);
//        if (p.distanceTo(projectionPoint) > nearestNode.getPoint().distanceTo(p)) return false;
//        else return true;
//    }

    private boolean inRange(Point2D p) {
        if (p.x() < 0 || p.x() > 1 || p.y() < 0 || p.y() > 0) return false;
        return true;
    }

//    private Node get(Point2D p) {
//        Node x = root;
//        while (x != null) {
//            int cmp = x.compareTo(p);
//            if (cmp > 0) x = x.left;
//            else if (cmp < 0) x = x.right;
//            else break;
//        }
//        return x;
//    }

    public static void main(String[] args) {
// initialize the two data structures with point from file
//        String filename = "C:\\Users\\ASH\\IdeaProjects\\algorithm_ex\\algorithm_ex5\\src\\circle10.txt";
//        In in = new In(filename);
//        KdTree kdtree = new KdTree();
//        String s = "A B C D E F G H I J";
//        String[] sa = s.split(" ");
//        int count = 0;
//        while (!in.isEmpty()) {
//
//            if (StdDraw.isMousePressed()) {
//                double x = in.readDouble();
//                double y = in.readDouble();
//                Point2D p = new Point2D(x, y);
//                kdtree.insert(p);
//                kdtree.draw();
//                System.out.println(sa[count++] + " " + kdtree.get(p).orientation);
//                StdDraw.pause(100);
//            }
//
//        }
//        // draw all of the points
//        StdDraw.clear();
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.setPenRadius(0.01);
//        kdtree.draw();
//
//        for (Node n :
//                kdtree.nodes()) {
//            System.out.println(n.getPoint());
//            System.out.println(n.orientation);
//        }
    }
}
