import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URL;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    final int WIDTH = 800, HEIGHT = 600;
    private Paddle paddle;
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Timer timer;
    private boolean leftPressed = false, rightPressed = false;
    private Image backgroundImage;

    private int hitCount = 0;
    private int score = 0;
    private static int lives = 30;
    private int fragmentSpawnCounter = 0;
    private Font gameFont = new Font("Arial", Font.BOLD, 20);
    private boolean scoreEnabled = true;
    private boolean isGameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("/res/classroom.jpg")).getImage();
    
        paddle = new Paddle(WIDTH / 2 - 50, HEIGHT - 30);
        balls.add(new RikaBall(WIDTH / 2, HEIGHT / 2));

        timer = new Timer(10, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        paddle.draw(g);

        for (Ball ball : balls) {
            ball.draw(g);
        }
        for (Fragment frag : fragments) {
            frag.draw(g);
        }

        if(scoreEnabled){
            g.setColor(Color.WHITE);
            g.setFont(gameFont);
            g.drawString("" + score, 20, 30);
            g.setColor(Color.GREEN);
            g.drawString("" + lives, 20, 60);
        }

        if (isGameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOverText = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            g.drawString(gameOverText, (WIDTH - textWidth) / 2, HEIGHT / 2);
        
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String restartText = "Press R to restart";
            int restartWidth = g.getFontMetrics().stringWidth(restartText);
            g.drawString(restartText, (WIDTH - restartWidth) / 2, HEIGHT / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (leftPressed) paddle.move(-10, WIDTH);
        if (rightPressed) paddle.move(10, WIDTH);

        ArrayList<Ball> newBalls = new ArrayList<>();
        Iterator<Ball> it = balls.iterator();

        fragmentSpawnCounter++;
        if (fragmentSpawnCounter >= 1000) {
            fragments.add(new Fragment((int)(Math.random() * (WIDTH - 30)), 0));
            fragmentSpawnCounter = 0;
        }

        while (it.hasNext()) {
            Ball ball = it.next();
            ball.move(WIDTH, HEIGHT);

            if (ball.getRect().intersects(paddle.getRect()) && ball.isReadyToBounce()) {
                ball.reverseY();
                ball.registerBounce();
                hitCount++;

                if (scoreEnabled) {
                    score += balls.size();
                }

                ball.playBounceSound();

                if (hitCount % 5 == 0) {
                    Ball newBall;
                
                    if (score > 2000 && Math.random() < 0.25) {
                        newBall = new HanyuuBall(WIDTH / 2, HEIGHT / 2); // 25% chance after 2000 score
                    }
                    else if (score > 1000 && Math.random() < 0.25) {
                        newBall = new SatokoBall(WIDTH / 2, HEIGHT / 2); // 25% chance after 1000 score
                    }
                    else {
                        newBall = new RikaBall(WIDTH / 2, HEIGHT / 2);
                    }
                
                    newBalls.add(newBall);
                }
            }

            if (ball.y > HEIGHT) {
                it.remove();
                if (scoreEnabled) {
                    lives--;
                }
            }

            if (score >= 2000) {
                backgroundImage = new ImageIcon(getClass().getResource("/res/saiguden.jpg")).getImage();
            } else if (score >= 1000) {
                backgroundImage = new ImageIcon(getClass().getResource("/res/school.jpg")).getImage();
            }
        }

        Iterator<Fragment> fragmentIt = fragments.iterator();
        while (fragmentIt.hasNext()) {
            Fragment frag = fragmentIt.next();
            frag.move(HEIGHT);

            if (frag.getRect().intersects(paddle.getRect())) {
                lives += 2;
                frag.collected = true;
                GamePanel.playSoundStatic("fragment.wav");
            }

            if (frag.collected) {
                fragmentIt.remove();
            }
        }

        balls.addAll(newBalls);
        repaint();

        if (lives <= 0 && scoreEnabled) {
            isGameOver = true;
            timer.stop();
        }
    }

    private void restartGame() {
        score = 0;
        lives = 30;
        hitCount = 0;
        isGameOver = false;
        backgroundImage = new ImageIcon(getClass().getResource("/res/classroom.jpg")).getImage();
        balls.clear();
        balls.add(new RikaBall(WIDTH / 2, HEIGHT / 2));
        timer.start();
    }

    public static void addLife() {
        lives++;
        GamePanel.playSoundStatic("fragment.wav");
    }

    public static void playSoundStatic(String soundFile) {
        try {
            URL url = GamePanel.class.getResource("/res/" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            scoreEnabled = !scoreEnabled;
            score = 0;
            lives = 30;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && isGameOver) {
            restartGame();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public void keyTyped(KeyEvent e) {}
}
