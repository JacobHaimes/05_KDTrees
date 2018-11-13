import java.util.Comparator;

/**
 * Point is a simple tuple of doubles with some handy functions.  Point is useful for
 * storing and computing 2D points.
 */
public final class Point implements Comparable<Point> {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // generates a Point with location chosen uniformly at random in the unit square [0,1]^2
    public static Point uniform() {
        return new Point(StdRandom.uniform(), StdRandom.uniform());
    }

    // generates a Point with location chosen with gaussian distribution in unit square [0,1]^2
    // with mean of 0.5 and stddev of 0.12
    public static Point gaussian() {
        double x = -1, y = -1;
        // do not return an x,y that is outside the unit square
        while(x < 0 || y < 0 || x > 1 || y > 1) {
            x = StdRandom.gaussian(0.5, 0.12);
            y = StdRandom.gaussian(0.5, 0.12);
        }
        return new Point(x, y);
    }


    /**
     * the functions below can be used to find the min/max x/y which
     * can be used to find the bounding box of points
     */

    // returns a point with minimum component values
    public static Point min(Point p0, Point p1) {
        return new Point(Math.min(p0.x, p1.x), Math.min(p0.y, p1.y));
    }

    // returns a point with maximum component values
    public static Point max(Point p0, Point p1) {
        return new Point(Math.max(p0.x, p1.x), Math.max(p0.y, p1.y));
    }


    /**
     * use the following functions to compare Points in different ways.
     * Points are Comparable, so they know how to compareTo one another,
     * but the other functions return Comparator objects which can compare
     * Points in different ways.
     */

    // compares this to that, first by y component then by x
    public int compareTo(Point that) {
        if(this.y < that.y) return -1;
        if(this.y > that.y) return +1;
        if(this.x < that.x) return -1;
        if(this.x > that.x) return +1;
        return 0;
    }

    // do the components of this equal the components of that?
    public boolean equals(Object that) {
        if(this == that) return true;
        if(that == null) return false;
        if(that.getClass() != this.getClass()) return false;
        Point thatp = (Point)that;
        if(Double.compare(this.x, thatp.x) != 0) return false;
        if(Double.compare(this.y, thatp.y) != 0) return false;
        return true;
    }

    // returns a Comparator, comparing x first then y
    public static Comparator<Point> xyComparator() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point o0, Point o1) {
                if(o0.x < o1.x) return -1;
                if(o0.x > o1.x) return +1;
                if(o0.y < o1.y) return -1;
                if(o0.y > o1.y) return +1;
                return 0;
            }
        };
    }

    // returns a Comparator, comparing y fist then x
    public static Comparator<Point> yxComparator() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point o0, Point o1) {
                if(o0.y < o1.y) return -1;
                if(o0.y > o1.y) return +1;
                if(o0.x < o1.x) return -1;
                if(o0.x > o1.x) return +1;
                return 0;
            }
        };
    }

    // returns a Comparator, comparing the polar radius (dist from (0,0))
    public static Comparator<Point> polarRadiusComparator() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point o0, Point o1) {
                double d0 = (o0.x*o0.x + o0.y*o0.y);
                double d1 = (o1.x*o1.x + o1.y*o1.y);
                if(d0 < d1) return -1;
                if(d0 > d1) return +1;
                return 0;
            }
        };
    }

    // returns a Comparator for this, comparing dist between first given
    // point and this and dist between second given point and this
    public Comparator<Point> distanceToComparator() {
        return new Comparator<Point>() {
            private final double ox = x;
            private final double oy = y;
            @Override
            public int compare(Point o0, Point o1) {
                double d0 = (o0.x-ox)*(o0.x-ox) + (o0.y-oy)*(o0.y-oy);
                double d1 = (o1.x-ox)*(o1.x-ox) + (o1.y-oy)*(o1.y-oy);
                if(d0 < d1) return -1;
                if(d0 > d1) return +1;
                return 0;
            }
        };
    }


    /**
     * getters and setters
     */

    // getters of x and y
    public double x() { return x; }
    public double y() { return y; }

    // getter of either x or y depending on dir
    // dir = LEFTRIGHT => returns x
    // dir = DOWNUP    => returns y
    public double xy(Partition.Direction dir) {
        if(dir == Partition.Direction.LEFTRIGHT) return x;
        else return y;
    }


    /**
     * functions for computing distances
     */

    // computes distance^2 between this point and (x,y)
    public double distSquared(double x, double y) {
        return (this.x-x)*(this.x-x) + (this.y-y)*(this.y-y);
    }

    // computes distance^2 between this point and another (p)
    public double distSquared(Point p) {
        return distSquared(p.x, p.y);
    }

    // computes distance between this point and (x,y)
    public double dist(double x, double y) {
        return Math.sqrt(distSquared(x, y));
    }

    // computes distance between this point and another (p)
    public double dist(Point p) {
        return dist(p.x, p.y);
    }


    /**
     * misc functions
     */

    // returns a new point that has values added to components
    public Point add(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    // returns whether point is within given min and max bounds
    public boolean isInBounds(double x0, double y0, double x1, double y1) {
        if(Double.compare(x,x0) <= 0) return false;
        if(Double.compare(y,y0) <= 0) return false;
        if(Double.compare(x1,x) <= 0) return false;
        if(Double.compare(y1,y) <= 0) return false;
        return true;
    }

    // produces a String representation of Point
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
