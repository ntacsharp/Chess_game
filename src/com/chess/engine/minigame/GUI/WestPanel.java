package com.chess.engine.minigame.GUI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.chess.Game;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Deck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WestPanel extends JPanel {
    private static final String PIECE_ICON_PATH = "art\\pieces\\";
    private static final String POWER_ICON_PATH = "art\\other\\power\\";

    // private final MiniTable miniTable;
    // private DeckPanel deckPanel;
    // private DiscardPanel discardPanel;
    private final GamePanel gp;

    public WestPanel(final GamePanel gp) {
        super(new GridLayout(2, 1, 0, 0));
        this.gp = gp;
        setDoubleBuffered(true);
        this.setPreferredSize(new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight()));
        validate();
    }
}

//     public void redraw(){
//         removeAll();
//         this.deckPanel = new DeckPanel();
//         this.discardPanel = new DiscardPanel();
//         this.add(deckPanel);
//         this.add(discardPanel);
//         validate();
//         repaint();
//     }

//     private class DeckPanel extends JPanel {
//         private List<CardPanel> cards = new ArrayList<>();

//         DeckPanel() {
//             super(new FlowLayout(FlowLayout.LEFT, 0, 0));
//             this.setBackground(MiniTable.darkTileColor);
//             setDoubleBuffered(true);
//             JLabel title = new JLabel("Deck");
//             title.setForeground(Color.WHITE);
//             title.setHorizontalAlignment(JLabel.CENTER);
//             title.setVerticalAlignment(JLabel.BOTTOM);
//             title.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
//                     (int) MiniTable.screenSize.getHeight() / 30));
//             title.setFont(new Font("Arial", Font.BOLD, 24));
//             this.add(title);
//             decorateDeck(miniTable.getGameState().getDeck().getCurrentDeck());
//             validate();
//         }

//         private void decorateDeck(final List<Card> currentDeck) {
//             JPanel deck = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//             deck.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
//                     (int) MiniTable.screenSize.getHeight() / 3));
//             deck.setBackground(MiniTable.darkTileColor);
//             for (Card card : currentDeck) {
//                 final CardPanel cardPanel = new CardPanel(card);
//                 this.cards.add(cardPanel);
//                 deck.add(cardPanel);
//             }
//             this.add(deck);
//         }
//     }

//     private class DiscardPanel extends JPanel {
//         private List<CardPanel> cards = new ArrayList<>();

//         DiscardPanel() {
//             this.setBackground(MiniTable.darkTileColor);
//             JLabel title = new JLabel("Discard");
//             setDoubleBuffered(true);
//             title.setForeground(Color.WHITE);
//             title.setHorizontalAlignment(JLabel.CENTER);
//             title.setVerticalAlignment(JLabel.BOTTOM);
//             title.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
//                     (int) MiniTable.screenSize.getHeight() / 30));
//             title.setFont(new Font("Arial", Font.BOLD, 24));
//             this.add(title);
//             decorateDiscard(miniTable.getGameState().getDeck());
//             validate();
//         }

//         private void decorateDiscard(final Deck deck) {
//             JPanel discard = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//             discard.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
//                     (int) MiniTable.screenSize.getHeight() / 3));
//             discard.setBackground(MiniTable.darkTileColor);
//             for (Card card : deck.getTotalDeck()) {
//                 if (!deck.getCurrentDeck().contains(card) && !deck.getHand().contains(card)) {
//                     final CardPanel cardPanel = new CardPanel(card);
//                     this.cards.add(cardPanel);
//                     discard.add(cardPanel);
//                 }
//             }
//             this.add(discard);
//         }
//     }

//     private class CardPanel extends JPanel {

//         CardPanel(final Card card) {
//             super(new GridLayout(6, 1, 0, 0));
//             this.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 36,
//                     (int) MiniTable.screenSize.getHeight() / 3));
                    
//             this.setBackground(MiniTable.darkTileColor);
//             setDoubleBuffered(true);
//             try {
//                 ImageIcon pieceIcon = new ImageIcon(ImageIO.read(new File(PIECE_ICON_PATH
//                         + "W" + card.getCardType().toString() + ".png")));
//                 JLabel pieceLabel = new JLabel(
//                         new ImageIcon(pieceIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 31,
//                                 (int) MiniTable.screenSize.getHeight() / 19, Image.SCALE_SMOOTH)));
//                 pieceLabel.setHorizontalAlignment(JLabel.CENTER);
//                 pieceLabel.setVerticalAlignment(JLabel.TOP);
//                 add(pieceLabel);
//                 for (int i = 0; i < 4; i++) {
//                     if (card.getHasPower(i)) {
//                         ImageIcon powerIcon = new ImageIcon(
//                                 ImageIO.read(new File(POWER_ICON_PATH + card.getPowerByID(i).toString() + ".png")));
//                         JLabel powerLabel = new JLabel(new ImageIcon(
//                                 powerIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 36,
//                                         (int) MiniTable.screenSize.getHeight() / 20, Image.SCALE_SMOOTH)));
//                         powerLabel.setHorizontalAlignment(JLabel.CENTER);
//                         powerLabel.setVerticalAlignment(JLabel.TOP);
//                         add(powerLabel);
//                     }
//                 }
//             } catch (IOException exception) {
//                 exception.printStackTrace();
//             }
//         }
//     }
// }
