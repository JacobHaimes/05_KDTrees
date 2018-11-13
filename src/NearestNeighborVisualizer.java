import java.awt.event.KeyEvent;

/**
 * Visualizes nearest neighbors to mouse cursor and partitioning data
 */
public class NearestNeighborVisualizer {

    // number of dots to add at start
    private final static int nGaussian = 100;
    private final static int nUniform  = 100;

    // delay in milliseconds (controls animation speed)
    private final static int DELAY = 10;

    // creates a new PointSearch data structure
    // note: you can swap between PSKDTree and PSBruteForce to debug your code
    private static PointSearch<Character> createNewPS() {
        return new PSBruteForce<>();
    }

    public static void main(String[] args) {
        PointSearch<Character> ps = createNewPS();

        // add some random points
        for(int i = 0; i < nGaussian; i++) ps.put(Point.gaussian(), 'g');
        for(int i = 0; i < nUniform; i++) ps.put(Point.uniform(), 'u');

        // add points to match assignment write-up
        /*
        ps.put(new Point(0.8, 0.9), 'A');
        ps.put(new Point(0.5, 0.4), 'B');
        ps.put(new Point(0.2, 0.6), 'C');
        ps.put(new Point(0.3, 0.1), 'D');
        ps.put(new Point(0.9, 0.4), 'E');
        */

        StdDraw.show(0);

        int k = 10;
        boolean drawClosest = true;
        boolean drawPartitions = false;
        boolean drawBoundingBox = false;

        KeyPress keyP    = new KeyPress(KeyEvent.VK_P);
        KeyPress keyUp   = new KeyPress(KeyEvent.VK_UP);
        KeyPress keyDown = new KeyPress(KeyEvent.VK_DOWN);
        KeyPress keyC    = new KeyPress(KeyEvent.VK_C);
        KeyPress keyN    = new KeyPress(KeyEvent.VK_N);
        KeyPress keyB    = new KeyPress(KeyEvent.VK_B);
        Mouse    mouse   = new Mouse(24, 36, 464, 464);

        while(true) {
            Point loc = mouse.getLocation();

            if(keyC.isPressed()) ps = createNewPS();
            if(keyUp.isPressed()) k++;
            if(keyDown.isPressed() && k > 0) k--;
            if(keyN.isPressed()) drawClosest = !drawClosest;
            if(keyP.isPressed()) drawPartitions = !drawPartitions;
            if(keyB.isPressed()) drawBoundingBox = !drawBoundingBox;
            if(mouse.isPressed()) ps.put(loc, 'm');

            Visualizer.clear();
            Visualizer.drawKDTree(ps, drawPartitions, drawBoundingBox);
            Visualizer.drawNeighbors(ps, loc, drawClosest, k);

            StdDraw.show(DELAY);
        }
    }
}
