import java.util.Random;

import javax.swing.*;

public class SatokoBall extends Ball {
    public SatokoBall(int x, int y) {
        super(x, y);
        ballImage = new ImageIcon(getClass().getResource("/res/satokoball.png")).getImage();
        ballSpawnImage = new ImageIcon(getClass().getResource("/res/satokoballspawn.png")).getImage();

        Random rand = new Random();
        speedX = rand.nextBoolean() ? 3 : -7;
        speedY = -7;
        GamePanel.playSoundStatic("nandesutte.wav");
    }

    @Override
    public void playBounceSound() {
        GamePanel.playSoundStatic("ohohoho.wav");
    }
}
