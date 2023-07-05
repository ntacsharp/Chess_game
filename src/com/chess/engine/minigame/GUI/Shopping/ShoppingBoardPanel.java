package com.chess.engine.minigame.GUI.Shopping;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.chess.Game;
import com.chess.engine.minigame.GUI.ColorList;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.cards.Card;

public class ShoppingBoardPanel extends JPanel {
    private final GamePanel gp;
    private final List<TilePanel> tileList = new ArrayList<>();
    private final List<ChoicePanel> choices = new ArrayList<>();
    private final PlayerPanel playerPanel;

    public ShoppingBoardPanel(final GamePanel gp) {
        super(new GridLayout(5, 5, 0, 0));
        this.setPreferredSize(
                new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getWidth() / 3));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.gp = gp;
        this.playerPanel = new PlayerPanel(this);
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int r, c;
            while (true) {
                r = rand.nextInt(5);
                c = rand.nextInt(5);
                if (r == 4 && c == 2)
                    continue;
                if (r == 0 && c == 2)
                    continue;
                boolean flag = true;
                for (ChoicePanel choicePanel : choices) {
                    if (choicePanel.getR() == r && choicePanel.getC() == c) {
                        flag = false;
                        break;
                    }
                }
                if (!flag)
                    continue;
                break;
            }
            choices.add(new ChoicePanel(this, r, c, i));
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                TilePanel tile = new TilePanel(this, i, j);
                tileList.add(tile);
                add(tile);
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(ColorList.lightBorderColor);
        g2.fillRect((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 12,
                (int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getWidth() / 3);
        g2.setColor(ColorList.darkBorderColor);
        g2.fillRect((int) Game.screenSize.getWidth() / 3 + 5, (int) Game.screenSize.getHeight() / 12 + 5,
                (int) Game.screenSize.getWidth() / 3 - 10, (int) Game.screenSize.getWidth() / 3 - 10);
        g2.fillRect(ALLBITS, ABORT, WIDTH, HEIGHT);
        for (TilePanel tilePanel : tileList) {
            tilePanel.draw(g2);
        }
        float alpha = 0.9f;
        for (int i = 0; i < 4; i++) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int x = (int) Game.screenSize.getWidth() / 3 + 11 + 2 * ((int) Game.screenSize.getWidth() / 15 - 4) + 5 * i;
            int y = (int) Game.screenSize.getHeight() / 12 + 4 * ((int) Game.screenSize.getWidth() / 15 - 4) + 80
                    - i * ((int) Game.screenSize.getWidth() / 60 - 4);
            int width = (int) Game.screenSize.getWidth() / 15 - 6 - 10 * i;
            g2.setColor(ColorList.lightBorderColor);
            g2.fillRect(x, y, width, (int) Game.screenSize.getWidth() / 60 - 4);
            y = (int) Game.screenSize.getHeight() / 12 +  80 - i * ((int) Game.screenSize.getWidth() / 60 - 4);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha + 0.1f));
            g2.setColor(ColorList.lightTileColor);
            g2.fillRect(x, y, width, (int) Game.screenSize.getWidth() / 60 - 4);
            alpha -= 0.2f;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        playerPanel.draw(g2);
        for (ChoicePanel choicePanel : choices) {
            choicePanel.draw(g2);
        }
    }

    public void update() {
        for (int i = 0; i < choices.size(); i++) {
            if(choices.get(i).getCard() != null){
                if (!gp.getGameState().getDeck().getShoppingList().contains(choices.get(i).getCard())) {
                    choices.remove(i);
                    i--;
                }
            }
        }

        for (TilePanel tile : tileList) {
            if (tile.choice != null && !choices.contains(tile.choice))
                tile.choice = null;
            tile.update();
        }
        playerPanel.update();
    }

    private ChoicePanel checkTile(final int r, final int c) {
        for (ChoicePanel choicePanel : choices) {
            if (choicePanel.getR() == r && choicePanel.getC() == c) {
                // gp.shoppingEastPanel.setCardEntered(choicePanel.getCard());
                // gp.shoppingEastPanel.setPowerEntered(choicePanel.getPowerID());
                // break;
                return choicePanel;
            }
        }
        return null;
    }

    private class TilePanel extends JPanel {
        private int r, c;
        private boolean isEntered = false;
        private boolean isMovable = true;
        private ChoicePanel choice;
        private final MouseListener msln = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(isMovable);
                    if (isMovable) {
                        playerPanel.setNewRC(r, c);
                        if (r == 0 && c == 2) {
                            gp.setState(3);
                            gp.setTimeToChangeState(System.nanoTime() + 1000000000);
                        } else {
                            if (choice != null) {
                                if (choice.getCard() != null) {
                                    // gp.getGameState().getDeck().updateCard(choice.getCard(), choice.getPowerID(),
                                    // true);
                                    choice.getCard().setHasPower(choice.getPowerID(), true);
                                    gp.getGameState().getDeck().removeChoice(choice.getCard(), choice.getPowerID());
                                } else {
                                    gp.getGameState().setMaxHealth(gp.getGameState().getMaxHealth() + 1);
                                    gp.getGameState().setCurrentHealth(gp.getGameState().getCurrentHealth() + 1);
                                    choices.remove(choices.size() - 1);
                                }
                                gp.getGameState().setGold(gp.getGameState().getGold() - choice.price);
                                Game.sound.playSE("/sound/buy.wav");
                            }
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
                    isEntered = true;
                    if (choice != null) {
                        gp.shoppingEastPanel.setCardEntered(choice.getCard());
                        gp.shoppingEastPanel.setPowerEntered(choice.getPowerID());
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isEntered = false;
                    gp.shoppingEastPanel.setCardEntered(null);
                    gp.shoppingEastPanel.setPowerEntered(-1);
                }

            };

        TilePanel(final ShoppingBoardPanel sbp, final int r, final int c) {
            super(new FlowLayout());
            this.r = r;
            this.c = c;
            this.choice = checkTile(r, c);
            this.setPreferredSize(new Dimension(85, 85));
            this.isFocusable();
        }

        private void draw(final Graphics2D g2) {
            if (isEntered) {
                g2.setColor(ColorList.lightBorderColor);
            } else {
                g2.setColor(((r + c) % 2 == 0) ? ColorList.darkTileColor : ColorList.lightTileColor);
            }
            int x = (int) Game.screenSize.getWidth() / 3 + 10 + c * ((int) Game.screenSize.getWidth() / 15 - 4);
            int y = (int) Game.screenSize.getHeight() / 12 + 10 + r * ((int) Game.screenSize.getWidth() / 15 - 4);
            g2.fillRect(x, y, (int) Game.screenSize.getWidth() / 15 - 4, (int) Game.screenSize.getWidth() / 15 - 4);
            if (isEntered) {
                g2.setColor(ColorList.lightBorderColor);
            } else {
                g2.setColor(((r + c) % 2 == 0) ? ColorList.darkTileColor : ColorList.lightTileColor);
            }
            x = (int) Game.screenSize.getWidth() / 3 + 15 + c * ((int) Game.screenSize.getWidth() / 15 - 4);
            y = (int) Game.screenSize.getHeight() / 12 + 15 + r * ((int) Game.screenSize.getWidth() / 15 - 4);
            g2.fillRect(x, y, (int) Game.screenSize.getWidth() / 15 - 14, (int) Game.screenSize.getWidth() / 15 - 14);
        }

        private void update() {
            if (this.getMouseListeners().length == 0 && !gp.isPausing())
                this.addMouseListener(msln);
            if (this.getMouseListeners().length > 0 && gp.isPausing())
                this.removeMouseListener(msln);
            if (choice != null && choice.getPrice() > gp.getGameState().getGold())
                this.isMovable = false;
            else this.isMovable = true;
        }
    }

    private class ChoicePanel {
        private final int r, c, powerID, x, y;
        private final Card card;
        private final int price, ind;

        ChoicePanel(final ShoppingBoardPanel sbp, final int r, final int c, final int ind) {
            super();
            this.r = r;
            this.c = c;
            this.ind = ind;
            if (ind == 5) {
                this.powerID = 4;
                this.card = null;
            } else {
                this.powerID = gp.getGameState().getDeck().getPowerIds().get(ind);
                this.card = gp.getGameState().getDeck().getShoppingList().get(ind);
            }
            Random rand = new Random();
            this.price = rand.nextInt(4, 10);
            this.x = (int) Game.screenSize.getWidth() / 3 + 20
                    + c * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.y = (int) Game.screenSize.getHeight() / 12 + 10
                    + r * ((int) Game.screenSize.getWidth() / 15 - 4);
        }

        public int getR() {
            return r;
        }

        public int getC() {
            return c;
        }

        public Card getCard() {
            return card;
        }

        public int getPowerID() {
            return powerID;
        }

        public int getPrice() {
            return price;
        }

        public void draw(Graphics2D g2) {
            if (ind == 5) {
                g2.drawImage(Game.imageList.getHeartImage(), this.x, this.y + 10,
                        (int) Game.screenSize.getWidth() / 15 - 25,
                        (int) Game.screenSize.getWidth() / 15 - 25, null);
            } else {
                g2.drawImage(Game.imageList.getCardImage(card.getCardType().toString()), this.x, this.y + 10,
                        (int) Game.screenSize.getWidth() / 15 - 25,
                        (int) Game.screenSize.getWidth() / 15 - 25, null);
                g2.drawImage(Game.imageList.getPowerImage(powerID), this.x + 50,
                        this.y + (int) Game.screenSize.getHeight() / 12,
                        (int) Game.screenSize.getWidth() / 45,
                        (int) Game.screenSize.getWidth() / 45, null);
            }
            g2.drawImage(Game.imageList.getCoinImage(), this.x - 10, this.y + (int) Game.screenSize.getHeight() / 12,
                    (int) Game.screenSize.getWidth() / 45,
                    (int) Game.screenSize.getWidth() / 45, null);
            if (this.price > gp.getGameState().getGold())
                g2.setColor(Color.red);
            else
                g2.setColor(Color.black);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(Integer.toString(this.price), this.x + 2,
                    this.y + (int) Game.screenSize.getHeight() / 12 + 18);

        }
    }

    private class PlayerPanel {
        private int x, y;
        private int oldR, newR, oldC, newC, oldX, oldY, newX, newY;
        private  boolean playSound = false;

        PlayerPanel(final ShoppingBoardPanel sbp) {
            this.oldC = this.newC = 2;
            this.oldR = this.newR = 4;
            this.oldX = this.x = this.newX = (int) Game.screenSize.getWidth() / 3 + 10
                    + oldC * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.oldY = this.y = this.newY = (int) Game.screenSize.getHeight() / 12 + 10
                    + oldR * ((int) Game.screenSize.getWidth() / 15 - 4);
        }

        public void setNewRC(final int newR, final int newC) {
            this.newR = newR;
            this.newC = newC;
            this.newX = (int) Game.screenSize.getWidth() / 3 + 10
                    + newC * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.newY = (int) Game.screenSize.getHeight() / 12 + 10
                    + newR * ((int) Game.screenSize.getWidth() / 15 - 4);
        }

        public void update() {
            if(this.oldR == this.newR && this.oldC == this.newC && !playSound){
                Game.sound.playSE("/sound/move.wav");
                playSound = true;
            }else  playSound = false;
            if (this.oldR < this.newR) {
                int SpeedY = (this.newY - this.oldY) / 20;
                this.y += SpeedY;
                if (this.y >= this.newY) {
                    this.y = this.newY;
                    this.oldY = this.newY;
                    this.oldR = this.newR;
                }
            } else if (this.oldR > this.newR) {
                int SpeedY = (this.newY - this.oldY) / 20;
                this.y += SpeedY;
                if (this.y <= this.newY) {
                    this.y = this.newY;
                    this.oldY = this.newY;
                    this.oldR = this.newR;
                }
            }
            if (this.oldC < this.newC) {
                int SpeedX = (this.newX - this.oldX) / 20;
                this.x += SpeedX;
                if (this.x >= this.newX) {
                    this.x = this.newX;
                    this.oldX = this.newX;
                    this.oldC = this.newC;
                }
            } else if (this.oldC > this.newC) {
                int SpeedX = (this.newX - this.oldX) / 20;
                this.x += SpeedX;
                if (this.x <= this.newX) {
                    this.x = this.newX;
                    this.oldX = this.newX;
                    this.oldC = this.newC;
                }
            }
        }

        public void draw(Graphics2D g2) {
            g2.drawImage(Game.imageList.getPlayerImage(), this.x, this.y,
                    (int) Game.screenSize.getWidth() / 15 - 4,
                    (int) Game.screenSize.getWidth() / 15 - 4, null);
        }
    }

}
