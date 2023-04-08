package main;

import javax.swing.*;

public class Main {
    public static JFrame window;
    public static void main (String [] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Mahou Shoujo Monogatari - Demo");
        window.setUndecorated(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon img = new ImageIcon("src/Prototype Sprites/FINALMAYBE.png");
        window.setIconImage(img.getImage());

        final GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}