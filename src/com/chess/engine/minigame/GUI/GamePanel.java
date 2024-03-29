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
import com.chess.Game;
import com.chess.History;

public class GamePanel extends JPanel implements Runnable {
    public History history = new History(this);

    private final MouseListener msln = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getX() >= Game.screenSize.getWidth() / 2 - 145 && e.getX() <= Game.screenSize.getWidth() / 2 + 155 && e.getY() >= (int) Game.screenSize.getHeight() * 2 / 3 - 50 && e.getY() <= (int) Game.screenSize.getHeight() * 2 / 3){
                System.exit(0);
            }

            if (e.getX() >= (int) Game.screenSize.getWidth() / 2 - 125 && e.getX() <= (int) Game.screenSize.getWidth() / 2 + 125 && e.getY() >= (int) Game.screenSize.getHeight() * 3 / 4 - 30 && e.getY() <= (int) Game.screenSize.getHeight() * 3 / 4 + 20){
                History.abandon();
                gameThread = null;
                Game.exitToMenu();
            }

            if (e.getX() >= (int) Game.screenSize.getWidth() / 2 - 65 && e.getX() <= (int) Game.screenSize.getWidth() / 2 + 55 && e.getY() >= (int) Game.screenSize.getHeight() * 7 / 8 - 10 && e.getY() <= (int) Game.screenSize.getHeight() * 7 / 8 + 40) {
                isPausing = false;
            }

            if (e.getX() >= (int) Game.screenSize.getWidth() / 3 - 20 && e.getX() <= (int) Game.screenSize.getWidth() * 2/ 3 + 20 && e.getY() >= (int) Game.screenSize.getHeight() / 6 - 10 && e.getY() <= (int) Game.screenSize.getHeight() / 6 + 35) {
                if (e.getX() < (int) Game.screenSize.getWidth() / 3)
                    Game.sound.setBGVolume(0);
                else if (e.getX() > (int) Game.screenSize.getWidth() * 2 / 3)
                    Game.sound.setBGVolume(100);
                else {
                    int scale = (e.getX() - (int) Game.screenSize.getWidth() / 3) * 300 / (int) Game.screenSize.getWidth();
                    Game.sound.setBGVolume(scale);
                }
            }

            if (e.getX() >= (int) Game.screenSize.getWidth() / 3 - 20 && e.getX() <= (int) Game.screenSize.getWidth() * 2/ 3 + 20 && e.getY() >= (int) Game.screenSize.getHeight() / 3 + 30 && e.getY() <= (int) Game.screenSize.getHeight() / 3 + 75) {
                if (e.getX() < (int) Game.screenSize.getWidth() / 3)
                    Game.sound.setSEVolume(0);
                else if (e.getX() > (int) Game.screenSize.getWidth() * 2 / 3)
                    Game.sound.setSEVolume(100);
                else {
                    int scale = (e.getX() - (int) Game.screenSize.getWidth() / 3) * 300 / (int) Game.screenSize.getWidth();
                    Game.sound.setSEVolume(scale);
                }
            }
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

    };

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

    public Thread gameThread;

    private double timeToChangeState;

    private float alpha = 0.0f;
    private int dir = 1;
    private boolean playedSound = false, isPausing = false, added = false;
    private int state = 0;
    // 0 - playing
    // 1 - displaying "victory"
    // 2 - shopping
    // 3 - display floor
    // 4 - Defeated
    // 5 - Win

    public GamePanel() {
        this.gameState = new GameState(history);
        history.load();
        this.setPreferredSize(Game.screenSize);
        this.setLayout(new BorderLayout());
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        Game.sound.playBG();
        this.added = false;
        validate();
        setUpPlay();
        //setUpShopping();
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

    public void stopGameThread() {
        gameThread = null;
    }

    public void update() {
        if (System.nanoTime() >= timeToChangeState) {
            if (gameState.getCurrentHealth() <= 0) {
                this.state = 4;
            }
            if (this.state == 4 || this.state == 5) {
                alpha += dir * 0.01f;
                if (alpha <= 0.1f)
                    dir = 1;
                else if (alpha >= 0.9f)
                    dir = -1;
                if(!added){
                    added = true;
                    this.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            stopGameThread();
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
            if (this.state == 1) {
                if (this.gameState.getFloor() < 2) {
                    this.state = 2;
                    setUpShopping();
                } else {
                    this.added = false;
                    this.state = 5;
                }
            } else if (this.state == 3) {
                this.state = 0;
                gameState.createNewFloor();
                setUpPlay();
            }
        }
        if (state == 0 || state == 1) {
            this.boardPanel.update();
            this.southPanel.update();
            this.northPanel.update();
        } else if (state == 2) {
            this.shoppingBoardPanel.update();
            this.shoppingNorthPanel.update();
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
                History.abandon();
                Game.sound.stop();
                Game.sound.playSE("/sound/over.wav");
                playedSound = true;
            }
            g2.setFont(new Font("Monospaced", Font.PLAIN, 100));
            g2.setColor(ColorList.transparentRedColor);
            g2.drawString("You died", (int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("Click to return...", (int) Game.screenSize.getWidth() / 2 - 60,
                    (int) Game.screenSize.getHeight() * 7 / 8);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        } else if (state == 5) {
            if (!playedSound) {
                History.abandon();
                Game.sound.stop();
                Game.sound.playSE("/sound/win.wav");
                playedSound = true;
            }
            g2.setFont(new Font("Monospaced", Font.PLAIN, 100));
            g2.setColor(ColorList.chosenBackground);
            g2.drawString("Congratulation!", (int) Game.screenSize.getWidth() / 5,
                    (int) Game.screenSize.getHeight() / 2);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("Click to return...", (int) Game.screenSize.getWidth() / 2 - 60,
                    (int) Game.screenSize.getHeight() * 7 / 8);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
        if (isPausing) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.setColor(ColorList.darkTileColor);
            g2.fillRect(0, 0, (int) Game.screenSize.getWidth(), (int) Game.screenSize.getHeight());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2.setColor(ColorList.lightBorderColor);
            g2.fillRect((int) Game.screenSize.getWidth() / 3,
                    (int) Game.screenSize.getHeight() / 6 + 10, (int) Game.screenSize.getWidth() / 3, 5);
            g2.fillRect((int) Game.screenSize.getWidth() / 3,
                    (int) Game.screenSize.getHeight() / 3 + 50, (int) Game.screenSize.getWidth() / 3, 5);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setColor(ColorList.chosenBackground);
            g2.fillRect((int) Game.screenSize.getWidth() / 3,
                    (int) Game.screenSize.getHeight() / 6 + 10,
                    (int) Game.screenSize.getWidth() * Game.sound.getBgVolumeScale() / 300, 5);
            g2.fillRect((int) Game.screenSize.getWidth() / 3,
                    (int) Game.screenSize.getHeight() / 3 + 50,
                    (int) Game.screenSize.getWidth() * Game.sound.getSeVolumeScale() / 300, 5);
            g2.setFont(new Font("Serif", Font.BOLD, 26));
            g2.drawString("Music Volume", (int) Game.screenSize.getWidth() / 2 - 85,
                    (int) Game.screenSize.getHeight() / 6 - 10);
            g2.drawString("Effect Volume", (int) Game.screenSize.getWidth() / 2 - 87,
                    (int) Game.screenSize.getHeight() / 3 + 30);
            g2.setFont(new Font("Serif", Font.BOLD, 40));
            g2.setColor(ColorList.chosenBackground);
//            g2.fillRect((int) Game.screenSize.getWidth() / 2 - 125, (int) Game.screenSize.getHeight() * 3 / 4 - 30, 240, 50);
            g2.drawString("Exit to Desktop", (int) Game.screenSize.getWidth() / 2 - 135,
                    (int) Game.screenSize.getHeight() * 2 / 3 - 10);
            g2.drawString("Abandon run", (int) Game.screenSize.getWidth() / 2 - 115,
                    (int) Game.screenSize.getHeight() * 3 / 4);
            g2.setFont(new Font("Serif", Font.BOLD, 45));
            g2.drawString("Back", (int) Game.screenSize.getWidth() / 2 - 55,
                    (int) Game.screenSize.getHeight() * 7 / 8 + 30);
        }
        g2.dispose();
    }

    public boolean isPausing() {
        return isPausing;
    }

    public void setPausing(boolean isPausing) {

        if (isPausing == true) {
            this.addMouseListener(msln);
        } else {
            this.removeMouseListener(msln);
        }
        this.isPausing = isPausing;
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
