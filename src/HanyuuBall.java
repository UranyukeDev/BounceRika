import java.util.Random;

import javax.swing.*;

public class HanyuuBall extends Ball {
    public HanyuuBall(int x, int y) {
        super(x, y);
        ballImage = new ImageIcon(getClass().getResource("/res/hanyuuball.png")).getImage();
        ballSpawnImage = new ImageIcon(getClass().getResource("/res/hanyuuballspawn.png")).getImage();

        Random rand = new Random();
        speedX = rand.nextBoolean() ? 3 : -5;
        speedY = -5;
        GamePanel.playSoundStatic("auauau.wav");
    }

    @Override
    public void playBounceSound() {
        GamePanel.playSoundStatic("auauau.wav");
    }

    @Override
    public void registerBounce() {
        super.registerBounce();
        // 10% chance to give extra life
        if (Math.random() < 0.1) {
            GamePanel.addLife();
        }
    }
}
