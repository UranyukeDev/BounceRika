import javax.swing.*;

public class RenaBall extends Ball {

    public RenaBall(int x, int y) {
        super(x, y);
        ballImage = new ImageIcon(getClass().getResource("/res/renaball.png")).getImage();
        ballSpawnImage = new ImageIcon(getClass().getResource("/res/renaballspawn.png")).getImage();
        speedX = randomSpeed();
        speedY = -3;
        GamePanel.playSoundStatic("omochikaeri.wav");
    }

    @Override
    public void playBounceSound() {
        GamePanel.playSoundStatic("hau.wav");
    }

    private int randomSpeed() {
        int speed = (int)(Math.random() * 5) + 2;
        if (Math.random() < 0.5) speed *= -1;
        return speed;
    }

    @Override
    public void move(int width, int height) {
        x += speedX;
        y += speedY;

        if (x <= 0 || x + size >= width) {
            if (Math.random() < 0.5) {
                speedX *= -1;
            }
            if (x <= 0) x = 1;
            if (x + size >= width) x = width - size - 1;
        }

        if (y <= 0) {
            if (Math.random() < 0.5) {
                speedY *= -1;
            }
            y = 1;
        }
    }

    @Override
    public void reverseY() {
        if (Math.random() < 0.5) {
            speedY *= -1;
        }
        if (speedY >= 0) {
            speedY = -((int)(Math.random() * 5) + 2);
        }
    }
}
