import javax.swing.*;
import java.awt.*;

public class Fragment {
    int x, y, size = 40;
    int speedY = 3;
    Image image;
    boolean collected = false;

    public Fragment(int x, int y) {
        this.x = x;
        this.y = y;
        image = new ImageIcon(getClass().getResource("/res/fragment.png")).getImage();
    }

    public void move(int height) {
        y += speedY;
        if (y > height) {
            collected = true;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }
}
