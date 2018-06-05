import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

public class Game extends JPanel implements KeyListener {
    public static int BLOCK_SIZE = 25;
    public static int WINDOW_WIDTH = 24 * BLOCK_SIZE;
    public static int WINDOW_HEIGHT = 24 * BLOCK_SIZE;
    private Font font = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private Font overlayFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
    private Tetromino currentPiece = null;
    private Thread thread;
    private boolean running = true;
    private boolean paused = false;
    private int score = 0;
    private boolean board[][] = new boolean[10][20];

    Game() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // initialize board
        for (int i = 0; i < 10; ++i) {
            for (int ii = 0; ii < 20; ++ii) {
                board[i][ii] = false;
            }
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    if (!paused) {
                        boolean convert = false; // convert current piece to blocks
                        if (currentPiece == null) {
                            currentPiece = new Tetromino();
                        } else {
                            if (checkCollision()) {
                                if (currentPiece.getPosition().y == 0) {
                                    running = false;
                                }

                                convert = true;
                            } else {
                                convert = !currentPiece.fall();
                            }
                            if (convert) {
                                for (Point p : currentPiece.getPiece()) {
                                    board[currentPiece.getPosition().x + p.x][currentPiece.getPosition().y + p.y] = true;
                                }
                                currentPiece = new Tetromino();
                                score += 1;

                                // remove full lines
                                for (int y = 0; y < 20; ++y) {
                                    int count = 0;
                                    for (int x = 0; x < 10; ++x) {
                                        if (board[x][y]) {
                                            count += 1;
                                        }
                                    }

                                    if (count == 10) {
                                        removeLine(y);
                                        y -= 1;
                                    }
                                }
                            }
                        }
                    }
                    repaint();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (Exception e) {};
                }
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private void removeLine(int line) {
        for (int x = 0; x < 10; ++x) {
            board[x][line] = false;
        }

        for (int y = line; y > 0; --y) {
            for (int x = 0; x < 10; ++x) {
                board[x][y] = board[x][y - 1];
            }
        }

        score += 5;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(font);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawRect(WINDOW_WIDTH / 2 - BLOCK_SIZE * 5, WINDOW_HEIGHT / 2 - BLOCK_SIZE * 10, BLOCK_SIZE * 10, BLOCK_SIZE * 20);

        g.drawString("SCORE: " + score, BLOCK_SIZE, BLOCK_SIZE + 12);

        // draw board
        for (int i = 0; i < 10; ++i) {
            for (int ii = 0; ii < 20; ++ii) {
                if (board[i][ii]) {
                    g.fillRect(WINDOW_WIDTH / 2 - BLOCK_SIZE * 5 + i * BLOCK_SIZE,
                        WINDOW_HEIGHT / 2 - BLOCK_SIZE * 10 + ii * BLOCK_SIZE,
                        BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // draw current tetromino
        if (currentPiece != null) {
            for (Point p : currentPiece.getPiece()) {
                g.fillRect(WINDOW_WIDTH / 2 - BLOCK_SIZE * 5 + currentPiece.getPosition().x * BLOCK_SIZE + p.x * BLOCK_SIZE,
                    WINDOW_HEIGHT / 2 - BLOCK_SIZE * 10 + currentPiece.getPosition().y * BLOCK_SIZE + p.y * BLOCK_SIZE,
                    BLOCK_SIZE, BLOCK_SIZE);
            }
        }

        if (paused || !running) {
            g.setColor(new Color(0, 0, 0, 191));
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(overlayFont);
            g.drawString(running ? "PAUSED" : "GAME OVER", running ? 243 : 213, 300);
            if (!running) {
                g.setFont(font);
                g.drawString("FINAL SCORE: " + score, 215, 323);
            }
        }
    }

    private boolean checkCollision() {
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 20; ++y) {
                if (board[x][y]) {
                    for (Point p : currentPiece.getPiece()) {
                        if (p.x + currentPiece.getPosition().x == x && p.y + currentPiece.getPosition().y + 1 == y) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                currentPiece.move(-1, board);
                break;
            case KeyEvent.VK_RIGHT:
                currentPiece.move(1, board);
                break;
            case KeyEvent.VK_UP:
                currentPiece.rotate();
                break;
            case KeyEvent.VK_DOWN:
                while (!checkCollision()) {
                    if (!currentPiece.fall()) {
                        break;
                    }
                }
                break;
            case KeyEvent.VK_P:
                paused = !paused;
                break;
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
