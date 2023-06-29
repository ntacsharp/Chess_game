package com.chess;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.GUI.ImageList;
import com.chess.engine.minigame.GUI.Sound;
import com.chess.menu.StartMenu;

public class Game {
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 1366_768
    public static ImageList imageList;
    public static Sound sound = new Sound();
    

    private static JFrame gameFrame;

    public static void exitToMenu() {
        StartMenu.main(null);
        gameFrame.dispose();
    }

    public static void play() {
        try {
            imageList = new ImageList();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        gameFrame = new JFrame();
        gameFrame.setLayout(new CardLayout());
        gameFrame.setSize(screenSize);
        gameFrame.setUndecorated(true);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setTitle("My Chess Game");
        GamePanel gamePanel = new GamePanel();
        gameFrame.add(gamePanel);
        gameFrame.setVisible(true);
        gameFrame.setFocusable(true);
        gameFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE) {                       
                    if (gamePanel.isPausing())
                        gamePanel.setPausing(false);
                    else{
                        gamePanel.setPausing(true);
                    }
                    gameFrame.getGlassPane().setVisible(gamePanel.isPausing());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });
        gamePanel.startGameThread();
        gameFrame.validate();
    }
}
