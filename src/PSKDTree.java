import com.sun.javafx.scene.traversal.Direction;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

/**
 * PSKDTree is a Point collection that provides nearest neighbor searching using
 * 2d tree
 */
public class PSKDTree<Value> implements PointSearch<Value> {
    private class Node {
        Point p; // 24 bytes
        Value v; // 8 bytes
        Node left, right; // 16 bytes
        Partition.Direction dir; // 16 bytes (subclass, ask later)
        // a constructor
        public Node(Point pt,
                    Value vl,
                    Node l,
                    Node r,
                    Partition.Direction d)
        {
            p = pt;
            v = vl;
            left = l;
            right = r;
            dir = d;
        }
    } // 16 bytes of overhead (subclass, ask later)
    // the root node of the BST.
    // (We are not using data structure provided by libraries.)
    private Node root;
    // size of the tree
    private int size;
    // The down-left node in the entire map
    Point min;
    // The up-right node in the entire map
    Point max;

    // a tester function for iterators
    public void printValues() {
        Iterator<Node> it = iterator();
        while (it.hasNext()) {
            System.out.format("%s ", it.next().p.toString());
        }
    }
    public Iterator<Node> iterator() { return new KDTreeIterator(); }
    private class KDTreeIterator implements Iterator<Node> {
        Stack<Node> inLine;
        KDTreeIterator() {
            inLine = new Stack<>();
            if (root != null)
                inLine.push(root);
        }
        @Override
        public boolean hasNext() {
            return !inLine.isEmpty();
        }
        @Override
        public Node next() {
            Node toReturn = inLine.pop();
            if (toReturn.left != null) inLine.push(toReturn.left);
            if (toReturn.right != null) inLine.push(toReturn.right);
            return toReturn;
        }
    }

    // constructor makes empty kD-tree
    public PSKDTree() {
        root = null; // tree is empty when root == null.
        size = 0;
        min = max = null;
    }

    // a recursive function to get the Node, return null if it doesn't exist.
    private Node get(Point p, Node rootNode) {
        if (rootNode == null || p.compareTo(rootNode.p) == 0) return rootNode;
        if (p.xy(rootNode.dir) < rootNode.p.xy(rootNode.dir))
            // if it is on the left of rootNode, search left
            return get(p, rootNode.left);
        else // if (p.xy(rootNode.dir) >= rootNode.p.xy(rootNode.dir))
            // if it is on the right of rootNode, search right
            return get(p, rootNode.right);
    }


    // a recursive function to insert a node into somewhere under rootNode
    private void put(Point p,
                     Value v,
                     Node rootNode)
    {
        // We need to ask Dr. Denning how to deal with exact same points
        // But I think we shouldn't allow exact same points, because calling get() on that point will never return some points.
        if (p.compareTo(rootNode.p) == 0) {
            //  throw new java.lang.IllegalArgumentException("Point " + p.toString() + " already exists");
            rootNode.v = v; // update the point to current value and exit
            return;
        }
        Partition.Direction currentDirection = rootNode.dir; // the direction
        if (p.xy(currentDirection) < rootNode.p.xy(currentDirection)) {
            // if the point inserted is less than rootNode....
            if (rootNode.left == null) {
                // ....and the left of rootNode is empty
                // insert it there
                rootNode.left = new Node(p, v, null, null, Partition.nextDirection(currentDirection));
            }
            else {
                // ....and left of rootNode is another node
                // put it under that node.
                put(p, v, rootNode.left);
            }
        }
        else // if (p.xy(currentDirection) >= rootNode.p.xy(currentDirection))
        {
            // if the point inserted is greater than rootNode....
            if (rootNode.right == null) {
                // ....and the right of rootNode is empty
                // insert it there
                rootNode.right = new Node(p, v, null, null, Partition.nextDirection(currentDirection));
            }
            else {
                // ....and right of rootNode is another node
                // put it under that node.
                put(p, v, rootNode.right);
            }
        }
    }

    // add the given Point to kD-tree
    public void put(Point p, Value v) {
        if (p == null) throw new java.lang.NullPointerException("Point in parameter is null.");
        if (isEmpty()) {
            // if empty, set root to the element inserted and set children to null
            // Partition the screen into left and right (using x value)
            root = new Node(p, v, null, null, Partition.Direction.LEFTRIGHT);
            // increment size
            size++;
            // set max and min to the point contained in root
            max = min = root.p;
        }
        else {
            // otherwise, traverse down the tree and insert
            put(p, v, root);
            size++;
            max = Point.max(max,p);
            min = Point.min(min,p);
        }
    }
    // return the value if exists, return null if doesn't exist
    public Value get(Point p) {
        Node targetNode = get(p, root);
        return targetNode==null ? null : targetNode.v;
    }

    public boolean contains(Point p) {
        return get(p) != null;
    }

    public Value getNearest(Point p) {
        return null;
    }

    // return an iterable of all points in collection
    public Iterable<Point> points() {
        Stack<Point> iterable = new Stack<>();
        Iterator<Node> it = iterator();
        while (it.hasNext())
            iterable.push(it.next().p);
        return iterable;
    }


    private void addPartition(Node n, Stack<Partition> partitionStack, Point localMin, Point localMax) {
        if (n == null) return;
        Point newMin, newMax;
        if (n.dir == Partition.Direction.LEFTRIGHT) {
            newMin = new Point(n.p.x(), localMin.y());
            newMax = new Point(n.p.x(), localMax.y());
        }
        else {
            newMin = new Point(localMin.x(), n.p.y());
            newMax = new Point(localMax.x(), n.p.y());
        }
        partitionStack.push(new Partition(newMin, newMax, n.dir));
        addPartition(n.left, partitionStack, localMin, newMax);
        addPartition(n.right, partitionStack, newMin, localMax);
    }
    // return an iterable of all partitions that make up the kD-tree
    public Iterable<Partition> partitions() {
        Stack<Partition> toReturn = new Stack<>();
        addPartition(root, toReturn, min, max);
        return toReturn;
    }

    // return the Point that is closest to the given Point
    public Point nearest(Point p) {
        return nearest(p,1).iterator().next();
    }

    private void goDown(Node rootNode,
                        Point p,
                        Stack<Node> childStack,
                        Stack<Node> parentStack,
                        MaxPQ<PointDist> nearestPoints,
                        int k) {
        Node tmp = rootNode;
        while(true) {
            if(tmp == null) break;
            nearestPoints.insert(new PointDist(tmp.p, tmp.p.dist(p)));
            if(nearestPoints.size() > k) nearestPoints.delMax();
            if(p.xy(tmp.dir) < tmp.p.xy(tmp.dir)) {
                if(tmp.right != null) {
                    parentStack.push(tmp);
                    childStack.push(tmp.right);
                }
                tmp = tmp.left;
            }
            else {
                if(tmp.left != null) {
                    parentStack.push(tmp);
                    childStack.push(tmp.left);
                }
                tmp = tmp.right;
            }
        }
    }
    // return the k nearest Points to the given Point
    public Iterable<Point> nearest(Point p, int k) {
        if (k<0) throw new IllegalArgumentException("K can't be a negative value");
        if (k==0) return new Stack<>();
        Stack<Node> backPoints = new Stack<>();
        Stack<Node> parentPoints = new Stack<>();
        MaxPQ<PointDist> nearestPoints = new MaxPQ<>();
        goDown(root, p, backPoints, parentPoints, nearestPoints, k);
        Node tmp, tmpParent;
        while(!backPoints.isEmpty()){
            tmp = backPoints.pop();
            tmpParent = parentPoints.pop();
            if(Math.abs(tmpParent.p.xy(tmpParent.dir) - p.xy(tmpParent.dir)) < nearestPoints.max().d()){
                goDown(tmp, p, backPoints, parentPoints, nearestPoints, k);
            }
        }
        Stack<Point> toReturn = new Stack<>();
        for(PointDist pd: nearestPoints)
            toReturn.push(pd.p());
        return toReturn;
    }

    // return the min and max for all Points in collection.
    // The min-max pair will form a bounding box for all Points.
    // if kD-tree is empty, return null.
    public Point min() { return min; }
    public Point max() { return max; }

    // return the number of Points in kD-tree
    public int size() { return size; }

    // return whether the kD-tree is empty
    public boolean isEmpty() { return root == null; }

    public static void insertPoints(String filename, PointSearch<Integer> ps) {
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            ps.put(new Point(x,y), 1);
        }
    }
    // place your timing code or unit testing here
    public static void main(String[] args) {
        //PSKDTree<Integer> test = new PSKDTree<>();
        PSBruteForce<Integer> test = new PSBruteForce<>();
        String filename = args[0];
        insertPoints(filename, test);
        int count = 0; // count how many nearest point search has been done
        int limit = 10000;
        double startTime = System.nanoTime();
        while (count < limit) {
            test.nearest(Point.uniform());
            count++;
        }
        double endTime = System.nanoTime();
        double difference = (endTime-startTime)/1000000000;
        System.out.format("Took %f seconds to conduct %d searches\n",difference, limit);
        System.out.format("Average: %d searches / second\n", Math.round(limit/difference));


    }

}
