/**
 * Partition is a simple class that stores two points and a direction,
 * where the two points make up a partition along the given direction.
 */
public final class Partition {
    // enumeration of all partition directions: Left-Right and Up-Down
    public enum Direction {
        LEFTRIGHT,
        DOWNUP
    }

    // returns the next direction in the enumeration
    public static Direction nextDirection(Direction dir) {
        if(dir == Direction.LEFTRIGHT) return Direction.DOWNUP;
        return Direction.LEFTRIGHT;
    }

    private final Point p0;
    private final Point p1;
    private final Direction dir;

    public Partition(Point p0, Point p1, Direction dir) {
        this.p0 = p0;
        this.p1 = p1;
        this.dir = dir;
    }

    public Partition(double x0, double y0, double x1, double y1, Direction dir) {
        this.p0 = new Point(x0, y0);
        this.p1 = new Point(x1, y1);
        this.dir = dir;
    }

    public Point p0() { return p0; }

    public Point p1() { return p1; }

    public Direction dir() { return dir; }
}
