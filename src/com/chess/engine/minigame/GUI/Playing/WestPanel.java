package com.chess.engine.minigame.GUI.Playing;

import javax.swing.JPanel;

import com.chess.Game;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.cards.Card;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;

public class WestPanel extends JPanel {
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

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Current Deck", (int) Game.screenSize.getWidth() / 9 + 10,
                (int) Game.screenSize.getHeight() / 11);
        for (int i = 0; i < gp.getGameState().getDeck().getCurrentDeck().size(); i++) {
            Card card = gp.getGameState().getDeck().getCurrentDeck().get(i);
            g2.drawImage(Game.imageList.getCardImage(card.getCardType().toString()),
                    (int) Game.screenSize.getWidth() / 35 + (int) Game.screenSize.getWidth() * i / 40,
                    (int) Game.screenSize.getHeight() / 10, (int) Game.screenSize.getWidth() / 40,
                    (int) Game.screenSize.getHeight() / 22, null);
            int cnt = 1;
            for (int j = 0; j < 4; j++) {
                if (card.getHasPower(j)) {
                    g2.drawImage(Game.imageList.getPowerImage(j),
                            (int) Game.screenSize.getWidth() / 35 + (int) Game.screenSize.getWidth() * i / 40,
                            (int) Game.screenSize.getHeight() / 10 + (int) Game.screenSize.getHeight() * cnt / 21,
                            (int) Game.screenSize.getWidth() / 40,
                            (int) Game.screenSize.getHeight() / 22, null);
                    cnt++;
                }
            }
        }
        g2.drawString("Used Cards", (int) Game.screenSize.getWidth() / 9 + 12,
                (int) Game.screenSize.getHeight() / 3 + 40);
        for (int i = 0; i < gp.getGameState().getDeck().getUsedCard().size(); i++) {
            Card card = gp.getGameState().getDeck().getUsedCard().get(i);
            g2.drawImage(Game.imageList.getCardImage(card.getCardType().toString()),
                    (int) Game.screenSize.getWidth() / 35 + (int) Game.screenSize.getWidth() * i / 40,
                    (int) Game.screenSize.getHeight() / 3 + 40, (int) Game.screenSize.getWidth() / 40,
                    (int) Game.screenSize.getHeight() / 22, null);
            int cnt = 1;
            for (int j = 0; j < 4; j++) {
                if (card.getHasPower(j)) {
                    g2.drawImage(Game.imageList.getPowerImage(j),
                            (int) Game.screenSize.getWidth() / 35 + (int) Game.screenSize.getWidth() * i / 40,
                            (int) Game.screenSize.getHeight() / 3 + 40 + (int) Game.screenSize.getHeight() * cnt / 21,
                            (int) Game.screenSize.getWidth() / 40,
                            (int) Game.screenSize.getHeight() / 22, null);
                    cnt++;
                }
            }
        }
    }
}