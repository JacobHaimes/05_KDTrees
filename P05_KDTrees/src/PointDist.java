/**
 * PointDist is a simple tuple of a Point and a dist.
 * This class is helpful for storing, passing, or returning the pair.
 */
public final class PointDist implements Comparable<PointDist> {
    private final Point p;
    private final double d;

    public PointDist(Point p, double d) {
        this.p = p;
        this.d = d;
    }

    public Point p() { return p; }

    public double d() { return d; }

    public int compareTo(PointDist that) {
        return Double.compare(this.d, that.d);
    }
}
