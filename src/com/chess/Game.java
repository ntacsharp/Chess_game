package com.chess;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.chess.engine.minigame.GUI.ColorList;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.GUI.ImageList;

public class Game {
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 1366_768
    public static ImageList imageList;

    public static void main(String[] args) {
        // GameState gameState = new GameState(PieceType.BABARIAN);

        // System.out.println(gameState.getChessBoard());

        // gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // gameFrame.setUndecorated(true);
        try {
            imageList = new ImageList();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        JFrame gameFrame = new JFrame();
        gameFrame.setSize(screenSize);
        gameFrame.setUndecorated(true);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setTitle("My Chess Game");
        GamePanel gamePanel = new GamePanel();
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setVisible(true);
        gameFrame.setBackground(ColorList.darkTileColor);
        gameFrame.setFocusable(true);
        gamePanel.startGameThread();
    }
}
