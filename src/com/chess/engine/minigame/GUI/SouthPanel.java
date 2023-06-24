package com.chess.engine.minigame.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Deck;

public class SouthPanel extends JPanel {
    private static final Color cardBackground = new Color(217, 231, 236);

    private static final String PIECE_ICON_PATH = "art\\pieces\\";
    private static final String POWER_ICON_PATH = "art\\other\\power\\";
    private static final String OTHER_ICON_PATH = "art\\other\\";

    private final MiniTable miniTable;
    private HandPanel handPanel;
    private StatusPanel statusPanel;
    private SkipPanel skipPanel;

    public SouthPanel(final MiniTable miniTable) {
        super(new BorderLayout());
        this.miniTable = miniTable;
        this.setBackground(MiniTable.darkTileColor);
        this.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth(), (int) MiniTable.screenSize.getHeight() / 3));
        JPanel stringPanel = new StringPanel();
        this.handPanel = new HandPanel();
        this.statusPanel = new StatusPanel();
        this.skipPanel = new SkipPanel();
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(handPanel, BorderLayout.CENTER);
        this.add(stringPanel, BorderLayout.WEST);
        this.add(skipPanel, BorderLayout.EAST);
        validate();
    }

    public void redraw(){
        removeAll();
        JPanel stringPanel = new StringPanel();
        this.handPanel = new HandPanel();
        this.statusPanel = new StatusPanel();
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(handPanel, BorderLayout.CENTER);
        this.add(stringPanel, BorderLayout.WEST);
        this.add(skipPanel, BorderLayout.EAST);
        validate();
        repaint();
    }

    private class HandPanel extends JPanel {
        final List<CardPanel> handCards;

        HandPanel() {
            super(new FlowLayout(FlowLayout.CENTER, 0, 0));
            this.handCards = new ArrayList<CardPanel>();
            for (int i = 0; i < miniTable.getGameState().getDeck().getHand().size(); i++) {
                final CardPanel cardPanel = new CardPanel(this, miniTable.getGameState().getDeck().getHand().get(i));
                this.handCards.add(cardPanel);
                add(cardPanel);
            }
            setBackground(MiniTable.darkTileColor);
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3, (int) MiniTable.screenSize.getHeight() / 3));
            validate();
        }

        public void drawHand(Deck deck) {
            removeAll();
            for (CardPanel cardPanel : handCards) {
                cardPanel.drawCard(deck);
                add(cardPanel);
            }
            validate();
            repaint();
        }
    }

    private class CardPanel extends JPanel {
        private final Card card;

        CardPanel(final HandPanel handPanel, final Card card) {
            super(new BorderLayout());
            this.card = card;
            setBackground(cardBackground);
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 9, (int) MiniTable.screenSize.getHeight() / 4));
            decorateCard();
            highlightChosenCard();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        miniTable.setChosenCard(null);
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        miniTable.setChosenCard(card);
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            handPanel.drawHand(miniTable.getGameState().getDeck());
                            miniTable.boardPanel.drawBoard(miniTable.getGameState().getChessBoard());
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    miniTable.eastPanel.redraw(card);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                }
            });
            validate();
        }

        private void decorateCard() {
            this.removeAll();
            try {
                final BufferedImage pieceImage = ImageIO.read(new File(PIECE_ICON_PATH
                        + "W" + this.card.getCardType().toString() + ".png"));
                final ImageIcon pieceIcon = new ImageIcon(pieceImage);
                add(new JLabel(new ImageIcon(pieceIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 16,
                        (int) MiniTable.screenSize.getHeight() / 8, Image.SCALE_SMOOTH))), BorderLayout.CENTER);
                JLabel powerLabel = new JLabel();
                powerLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
                powerLabel.setPreferredSize(
                        new Dimension((int) MiniTable.screenSize.getWidth() / 10, (int) MiniTable.screenSize.getHeight() / 20));
                for (int i = 0; i < 4; i++) {
                    if (this.card.getHasPower(i)) {
                        BufferedImage powerImage = ImageIO
                                .read(new File(POWER_ICON_PATH + this.card.getPowerByID(i).toString() + ".png"));
                        final ImageIcon icon = new ImageIcon(powerImage);
                        powerLabel.add(new JLabel(
                                new ImageIcon(icon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 41,
                                        (int) MiniTable.screenSize.getHeight() / 23, Image.SCALE_SMOOTH))));
                    }
                }
                add(powerLabel, BorderLayout.NORTH);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        private void highlightChosenCard() {
            if (miniTable.getChosenCard() != null && miniTable.getChosenCard().equals(card)) {
                setBorder(new LineBorder(MiniTable.chosenBackground, 5));
            } else {
                setBorder(new LineBorder(Color.WHITE, 5));
            }
        }

        public void drawCard(Deck deck) {
            setBackground(cardBackground);
            decorateCard();
            highlightChosenCard();
            validate();
            repaint();
        }
    }

    private class StatusPanel extends JPanel {
        StatusPanel() {
            super(new FlowLayout());
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3, (int) MiniTable.screenSize.getHeight() / 12));
            setBackground(MiniTable.darkTileColor);
            add(new HeartPanel());
            add(new MovePanel());
            validate();
        }
    }

    private class MovePanel extends JPanel {
        MovePanel() {
            super(new FlowLayout(FlowLayout.RIGHT));
            setBackground(MiniTable.darkTileColor);
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 6, (int) MiniTable.screenSize.getHeight() / 12));
            decorateMovePanel(miniTable.getGameState());
            validate();
        }

        private void decorateMovePanel(final GameState gameState) {
            try {
                final ImageIcon moveIcon = new ImageIcon(ImageIO.read(new File(POWER_ICON_PATH + "c.png")));
                for (int i = 0; i < gameState.getMoveLeft(); i++) {
                    add(new JLabel(
                            new ImageIcon(moveIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth() / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private class HeartPanel extends JPanel {
        HeartPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 6, (int) MiniTable.screenSize.getHeight() / 12));
            setBackground(MiniTable.darkTileColor);
            decorateHeartPanel(miniTable.getGameState());
            validate();
        }

        private void decorateHeartPanel(final GameState gameState) {
            try {
                final ImageIcon heartIcon = new ImageIcon(ImageIO.read(new File(OTHER_ICON_PATH + "heart.png")));
                final ImageIcon brokenHeartIcon = new ImageIcon(
                        ImageIO.read(new File(OTHER_ICON_PATH + "bheart.png")));
                final ImageIcon shieldIcon = new ImageIcon(ImageIO.read(new File(POWER_ICON_PATH + "s.png")));
                for (int i = 1; i <= gameState.getMaxHealth(); i++) {
                    if (i <= gameState.getCurrentHealth()) {
                        add(new JLabel(
                                new ImageIcon(heartIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth() / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
                    } else {
                        add(new JLabel(
                                new ImageIcon(
                                        brokenHeartIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth() / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
                    }
                }
                for (int i = 0; i < gameState.getShield(); i++) {
                    add(new JLabel(
                            new ImageIcon(shieldIcon.getImage().getScaledInstance((int)MiniTable.screenSize.getWidth() / 38, (int)MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH))));
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private class SkipPanel extends JPanel {
        SkipPanel() {
            super(new FlowLayout(FlowLayout.LEFT, 20, 0));
            setBackground(MiniTable.darkTileColor);
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3, (int) MiniTable.screenSize.getHeight() / 3));
            try {
                final ImageIcon skipIcon = new ImageIcon(ImageIO.read(new File(OTHER_ICON_PATH + "skip.png")));
                final JLabel skip = new JLabel(
                        new ImageIcon(skipIcon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH)));
                skip.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        miniTable.skip();
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
                add(skip);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            validate();
        }
    }

    private class StringPanel extends JPanel {
        StringPanel() {
            super(new GridBagLayout());
            setBackground(MiniTable.darkTileColor);
            setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3, (int) MiniTable.screenSize.getHeight() / 3));
            GridBagConstraints gbc = new GridBagConstraints();
            JLabel label = new JLabel("<html>Chess<br>Floor " + miniTable.getGameState().getFloor() + " of 7</html>");
            label.setForeground(Color.WHITE);
            label.setVerticalAlignment(JLabel.BOTTOM);
            label.setHorizontalAlignment(JLabel.LEFT);
            label.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3, (int) MiniTable.screenSize.getHeight() / 20));
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.SOUTH;
            gbc.weighty = 1;
            add(label, gbc);
            validate();
        }
    }
}
