package com.chess.engine.minigame.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;
import com.chess.menu.StartMenu;
import com.chess.Game;

public class GamePanel extends JPanel implements Runnable {

    private int FPS = 60;

    // private final JPanel gamePanel;
    // private final JPanel settingPanel;
    private GameState gameState;
    private Card chosenCard;

    public BoardPanel boardPanel;
    public SouthPanel southPanel;
    public NorthPanel northPanel;
    public WestPanel westPanel;
    public EastPanel eastPanel;
    
    private Thread gameThread;

    public GamePanel() {
        this.gameState = new GameState(PieceType.BABARIAN);
        this.setPreferredSize(Game.screenSize);
        this.setLayout(new BorderLayout());
        this.setBackground(ColorList.darkTileColor);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.boardPanel = new BoardPanel(this);
        this.northPanel = new NorthPanel(this);
        this.southPanel = new SouthPanel(this);
        this.eastPanel = new EastPanel(this);
        this.westPanel = new WestPanel(this);
        this.add(northPanel, BorderLayout.NORTH);
        this.add(westPanel, BorderLayout.WEST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(boardPanel, BorderLayout.CENTER);
        validate();
        // this.addMouseListener(new MouseListener() {
        // @Override
        // public void mouseClicked(MouseEvent e) {
        // }
        // @Override
        // public void mousePressed(MouseEvent e) {
        // }
        // @Override
        // public void mouseReleased(MouseEvent e) {
        // }
        // @Override
        // public void mouseEntered(MouseEvent e) {
        // }
        // @Override
        // public void mouseExited(MouseEvent e) {
        // }
        // });
        // this.addKeyListener(new KeyListener() {

        // @Override
        // public void keyTyped(KeyEvent e) {
        // }

        // @Override
        // public void keyPressed(KeyEvent e) {
        // }

        // @Override
        // public void keyReleased(KeyEvent e) {
        // if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        // setting();
        // }
        // }

        // });
        // this.gamePanel = new JPanel(new BorderLayout());
        // this.settingPanel = new SettingPanel();
        // setUpGamePanel();
        // this.add(settingPanel);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timeBetweenDraw = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + timeBetweenDraw;
        while (gameThread != null) {
            update();
            this.repaint();
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime /= 1000000;
            if (remainingTime < 0) {
                remainingTime = 0;
            }
            try {
                Thread.sleep((long) remainingTime);
                nextDrawTime += timeBetweenDraw;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        this.boardPanel.update();
        this.southPanel.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        boardPanel.draw(g2);
        northPanel.draw(g2);
        southPanel.draw(g2);
        eastPanel.draw(g2);
        westPanel.draw(g2);
        g2.dispose();
        // BufferedImage image;
        // try {
        // image = ImageIO.read(new File("art/pieces/WP.png"));
        //
        // g2.setColor(ColorList.lightGreen);
        // g2.drawImage(image, 100, 100, 100, 100, null);
        // g2.dispose();
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

    public GameState getGameState() {
        return gameState;
    }

    public Card getChosenCard(){
        return this.chosenCard;
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
    }

    

    // public void setting() {
    // CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
    // cardLayout.next(frame.getContentPane());
    // }

    // private void setUpGamePanel() {
    // this.gamePanel.removeAll();
    // this.gamePanel.setSize(screenSize);
    // this.gameState = new GameState(PieceType.BABARIAN);
    // this.boardPanel = new BoardPanel(this);
    // this.southPanel = new SouthPanel(this);
    // this.northPanel = new NorthPanel(this);
    // this.westPanel = new WestPanel(this);
    // this.eastPanel = new EastPanel();
    // this.gamePanel.add(boardPanel, BorderLayout.CENTER);
    // this.gamePanel.add(southPanel, BorderLayout.SOUTH);
    // this.gamePanel.add(northPanel, BorderLayout.NORTH);
    // this.gamePanel.add(eastPanel, BorderLayout.EAST);
    // this.gamePanel.add(westPanel, BorderLayout.WEST);
    // frame.add(gamePanel);
    // boardPanel.playerTurn();
    // }

    // public class SettingPanel extends JPanel {
    // SettingPanel() {
    // super(new GridLayout(7, 1, 0, 0));
    // setBackground(transparentBg);
    // setSize(screenSize);
    // setDoubleBuffered(true);
    // JPanel masterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // masterPanel.setBackground(transparentBg);
    // add(masterPanel);
    // JPanel musicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // musicPanel.setBackground(transparentBg);
    // add(musicPanel);
    // JPanel soundPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // soundPanel.setBackground(transparentBg);
    // add(soundPanel);
    // JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // keyPanel.setBackground(transparentBg);
    // add(keyPanel);
    // JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // mainPanel.setBackground(transparentBg);
    // JLabel mainLabel = new JLabel("Exit to Mainmenu");
    // mainLabel.setHorizontalAlignment(JLabel.CENTER);
    // mainLabel.setFont(new Font("Arial", Font.BOLD, 36));
    // mainLabel.setForeground(chosenBackground);
    // mainLabel.setBackground(transparentBg);
    // mainLabel.addMouseListener(new MouseListener() {
    // @Override
    // public void mouseClicked(MouseEvent e) {
    // StartMenu.main(null);
    // frame.dispose();
    // }

    // @Override
    // public void mousePressed(MouseEvent e) {
    // }

    // @Override
    // public void mouseReleased(MouseEvent e) {
    // }

    // @Override
    // public void mouseEntered(MouseEvent e) {
    // }

    // @Override
    // public void mouseExited(MouseEvent e) {
    // }

    // });
    // mainPanel.add(mainLabel);
    // add(mainPanel);
    // JPanel abandonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // abandonPanel.setBackground(transparentBg);
    // JLabel abandonLabel = new JLabel("Abandon run");
    // abandonLabel.setHorizontalAlignment(JLabel.CENTER);
    // abandonLabel.setFont(new Font("Arial", Font.BOLD, 36));
    // abandonLabel.setForeground(chosenBackground);
    // abandonLabel.setBackground(transparentBg);
    // abandonLabel.addMouseListener(new MouseListener() {
    // @Override
    // public void mouseClicked(MouseEvent e) {
    // System.out.println("Abandon");
    // }

    // @Override
    // public void mousePressed(MouseEvent e) {
    // }

    // @Override
    // public void mouseReleased(MouseEvent e) {
    // }

    // @Override
    // public void mouseEntered(MouseEvent e) {
    // }

    // @Override
    // public void mouseExited(MouseEvent e) {
    // }

    // });
    // abandonPanel.add(abandonLabel);
    // add(abandonPanel);
    // JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
    // backPanel.setBackground(transparentBg);
    // JLabel backLabel = new JLabel("Back");
    // backLabel.setHorizontalAlignment(JLabel.CENTER);
    // backLabel.setFont(new Font("Arial", Font.BOLD, 36));
    // backLabel.setForeground(chosenBackground);
    // backLabel.setBackground(transparentBg);
    // backLabel.addMouseListener(new MouseListener() {
    // @Override
    // public void mouseClicked(MouseEvent e) {
    // setting();
    // }

    // @Override
    // public void mousePressed(MouseEvent e) {
    // }

    // @Override
    // public void mouseReleased(MouseEvent e) {
    // }

    // @Override
    // public void mouseEntered(MouseEvent e) {
    // }

    // @Override
    // public void mouseExited(MouseEvent e) {
    // }

    // });
    // backPanel.add(backLabel);
    // add(backPanel);
    // validate();
    // }
    // }

}
