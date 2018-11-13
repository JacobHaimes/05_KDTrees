/**
 * Mouse is a helper class for detecting mouse presses (not just whether the mouse is pressed)
 * and for transforming the cursor location from window-space to program-space
 */
public class Mouse {
    private boolean pressed;
    private final int xOffset, yOffset, width, height;

    public Mouse(int xOffset, int yOffset, int width, int height) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
    }

    public boolean isPressed() {
        boolean p = StdDraw.mousePressed();
        boolean v = !pressed && p;
        pressed = p;
        return v;
    }

    public Point getLocation() {
        double mouseX = (StdDraw.mouseX() - (float)xOffset) / (float)width;
        double mouseY = (StdDraw.mouseY() - (float)yOffset) / (float)height;
        return new Point(mouseX, mouseY);
    }
}
