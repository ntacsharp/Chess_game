package com.chess.engine.minigame.GUI.Playing;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chess.Game;
import com.chess.engine.minigame.GUI.ColorList;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Card.CardType;

public class SouthPanel extends JPanel {

    // private final MiniTable miniTable;
    private HandPanel handPanel;
    // private StatusPanel statusPanel;
    private SkipPanel skipPanel;
    private FloorPanel floorPanel;
    private final GamePanel gp;

    public SouthPanel(final GamePanel gp) {
        super(new BorderLayout());
        this.gp = gp;
        setDoubleBuffered(true);
        this.setPreferredSize(new Dimension((int) Game.screenSize.getWidth(), (int) Game.screenSize.getHeight() / 3));
        this.floorPanel = new FloorPanel();
        this.handPanel = new HandPanel();
        this.skipPanel = new SkipPanel();
        this.add(floorPanel, BorderLayout.WEST);
        this.add(handPanel, BorderLayout.CENTER);
        this.add(skipPanel, BorderLayout.EAST);
        validate();
    }

    public void draw(Graphics2D g2) {
        floorPanel.draw(g2);
        handPanel.draw(g2);
        skipPanel.draw(g2);
    }

    public void update() {
        handPanel.update();
        skipPanel.update();
    }

    private class HandPanel extends JPanel {
        public List<CardPanel> cardPanels = new ArrayList<>();

        HandPanel() {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            this.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 3));
            for (int i = 0; i < gp.getGameState().getDeck().getHand().size(); i++) {
                Card card = gp.getGameState().getDeck().getHand().get(i);
                CardPanel cardPanel = new CardPanel(this, card, i);
                cardPanels.add(cardPanel);
                add(cardPanel);
            }
            validate();
        }

        public void update() {
            if (this.cardPanels.size() == 3) {
                boolean flag = true;
                for (CardPanel cardPanel : this.cardPanels) {
                    if (cardPanel.getCard().getCardType() != CardType.PAWN) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    gp.getGameState().getDeck().promote();
            }
            for (int i = 0; i < this.cardPanels.size(); i++) {
                CardPanel cardPanel = cardPanels.get(i);
                Card thisCard = cardPanel.getCard();
                if (!gp.getGameState().getDeck().getHand().contains(thisCard)) {
                    this.cardPanels.remove(cardPanel);
                    this.remove(cardPanel);
                    revalidate();
                    repaint();
                    i--;
                }
            }
            for (Card card : gp.getGameState().getDeck().getHand()) {
                boolean flag = true;
                for (CardPanel cardPanel : cardPanels) {
                    if (cardPanel.getCard().equals(card)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    CardPanel cardPanel = new CardPanel(this, card, cardPanels.size());
                    cardPanels.add(cardPanel);
                    add(cardPanel);
                    revalidate();
                    repaint();
                }
            }
            for (int i = 0; i < this.cardPanels.size(); i++) {
                cardPanels.get(i).setNewId(i);
            }
            for (CardPanel cardPanel : cardPanels) {
                cardPanel.update();
            }
        }

        public void draw(Graphics2D g2) {
            if (gp.getState() == 0) {
                for (int i = 0; i < gp.getGameState().getMaxHealth() + gp.getGameState().getShield(); i++) {
                    if (i < gp.getGameState().getCurrentHealth()) {
                        g2.drawImage(Game.imageList.getHeartImage(), (int) Game.screenSize.getWidth() / 3 + i * 30,
                                (int) Game.screenSize.getHeight() * 2 / 3 + 15, 40, 40, null);
                    } else if (i < gp.getGameState().getMaxHealth()) {
                        g2.drawImage(Game.imageList.getBheartImage(), (int) Game.screenSize.getWidth() / 3 + i * 30,
                                (int) Game.screenSize.getHeight() * 2 / 3 + 15, 40, 40, null);
                    } else {
                        g2.drawImage(Game.imageList.getShieldImage(), (int) Game.screenSize.getWidth() / 3 + i * 30,
                                (int) Game.screenSize.getHeight() * 2 / 3 + 15, 40, 40, null);
                    }
                }
                for (int i = 0; i < gp.getGameState().getMoveLeft(); i++) {
                    g2.drawImage(Game.imageList.getMoveImage(), (int) Game.screenSize.getWidth() * 2 / 3 - i * 30 - 40,
                            (int) Game.screenSize.getHeight() * 2 / 3 + 15, 40, 40, null);
                }
                for (CardPanel cardPanel : this.cardPanels) {
                    cardPanel.draw(g2);
                }
            } else {
                g2.setColor(ColorList.chosenBackground);
                g2.setFont(new Font("arial", Font.BOLD, 50));
                g2.drawString("Victory", (int) Game.screenSize.getWidth() / 2 - 80,
                        (int) Game.screenSize.getHeight() * 4 / 5);
            }
        }
    }

    private class CardPanel extends JPanel {
        private Card card;
        private int oldId, newId;
        private int oldX, newX;
        private boolean isEntered = false;

        private final MouseListener msln = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    gp.setChosenCard(card);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    gp.setChosenCard(null);
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
                gp.eastPanel.setObjEntered(card);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isEntered = false;
            }
        };

        CardPanel(final HandPanel hp, final Card card, final int newId) {
            super(new FlowLayout(FlowLayout.LEFT));
            this.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 9, (int) Game.screenSize.getHeight() / 3));
            this.card = card;
            this.oldId = newId;
            this.newId = newId;
            this.oldX = (int) Game.screenSize.getWidth() / 3 + newId * (int) Game.screenSize.getWidth() / 9;
            this.newX = (int) Game.screenSize.getWidth() / 3 + newId * (int) Game.screenSize.getWidth() / 9;
        }

        public Card getCard() {
            return this.card;
        }

        public void setNewId(final int newId) {
            this.newId = newId;
            this.newX = (int) Game.screenSize.getWidth() / 3 + newId * (int) Game.screenSize.getWidth() / 9;
        }

        public void update() {
            if (this.getMouseListeners().length == 0 && !gp.isPausing())
                this.addMouseListener(msln);
            if (this.getMouseListeners().length > 0 && gp.isPausing())
                this.removeMouseListener(msln);
            if (this.oldX > this.newX)
                this.oldX -= 4;
            else {
                this.oldX = this.newX;
                this.oldId = this.newId;
            }
        }

        public void draw(Graphics2D g2) {
            if (this.card.equals(gp.getChosenCard())) {
                g2.setColor(ColorList.chosenBackground);
                g2.fillRect(this.oldX, (int) Game.screenSize.getHeight() * 3 / 4 - 20,
                        (int) Game.screenSize.getWidth() / 9 + 1,
                        (int) Game.screenSize.getHeight() / 4);
                g2.setColor(ColorList.cardBackground);
                g2.fillRect(this.oldX + 5, (int) Game.screenSize.getHeight() * 3 / 4 - 15,
                        (int) Game.screenSize.getWidth() / 9 - 9,
                        (int) Game.screenSize.getHeight() / 4 - 10);
                g2.drawImage(Game.imageList.getCardImage(this.card.getCardType().toString()),
                        this.oldX + 5, (int) Game.screenSize.getHeight() * 3 / 4,
                        (int) Game.screenSize.getWidth() / 10,
                        (int) Game.screenSize.getHeight() / 6,
                        null);
                int cnt = 0;
                for (int i = 0; i < 4; i++) {
                    if (card.getHasPower(i)) {
                        g2.drawImage(Game.imageList.getPowerImage(i), this.oldX + 8 + cnt * 35,
                                (int) Game.screenSize.getHeight() * 15 / 16 - 10, 30, 30, null);
                        cnt++;
                    }
                }
            } else if (!isEntered) {
                g2.setColor(Color.WHITE);
                g2.fillRect(this.oldX, (int) Game.screenSize.getHeight() * 3 / 4,
                        (int) Game.screenSize.getWidth() / 9 + 1,
                        (int) Game.screenSize.getHeight() / 4);
                g2.setColor(ColorList.cardBackground);
                g2.fillRect(this.oldX + 5, (int) Game.screenSize.getHeight() * 3 / 4 + 5,
                        (int) Game.screenSize.getWidth() / 9 - 9,
                        (int) Game.screenSize.getHeight() / 4 - 10);
                g2.drawImage(Game.imageList.getCardImage(this.card.getCardType().toString()),
                        this.oldX + 5, (int) Game.screenSize.getHeight() * 3 / 4 + 20,
                        (int) Game.screenSize.getWidth() / 10,
                        (int) Game.screenSize.getHeight() / 6,
                        null);
                int cnt = 0;
                for (int i = 0; i < 4; i++) {
                    if (card.getHasPower(i)) {
                        g2.drawImage(Game.imageList.getPowerImage(i), this.oldX + 8 + cnt * 35,
                                (int) Game.screenSize.getHeight() * 15 / 16 + 10, 30, 30, null);
                        cnt++;
                    }
                }
            } else {
                g2.setColor(Color.WHITE);
                g2.fillRect(this.oldX, (int) Game.screenSize.getHeight() * 3 / 4 - 20,
                        (int) Game.screenSize.getWidth() / 9 + 1,
                        (int) Game.screenSize.getHeight() / 4);
                g2.setColor(ColorList.cardBackground);
                g2.fillRect(this.oldX + 5, (int) Game.screenSize.getHeight() * 3 / 4 - 15,
                        (int) Game.screenSize.getWidth() / 9 - 9,
                        (int) Game.screenSize.getHeight() / 4 - 10);
                g2.drawImage(Game.imageList.getCardImage(this.card.getCardType().toString()),
                        this.oldX + 5, (int) Game.screenSize.getHeight() * 3 / 4,
                        (int) Game.screenSize.getWidth() / 10,
                        (int) Game.screenSize.getHeight() / 6,
                        null);
                int cnt = 0;
                for (int i = 0; i < 4; i++) {
                    if (card.getHasPower(i)) {
                        g2.drawImage(Game.imageList.getPowerImage(i), this.oldX + 8 + cnt * 35,
                                (int) Game.screenSize.getHeight() * 15 / 16 - 10, 30, 30, null);
                        cnt++;
                    }
                }
            }
        }
    }

    private class FloorPanel extends JPanel {
        FloorPanel() {
            super(new GridBagLayout());
            this.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 3));
            validate();
        }

        public void draw(Graphics2D g2) {
            g2.setColor(new Color(255, 255, 255, 123));
            g2.drawString("My Chess", 20, (int) Game.screenSize.getHeight() - 50);
            g2.drawString("Floor " + gp.getGameState().getFloor() + " on 7", 20,
                    (int) Game.screenSize.getHeight() - 20);
        }
    }

    private class SkipPanel extends JPanel {
        private boolean isEntered = false;
        private final MouseListener msln = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gp.boardPanel.endTurn();
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
                gp.eastPanel.setObjEntered("skip");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isEntered = false;
            }

        };

        SkipPanel() {
            super(new FlowLayout(FlowLayout.LEFT, 20, (int) Game.screenSize.getHeight() / 6));
            setDoubleBuffered(true);
            setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 3));
            JLabel skip = new JLabel();
            skip.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 16, (int) Game.screenSize.getHeight() / 8));
            this.add(skip);
            validate();
        }

        private void draw(Graphics2D g2) {
            if (!isEntered) {
                g2.drawImage(Game.imageList.getSkipImage(), (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                        (int) Game.screenSize.getHeight() * 5 / 6, (int) Game.screenSize.getWidth() / 16,
                        (int) Game.screenSize.getHeight() / 8, null);
            } else {
                g2.drawImage(Game.imageList.getSkipImage(), (int) Game.screenSize.getWidth() * 2 / 3 + 10,
                        (int) Game.screenSize.getHeight() * 5 / 6 - 10, (int) Game.screenSize.getWidth() / 16 + 20,
                        (int) Game.screenSize.getHeight() / 8 + 20, null);
            }
        }

        private void update() {
            if (this.getMouseListeners().length == 0 && !gp.isPausing())
                this.addMouseListener(msln);
            if (this.getMouseListeners().length > 0 && gp.isPausing())
                this.removeMouseListener(msln);
        }
    }
}