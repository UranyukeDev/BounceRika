import java.util.Random;

import javax.swing.*;

public class RikaBall extends Ball {
    public RikaBall(int x, int y) {
        super(x, y);
        ballImage = new ImageIcon(getClass().getResource("/res/rikaball.png")).getImage();
        ballSpawnImage = new ImageIcon(getClass().getResource("/res/rikaballspawn.png")).getImage();

        Random rand = new Random();
        speedX = rand.nextBoolean() ? 3 : -5;
        speedY = -5;
        GamePanel.playSoundStatic("nipah.wav");
    }

    @Override
    public void playBounceSound() {
        GamePanel.playSoundStatic("meep.wav");
    }
}
