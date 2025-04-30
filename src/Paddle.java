import java.awt.*;

public class Paddle {
    int x, y, width = 150, height = 15;

    public Paddle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int maxWidth) {
        x += dx;
        if (x < 0) x = 0;
        if (x + width > maxWidth) x = maxWidth - width;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 100, 0));
        g.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
