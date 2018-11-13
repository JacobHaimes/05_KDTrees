import java.awt.*;

/**
 * Created by jdenning on 11/2/15.
 */
public class Visualizer {

    // delay in milliseconds (controls animation speed)
    private final static int DELAY = 10;

    // colors for vizualization
    private static final Color cDot = new Color(64,64,64);      // point (small box)
    private static final Color cKNN = new Color(200,200,255);   // k nearest neighbors
    private static final Color cNN  = new Color(64,64,255);     // nearest neighbor
    private static final Color cLR  = new Color(255,128,128);   // left-right partition
    private static final Color cUD  = new Color(128,255,128);   // up-down partition
    private static final Color cBox = new Color(192,192,192);   // bounding box

    private static double offsetX = 0.0;
    private static double offsetY = 0.0;
    private static double scale = 1.0;

    private static double dotSize = 1.0;

    public static void setScale(double scale) { Visualizer.scale = scale; }
    public static double getScale() { return Visualizer.scale; }
    public static void setOffsetX(double offsetX) { Visualizer.offsetX = offsetX; };
    public static double getOffsetX() { return Visualizer.offsetX; }
    public static void setOffsetY(double offsetY) { Visualizer.offsetY = offsetY; };
    public static double getOffsetY() { return Visualizer.offsetY; }
    public static void setScaling(double minX, double minY, double maxX, double maxY) {
        offsetX = minX; offsetY = minY;
        double d = Math.max(maxX - minX, maxY - minY);
        if(d == 0.0) scale = 0.0;
        else scale = 1.0 / d;
    }
    public static void setScaling(Point min, Point max) {
        if(min == null || max == null) return;
        setScaling(min.x(), min.y(), max.x(), max.y());
    }

    public static double getDotSize() { return dotSize; }
    public static void setDotSize(double dotSize) { Visualizer.dotSize = dotSize; }

    public static void clear() {
        StdDraw.clear();
        StdDraw.setXscale(0, 512);
        StdDraw.setYscale(0, 512);
    }
    public static void drawNeighbors(PointSearch ps, Point loc, boolean drawClosest, int k) {
        if(ps.isEmpty()) return;

        Iterable<Point> nearest = ps.nearest(loc, k);
        if(nearest != null) {
            StdDraw.setPenColor(cKNN);
            for (Point p : nearest) drawLine(loc, p);
        }

        if(drawClosest) {
            Point closest = ps.nearest(loc);
            if (closest != null) {
                StdDraw.setPenColor(cNN);
                StdDraw.setPenRadius(0.005);
                drawLine(loc, closest);
                StdDraw.setPenRadius();
            }
        }
    }
    public static void drawKDTree(PointSearch ps, boolean drawPartitions, boolean drawBoundingBox) {
        if(ps.isEmpty()) return;

        if(drawBoundingBox) {
            StdDraw.setPenColor(cBox);
            drawBox(ps.min(), ps.max());
        }

        if(drawPartitions) {
            Iterable<Partition> partitions = ps.partitions();
            if(partitions != null) {
                StdDraw.setPenRadius(0.005);
                for (Partition p : partitions) {
                    if (p.dir() == Partition.Direction.LEFTRIGHT) StdDraw.setPenColor(cLR);
                    else StdDraw.setPenColor(cUD);
                    drawLine(p.p0(), p.p1());
                }
                StdDraw.setPenRadius();
            }
        }

        Iterable<Point> points = ps.points();
        if(points != null) {
            StdDraw.setPenColor(cDot);
            setDotSize(Math.max(1.0, 5.0 / Math.pow(ps.size() + 1, 0.5)));
            for (Point p : points) drawDot(p);
        }
    }

    private static double xformX(double x) { return (x - offsetX) * scale; }
    private static double xformY(double y) { return (y - offsetY) * scale; }
    private static double xformInvX(double x) { return x / scale + offsetX; }
    private static double xformInvY(double y) { return y / scale + offsetY; }
    public static Point xform(Point p) {
        return new Point(xformX(p.x()), xformY(p.y()));
    }
    public static Point xformInv(Point p) {
        return new Point(xformInvX(p.x()), xformInvY(p.y()));
    }


    public static void drawDot(double x, double y) {
        StdDraw.square(xformX(x) * 464 + 24, xformY(y) * 464 + 36, dotSize);
    }
    public static void drawDot(Point p) {
        drawDot(p.x(), p.y());
    }

    public static void drawLine(double x0, double y0, double x1, double y1) {
        StdDraw.line(
                xformX(x0) * 464 + 24, xformY(y0) * 464 + 36,
                xformX(x1) * 464 + 24, xformY(y1) * 464 + 36
        );
    }
    public static void drawLine(Point p0, Point p1) {
        drawLine(p0.x(), p0.y(), p1.x(), p1.y());
    }

    public static void drawBox(Point p0, Point p1) {
        drawLine(p0.x(), p0.y(), p1.x(), p0.y());
        drawLine(p1.x(), p0.y(), p1.x(), p1.y());
        drawLine(p1.x(), p1.y(), p0.x(), p1.y());
        drawLine(p0.x(), p1.y(), p0.x(), p0.y());
    }

}
