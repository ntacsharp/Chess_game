package com.chess.engine.minigame.GUI;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.border.LineBorder;

import com.chess.Game;
import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Deck;

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
            for (int i = 0; i < this.cardPanels.size(); i++) {
                CardPanel cardPanel = cardPanels.get(i);
                if (!gp.getGameState().getDeck().getHand().contains(cardPanel.getCard())) {
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
        }
    }

    private class CardPanel extends JPanel {
        private final Card card;
        private final HandPanel hp;
        private int oldId, newId;
        private int oldX, newX;
        private boolean isEntered = false;

        CardPanel(final HandPanel hp, final Card card, final int newId) {
            super(new FlowLayout(FlowLayout.LEFT));
            this.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 9, (int) Game.screenSize.getHeight() / 3));
            this.card = card;
            this.hp = hp;
            this.oldId = newId;
            this.newId = newId;
            this.oldX = (int) Game.screenSize.getWidth() / 3 + newId * (int) Game.screenSize.getWidth() / 9;
            this.newX = (int) Game.screenSize.getWidth() / 3 + newId * (int) Game.screenSize.getWidth() / 9;
            this.addMouseListener(new MouseListener() {

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

            });
        }

        public Card getCard() {
            return this.card;
        }

        public void setNewId(final int newId) {
            this.newId = newId;
            this.newX = (int) Game.screenSize.getWidth() / 3 + newId * (int) Game.screenSize.getWidth() / 9;
        }

        public void update() {
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

        SkipPanel() {
            super(new FlowLayout(FlowLayout.LEFT, 20, (int) Game.screenSize.getHeight() / 6));
            setDoubleBuffered(true);
            setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 3));
            JLabel skip = new JLabel();
            skip.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 16, (int) Game.screenSize.getHeight() / 8));
            skip.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("skip");
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

            });
            this.add(skip);
            validate();
        }

        public void draw(Graphics2D g2) {
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
    }

}

// public void redraw(){
// removeAll();
// JPanel stringPanel = new StringPanel();
// this.handPanel = new HandPanel();
// this.add(statusPanel, BorderLayout.NORTH);
// this.add(handPanel, BorderLayout.CENTER);
// this.add(stringPanel, BorderLayout.WEST);
// this.add(skipPanel, BorderLayout.EAST);
// validate();
// repaint();
// }

// public void redrawStatus(){
// statusPanel.redraw();
// this.add(statusPanel);
// }

// private class HandPanel extends JPanel {
// final List<CardPanel> handCards;

// HandPanel() {
// super(new FlowLayout(FlowLayout.CENTER, 0, 0));
// setDoubleBuffered(true);
// this.handCards = new ArrayList<CardPanel>();
// for (int i = 0; i < miniTable.getGameState().getDeck().getHand().size(); i++)
// {
// final CardPanel cardPanel = new CardPanel(this,
// miniTable.getGameState().getDeck().getHand().get(i));
// this.handCards.add(cardPanel);
// add(cardPanel);
// }
// setBackground(MiniTable.darkTileColor);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
// (int) MiniTable.screenSize.getHeight() / 3));
// validate();
// }

// public void drawHand(Deck deck) {
// removeAll();
// for (CardPanel cardPanel : handCards) {
// cardPanel.drawCard(deck);
// add(cardPanel);
// }
// validate();
// repaint();
// }
// }

// private class CardPanel extends JPanel {
// private final Card card;

// CardPanel(final HandPanel handPanel, final Card card) {
// super(new BorderLayout());
// this.card = card;
// setBackground(cardBackground);
// setDoubleBuffered(true);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 9,
// (int) MiniTable.screenSize.getHeight() / 4));
// decorateCard();
// highlightChosenCard();
// addMouseListener(new MouseListener() {
// @Override
// public void mouseClicked(MouseEvent e) {
// if (SwingUtilities.isRightMouseButton(e)) {
// miniTable.boardPanel.setChosenCard(null);
// } else if (SwingUtilities.isLeftMouseButton(e)) {
// miniTable.boardPanel.setChosenCard(card);
// }
// SwingUtilities.invokeLater(new Runnable() {
// @Override
// public void run() {
// handPanel.drawHand(miniTable.getGameState().getDeck());
// miniTable.boardPanel.drawBoard(miniTable.getGameState().getChessBoard());
// }
// });
// }

// @Override
// public void mousePressed(MouseEvent e) {
// // TODO Auto-generated method stub
// }

// @Override
// public void mouseReleased(MouseEvent e) {
// // TODO Auto-generated method stub
// }

// @Override
// public void mouseEntered(MouseEvent e) {
// miniTable.eastPanel.redraw(card);
// }

// @Override
// public void mouseExited(MouseEvent e) {
// // TODO Auto-generated method stub
// }
// });
// validate();
// }

// private void decorateCard() {
// this.removeAll();
// try {
// final BufferedImage pieceImage = ImageIO.read(new File(PIECE_ICON_PATH
// + "W" + this.card.getCardType().toString() + ".png"));
// final ImageIcon pieceIcon = new ImageIcon(pieceImage);
// add(new JLabel(new ImageIcon(pieceIcon.getImage().getScaledInstance((int)
// MiniTable.screenSize.getWidth() / 16,
// (int) MiniTable.screenSize.getHeight() / 8, Image.SCALE_SMOOTH))),
// BorderLayout.CENTER);
// JLabel powerLabel = new JLabel();
// powerLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
// powerLabel.setPreferredSize(
// new Dimension((int) MiniTable.screenSize.getWidth() / 10, (int)
// MiniTable.screenSize.getHeight() / 20));
// for (int i = 0; i < 4; i++) {
// if (this.card.getHasPower(i)) {
// BufferedImage powerImage = ImageIO
// .read(new File(POWER_ICON_PATH + this.card.getPowerByID(i).toString() +
// ".png"));
// final ImageIcon icon = new ImageIcon(powerImage);
// powerLabel.add(new JLabel(
// new ImageIcon(icon.getImage().getScaledInstance((int)
// MiniTable.screenSize.getWidth() / 41,
// (int) MiniTable.screenSize.getHeight() / 23, Image.SCALE_SMOOTH))));
// }
// }
// add(powerLabel, BorderLayout.NORTH);
// } catch (IOException exception) {
// exception.printStackTrace();
// }
// }

// private void highlightChosenCard() {
// if (miniTable.boardPanel.getChosenCard() != null &&
// miniTable.boardPanel.getChosenCard().equals(card)) {
// setBorder(new LineBorder(MiniTable.chosenBackground, 5));
// } else {
// setBorder(new LineBorder(Color.WHITE, 5));
// }
// }

// public void drawCard(Deck deck) {
// setBackground(cardBackground);
// decorateCard();
// highlightChosenCard();
// validate();
// repaint();
// }
// }

// private class StatusPanel extends JPanel {
// private HeartPanel heartPanel;
// private MovePanel movePanel;
// StatusPanel() {
// super(new FlowLayout());
// setDoubleBuffered(true);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
// (int) MiniTable.screenSize.getHeight() / 12));
// setBackground(MiniTable.darkTileColor);
// heartPanel = new HeartPanel();
// movePanel = new MovePanel();
// add(heartPanel);
// add(movePanel);
// validate();
// }

// public void redraw(){
// removeAll();
// heartPanel.redraw();
// movePanel.redraw();
// add(heartPanel);
// add(movePanel);
// validate();
// repaint();
// }
// }

// private class MovePanel extends JPanel {
// MovePanel() {
// super(new FlowLayout(FlowLayout.RIGHT));
// setDoubleBuffered(true);
// setBackground(MiniTable.darkTileColor);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 6,
// (int) MiniTable.screenSize.getHeight() / 12));
// decorateMovePanel(miniTable.getGameState());
// validate();
// }

// private void decorateMovePanel(final GameState gameState) {
// try {
// final ImageIcon moveIcon = new ImageIcon(ImageIO.read(new
// File(POWER_ICON_PATH + "c.png")));
// for (int i = 0; i < gameState.getMoveLeft(); i++) {
// add(new JLabel(
// new
// ImageIcon(moveIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth()
// / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
// }
// } catch (IOException exception) {
// exception.printStackTrace();
// }
// }

// public void redraw(){
// removeAll();
// decorateMovePanel(miniTable.getGameState());
// validate();
// repaint();
// }
// }

// private class HeartPanel extends JPanel {
// HeartPanel() {
// super(new FlowLayout(FlowLayout.LEFT));
// setDoubleBuffered(true);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 6,
// (int) MiniTable.screenSize.getHeight() / 12));
// setBackground(MiniTable.darkTileColor);
// decorateHeartPanel(miniTable.getGameState());
// validate();
// }

// private void decorateHeartPanel(final GameState gameState) {
// try {
// final ImageIcon heartIcon = new ImageIcon(ImageIO.read(new
// File(OTHER_ICON_PATH + "heart.png")));
// final ImageIcon brokenHeartIcon = new ImageIcon(
// ImageIO.read(new File(OTHER_ICON_PATH + "bheart.png")));
// final ImageIcon shieldIcon = new ImageIcon(ImageIO.read(new
// File(POWER_ICON_PATH + "s.png")));
// for (int i = 1; i <= gameState.getMaxHealth(); i++) {
// if (i <= gameState.getCurrentHealth()) {
// add(new JLabel(
// new
// ImageIcon(heartIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth()
// / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
// } else {
// add(new JLabel(
// new ImageIcon(
// brokenHeartIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth()
// / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
// }
// }
// for (int i = 0; i < gameState.getShield(); i++) {
// add(new JLabel(
// new
// ImageIcon(shieldIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth()
// / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
// }
// } catch (IOException exception) {
// exception.printStackTrace();
// }
// }

// public void redraw(){
// removeAll();
// decorateHeartPanel(miniTable.getGameState());
// validate();
// repaint();
// }
// }

// private class SkipPanel extends JPanel {
// SkipPanel() {
// super(new FlowLayout(FlowLayout.LEFT, 20, 0));
// setDoubleBuffered(true);
// setBackground(MiniTable.darkTileColor);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
// (int) MiniTable.screenSize.getHeight() / 3));
// try {
// final ImageIcon skipIcon = new ImageIcon(ImageIO.read(new
// File(OTHER_ICON_PATH + "skip.png")));
// final JLabel skip = new JLabel(
// new ImageIcon(skipIcon.getImage().getScaledInstance(150, 180,
// Image.SCALE_SMOOTH)));
// skip.addMouseListener(new MouseListener() {
// @Override
// public void mouseClicked(MouseEvent e) {
// miniTable.boardPanel.skip();
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
// add(skip);
// } catch (IOException exception) {
// exception.printStackTrace();
// }
// validate();
// }
// }

// private class StringPanel extends JPanel {
// StringPanel() {
// super(new GridBagLayout());
// setBackground(MiniTable.darkTileColor);
// setDoubleBuffered(true);
// setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
// (int) MiniTable.screenSize.getHeight() / 3));
// GridBagConstraints gbc = new GridBagConstraints();
// JLabel label = new JLabel("<html>Chess<br>Floor " +
// miniTable.getGameState().getFloor() + " of 7</html>");
// label.setForeground(Color.WHITE);
// label.setVerticalAlignment(JLabel.BOTTOM);
// label.setHorizontalAlignment(JLabel.LEFT);
// label.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() /
// 3, (int) MiniTable.screenSize.getHeight() / 20));
// gbc.fill = GridBagConstraints.HORIZONTAL;
// gbc.anchor = GridBagConstraints.SOUTH;
// gbc.weighty = 1;
// add(label, gbc);
// validate();
// }
// }
// }
