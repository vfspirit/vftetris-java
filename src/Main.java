import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }

    Main() {
        Game game = new Game();
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(game);
        addKeyListener(game);
        pack();
        setResizable(false);
        setVisible(true);
    }
}
