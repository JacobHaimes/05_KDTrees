import java.util.Iterator;

/**
 * PSBruteForce is a Point collection that provides brute force
 * nearest neighbor searching using red-black tree.
 */
public class PSBruteForce<Value> implements PointSearch<Value> {
    private Point min;
    private Point max;
    private RedBlackBST<Point,Value> BFTree;
    // constructor makes empty collection
    public PSBruteForce() {
        BFTree = new RedBlackBST<>();
    }

    // add the given Point to KDTree
    public void put(Point p, Value v) {
        min = min==null ? p : Point.min(p, min);
        max = max==null ? p : Point.max(p, max);
        BFTree.put(p,v);
    }

    public Value get(Point p) {
        return BFTree.get(p);
    }

    public boolean contains(Point p) {
        return BFTree.contains(p);
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() {
        return BFTree.keys();
    }

    // return the Point that is closest to the given Point
    public Point nearest(Point p) {
        double nearDist = -1;
        Point nearestP = null;
        for(Point i: this.points()){
            if(nearDist == -1 || i.dist(p) < nearDist){
                nearDist = i.dist(p);
                nearestP = i;
            }
        }
        return nearestP;
    }

    // return the Value associated to the Point that is closest to the given Point
    public Value getNearest(Point p) {
        return BFTree.get(nearest(p));
    }

    // return the min and max for all Points in collection.
    // The min-max pair will form a bounding box for all Points.
    // if KDTree is empty, return null.
    public Point min() { return min; }
    public Point max() { return max; }

    // return the k nearest Points to the given Point
    public Iterable<Point> nearest(Point p, int k) {

        MaxPQ<PointDist> nearestK = new MaxPQ<>();
        for(Point i: this.points()){
            PointDist j = new PointDist(i, i.dist(p));
            nearestK.insert(j);
            while (nearestK.size() > k){
                nearestK.delMax();
            }
        }
        Stack<Point> toReturn = new Stack<>();
        for (PointDist i: nearestK){
            toReturn.push(i.p());
        }
        return toReturn;
    }

    public Iterable<Partition> partitions() {

        return null;
    }

    // return the number of Points in KDTree
    public int size() { return BFTree.size(); }

    // return whether the KDTree is empty
    public boolean isEmpty() { return BFTree.isEmpty();}

    // place your timing code or unit testing here
    public static void main(String[] args) {
    }
}
