package com.chess.engine.minigame.GUI.Shopping;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.chess.Game;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.cards.Card;

public class ShoppingEastPanel extends JPanel {
    private Card cardEntered = null;
    private int powerEntered = -1;

    public ShoppingEastPanel(final GamePanel gp) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setDoubleBuffered(true);
        this.setPreferredSize(
                new Dimension(new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight())));
        validate();
    }

    public void setCardEntered(final Card card) {
        this.cardEntered = card;
    }

    public void setPowerEntered(int powerEntered) {
        this.powerEntered = powerEntered;
    }

    public void draw(Graphics2D g2) {
        if (cardEntered != null && powerEntered != -1 && powerEntered < 4) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.drawString("Add a power to this card.", (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                    (int) Game.screenSize.getHeight() / 12 + 20);
            g2.drawImage(Game.imageList.getPowerImage(powerEntered), (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                    (int) Game.screenSize.getHeight() / 12 + 105, 20, 20, null);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString(cardEntered.getCardType().getName(),
                    (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                    (int) Game.screenSize.getHeight() / 12 + 60);
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.drawString(Card.getPowerByID(powerEntered).getDescription(),
                    (int) Game.screenSize.getWidth() * 2 / 3 + 50,
                    (int) Game.screenSize.getHeight() / 12 + 120);
        } else if (cardEntered == null && powerEntered == 4) {
            g2.setFont(new Font("Arial", Font.ITALIC, 18));
            g2.drawString("Increase your health.", (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                    (int) Game.screenSize.getHeight() / 12 + 20);
        }
    }
}
