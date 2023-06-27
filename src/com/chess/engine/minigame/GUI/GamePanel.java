package com.chess.engine.minigame.GUI;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

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
    private float alpha = 0.0f;
    private int dir = 1;
    private boolean playedSound = false;
    // 0 - playing
    // 1 - displaying "victory"
    // 2 - shopping
    // 3 - display floor
    // 4 - Defeated
    // 5 - Win

    public GamePanel() {
        this.gameState = new GameState(PieceType.BABARIAN);
        this.setPreferredSize(Game.screenSize);
        this.setLayout(new BorderLayout());
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        Game.sound.playBG();
        validate();
        setUpPlay();
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
        if (System.nanoTime() >= timeToChangeState) {
            if (gameState.getCurrentHealth() <= 0) {
                this.state = 4;
            }
            if (this.state == 1) {
                if (this.gameState.getFloor() < 7) {
                    this.state = 2;
                    setUpShopping();
                } else {
                    this.state = 5;
                }
            } else if (this.state == 3) {
                this.state = 0;
                gameState.createNewFloor();
                setUpPlay();
            } else if (this.state == 4 || this.state == 5) {
                alpha += dir * 0.01f;
                if (alpha <= 0.1f)
                    dir = 1;
                else if (alpha >= 0.9f)
                    dir = -1;
                this.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Game.exitToMenu();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }

                });
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
        if (state < 4)
            g2.setColor(ColorList.darkTileColor);
        else
            g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, (int) Game.screenSize.getWidth(), (int) Game.screenSize.getHeight());
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
        } else if (state == 4) {
            if (!playedSound) {
                Game.sound.stop();
                Game.sound.playSE("sound\\over.wav");
                playedSound = true;
            }
            g2.setFont(new Font("Monospaced", Font.PLAIN, 100));
            g2.setColor(ColorList.RedColor);
            g2.drawString("You died", (int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("Click to return...", (int) Game.screenSize.getWidth() / 2 - 60,
                    (int) Game.screenSize.getHeight() * 7 / 8);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        } else if (state == 5) {
            if (!playedSound) {
                Game.sound.stop();
                Game.sound.playSE("sound\\win.wav");
                playedSound = true;
            }
            g2.setFont(new Font("Monospaced", Font.PLAIN, 100));
            g2.setColor(ColorList.chosenBackground);
            g2.drawString("Congratulation!", (int) Game.screenSize.getWidth() / 5,
                    (int) Game.screenSize.getHeight() / 2);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("Click to return...", (int) Game.screenSize.getWidth() / 2 - 60,
                    (int) Game.screenSize.getHeight() * 7 / 8);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
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

}
