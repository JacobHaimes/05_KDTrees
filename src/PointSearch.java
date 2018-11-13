/**
 * This interface defines the basic API for a 2D symbol table
 * where the keys are Points and the values are the generic
 * type Value
 */
public interface PointSearch<Value> {
    boolean isEmpty();
    int size();

    Point min();
    Point max();

    void put(Point p, Value v);
    Iterable<Point> points();
    boolean contains(Point p);
    Value get(Point p);
    Value getNearest(Point p);

    Point nearest(Point p);
    Iterable<Point> nearest(Point p, int k);

    Iterable<Partition> partitions();
}
