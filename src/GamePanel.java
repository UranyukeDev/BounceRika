import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.URL;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    final int WIDTH = 800, HEIGHT = 600;
    Paddle paddle;
    ArrayList<Ball> balls = new ArrayList<>();
    Timer timer;
    boolean leftPressed = false, rightPressed = false;
    private Image backgroundImage;

    private int hitCount = 0;
    private int score = 0;
    private int lives = 30;
    private Font gameFont = new Font("Arial", Font.BOLD, 20);
    private boolean scoreEnabled = true;

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

        if(scoreEnabled){
            g.setColor(Color.WHITE);
            g.setFont(gameFont);
            g.drawString("" + score, 20, 30);
            g.setColor(Color.GREEN);
            g.drawString("" + lives, 20, 60);
        }

        if (lives <= 0){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", WIDTH / 2 - 120, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (leftPressed) paddle.move(-10, WIDTH);
        if (rightPressed) paddle.move(10, WIDTH);

        ArrayList<Ball> newBalls = new ArrayList<>();
        Iterator<Ball> it = balls.iterator();

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

                    if (score > 1000 && Math.random() < 0.4) {
                        newBall = new SatokoBall(WIDTH / 2, HEIGHT / 2); // 40% chance
                    } else {
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

        balls.addAll(newBalls);
        repaint();

        if (lives <= 0 && scoreEnabled) {
            timer.stop();
            return;
        }
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
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public void keyTyped(KeyEvent e) {}
}
