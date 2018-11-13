/**
 * KeyPress is a helper class for detecting key presses (not just whether the key is pressed)
 */
public class KeyPress {
    private final int key;
    private boolean pressed;

    public KeyPress(int key) {
        this.key = key;
    }

    public boolean isDown() {
        return StdDraw.isKeyPressed(this.key);
    }

    public boolean isUp() {
        return !isDown();
    }

    public boolean isPressed() {
        boolean p = StdDraw.isKeyPressed(this.key);
        boolean v = !pressed && p;
        pressed = p;
        return v;
    }
}
