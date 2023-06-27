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
import com.chess.engine.minigame.GUI.Playing.BoardPanel;
import com.chess.engine.minigame.GUI.Playing.EastPanel;
import com.chess.engine.minigame.GUI.Playing.NorthPanel;
import com.chess.engine.minigame.GUI.Playing.SouthPanel;
import com.chess.engine.minigame.GUI.Playing.WestPanel;
import com.chess.engine.minigame.GUI.Shopping.ShoppingBoardPanel;
import com.chess.engine.minigame.GUI.Shopping.ShoppingEastPanel;
import com.chess.engine.minigame.GUI.Shopping.ShoppingNorthPanel;
import com.chess.engine.minigame.GUI.Shopping.ShoppingSouthPanel;
import com.chess.engine.minigame.GUI.Shopping.ShoppingWestPanel;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;
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

    public ShoppingBoardPanel shoppingBoardPanel;
    public ShoppingEastPanel shoppingEastPanel;
    public ShoppingNorthPanel shoppingNorthPanel;
    public ShoppingSouthPanel shoppingSouthPanel;
    public ShoppingWestPanel shoppingWestPanel;

    private Thread gameThread;

    private double timeToChangeState;
    private int state = 0;
    // 0 - playing
    // 1 - displaying "victor-e"
    // 2 - shopping
    // 3 - display floor

    public GamePanel() {
        this.gameState = new GameState(PieceType.BABARIAN);
        this.setPreferredSize(Game.screenSize);
        this.setLayout(new BorderLayout());
        //this.setBackground(ColorList.darkTileColor);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        validate();
        setUpPlay();
        // setUpShopping();
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

    private void setUpPlay() {
        this.removeAll();
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
        revalidate();
    }

    private void setUpShopping() {
        this.removeAll();
        gameState.getDeck().generateShoppingRound();
        this.shoppingBoardPanel = new ShoppingBoardPanel(this);
        this.shoppingEastPanel = new ShoppingEastPanel(this);
        this.shoppingNorthPanel = new ShoppingNorthPanel(this);
        this.shoppingSouthPanel = new ShoppingSouthPanel(this);
        this.shoppingWestPanel = new ShoppingWestPanel(this);
        this.add(shoppingNorthPanel, BorderLayout.NORTH);
        this.add(shoppingWestPanel, BorderLayout.WEST);
        this.add(shoppingSouthPanel, BorderLayout.SOUTH);
        this.add(shoppingEastPanel, BorderLayout.EAST);
        this.add(shoppingBoardPanel, BorderLayout.CENTER);
        revalidate();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        if (gameState.getCurrentHealth() <= 0) {
            this.state = 4;
            timeToChangeState = System.nanoTime() + 1500000000;
        }
        if (System.nanoTime() >= timeToChangeState) {
            if (this.state == 1) {
                if (this.gameState.getFloor() < 7) {
                    this.state = 2;
                    setUpShopping();
                } else {
                    gameThread = null;
                    Game.exitToMenu();
                }
            } else if (this.state == 3) {
                this.state = 0;
                gameState.createNewFloor();
                setUpPlay();
            } else if (this.state == 4) {
                gameThread = null;
                Game.exitToMenu();
            }
        }
        if (state == 0 || state == 1) {
            this.boardPanel.update();
            this.southPanel.update();
        } else if (state == 2) {
            this.shoppingBoardPanel.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ColorList.darkTileColor);
        g2.fillRect(0, 0, (int)Game.screenSize.getWidth(), (int)Game.screenSize.getHeight());
        if (state == 0 || state == 1) {
            boardPanel.draw(g2);
            northPanel.draw(g2);
            southPanel.draw(g2);
            eastPanel.draw(g2);
            westPanel.draw(g2);
        } else if (state == 2) {
            shoppingBoardPanel.draw(g2);
            shoppingNorthPanel.draw(g2);
            shoppingSouthPanel.draw(g2);
            shoppingEastPanel.draw(g2);
            shoppingWestPanel.draw(g2);
        } else if (state == 3) {
            g2.setColor(ColorList.chosenBackground);
            g2.setFont(new Font("Arial", Font.BOLD, 80));
            g2.drawString("Floor " + (gameState.getFloor() + 1) + " on 7", (int) Game.screenSize.getWidth() / 3,
                    (int) Game.screenSize.getHeight() / 2);
        }
        g2.dispose();
    }

    public GameState getGameState() {
        return gameState;
    }

    public Card getChosenCard() {
        return this.chosenCard;
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTimeToChangeState(double timeToChangeState) {
        this.timeToChangeState = timeToChangeState;
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
