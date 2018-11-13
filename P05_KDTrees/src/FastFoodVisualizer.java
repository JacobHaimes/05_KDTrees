import java.awt.*;

/**
 * Loads and visualizes location data, and reports information on the closest
 * data point to mouse cursor
 */
public class FastFoodVisualizer {

    // delay in milliseconds (controls animation speed)
    private final static int DELAY = 10;

    private static final Color cText = new Color(64,64,64);

    private static void parseCSVLine(String line, String[] data) {
        boolean inQuote = false;
        int s = -1;
        int j = 0;
        for(int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if(c == ',' && !inQuote) {
                if(line.charAt(s+1) == '"' && line.charAt(i-1) == '"') {
                    data[j] = line.substring(s+2,i-1);
                } else {
                    data[j] = line.substring(s + 1, i);
                }
                j++;
                s = i;
                continue;
            }
            if(c=='"') {
                inQuote = !inQuote;
            }
        }
        int i = line.length();
        if(s < i) {
            if(line.charAt(s+1) == '"' && line.charAt(i-1) == '"') {
                data[j] = line.substring(s+2,i-1);
            } else {
                data[j] = line.substring(s + 1, i);
            }
        }
    }

    private static String[][] readCSV(String filename, int m) {
        String[] lines = (new In(filename)).readAllLines();
        int n = lines.length;
        String[][] a = new String[n][m];
        for(int i = 0; i < n; i++) parseCSVLine(lines[i], a[i]);
        return a;
    }

    private static void insertCSV(String filename, PointSearch<String[]> ps) {
        String[][] data = readCSV(filename, 4);
        for(int i = 0; i < data.length; i++) {
            double longitude = Double.parseDouble(data[i][0]);
            double latitude = Double.parseDouble(data[i][1]);
            String[] loc = {data[i][2], data[i][3]};
            ps.put(new Point(longitude, latitude), loc);
        }
    }

    public static void main(String[] args) {
        PointSearch<String[]> psBurgerKings = new PSKDTree<>();

        insertCSV("burgerking.csv", psBurgerKings);

        Visualizer.setScaling(psBurgerKings.min(), psBurgerKings.max());

        StdDraw.show(0);

        Mouse mouse = new Mouse(24, 36, 464, 464);

        while(true) {
            Point loc = Visualizer.xformInv(mouse.getLocation());

            Visualizer.clear();
            Visualizer.drawKDTree(psBurgerKings, false, false);
            Visualizer.drawNeighbors(psBurgerKings, loc, true, 0);

            StdDraw.setPenColor(cText);
            int i = 464;
            String[] value = psBurgerKings.getNearest(loc);
            if(value != null) {
                for (String l : value) {
                    StdDraw.text(256, i, l);
                    i -= 16;
                }
            }

            StdDraw.show(DELAY);
        }


    }
}
