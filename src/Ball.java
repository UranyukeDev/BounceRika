import java.awt.*;

public class Ball {
    protected int x, y, size = 120;
    protected int speedX, speedY;
    protected Image ballImage;
    protected Image ballSpawnImage;
    private long spawnTime;
    //anti multiple collisions
    protected boolean canBounce = true;
    protected long lastBounceTime = 0;
    protected final long bounceCooldown = 1000;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;

        spawnTime = System.currentTimeMillis();
    }

    public void move(int width, int height) {
        x += speedX;
        y += speedY;

        if (x <= 0 || x + size >= width) speedX *= -1;
        if (y <= 0) speedY *= -1;

        if(!canBounce && System.currentTimeMillis() - lastBounceTime > bounceCooldown){
            canBounce = true;
        }
    }

    public void reverseY() {
        speedY *= -1;
    }

    public void draw(Graphics g) {
        long elapsed = System.currentTimeMillis() - spawnTime;

        if (elapsed < 1000) {
            g.drawImage(ballSpawnImage, x, y, size, size, null);
        } else {
            g.drawImage(ballImage, x, y, size, size, null);
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isReadyToBounce(){
        return canBounce || (System.currentTimeMillis() - lastBounceTime > bounceCooldown);
    }

    public void registerBounce(){
        canBounce = false;
        lastBounceTime = System.currentTimeMillis();
    }

    public void playBounceSound() {
        
    }
}
