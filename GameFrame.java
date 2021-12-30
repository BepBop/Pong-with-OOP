import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

    GamePanel panel;

    GameFrame() {
        panel = new GamePanel();
        setUndecorated(true);
        add(panel);
        setTitle("Pong Game");
        setResizable(false);
        setBackground(Color.BLACK);
        pack();
        setVisible(true);
        setLocationRelativeTo(null); // appear in middle of screen
    }
}
