import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlappyBird extends JPanel implements ActionListener {

    private static final int WIDTH = 800, HEIGHT = 600;
    private int ticks, yMotion, score;
    private boolean gameOver, started;
    private ArrayList<Rectangle> pipes;
    private Random rand;

    public FlappyBird() {
        Timer timer = new Timer(20, this);
        rand = new Random();
        pipes = new ArrayList<>();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    jump();
                }
            }
        });
        setFocusable(true);
        setBackground(Color.cyan);
        setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
        resetGame();
        timer.start();
    }

    private void resetGame() {
        ticks = 0;
        yMotion = 0;
        score = 0;
        gameOver = false;
        started = false;
        pipes.clear();
        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);
    }

    private void jump() {
        if (gameOver) {
            resetGame();
        }
        if (!started) {
            started = true;
        }
        if (yMotion > 0) {
            yMotion = 0;
        }
        yMotion -= 10;
    }

    private void addPipe(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);
        if (start) {
            pipes.add(new Rectangle(WIDTH + width + pipes.size() * 300, HEIGHT - height - 120, width, height));
            pipes.add(new Rectangle(WIDTH + width + (pipes.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            pipes.add(new Rectangle(pipes.get(pipes.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    private void paintPipe(Graphics g, Rectangle pipe) {
        g.setColor(Color.green.darker());
        g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
    }

    private void paintScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 100));
        if (!started) {
            g.drawString("Press SPACE to start", 50, HEIGHT / 2 - 50);
        }
        if (gameOver) {
            g.drawString("Game Over", 100, HEIGHT / 2 - 50);
        }
        if (started && !gameOver) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Drawing the bird as an oval
        g.setColor(Color.red);
        g.fillOval(100, HEIGHT / 2 + yMotion, 20, 20);

        for (Rectangle pipe : pipes) {
            paintPipe(g, pipe);
        }
        paintScore(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;
        ticks++;
        if (started) {
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                if (pipe.x + pipe.width < 0) {
                    pipes.remove(pipe);
                    if (pipe.y == 0) {
                        addPipe(false);
                    }
                }
            }
            for (Rectangle pipe : pipes) {
                if (pipe.intersects(new Rectangle(100, HEIGHT / 2 + yMotion, 20, 20))) {
                    gameOver = true;
                }
            }
            if (HEIGHT / 2 + yMotion >= HEIGHT - 120 || HEIGHT / 2 + yMotion <= 0) {
                gameOver = true;
            }
            if (gameOver) {
                yMotion = 0;
            }
            if (started && !gameOver && ticks % 15 == 0) {
                score++;
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
