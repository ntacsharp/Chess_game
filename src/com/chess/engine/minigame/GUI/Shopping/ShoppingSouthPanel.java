package com.chess.engine.minigame.GUI.Shopping;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.chess.Game;
import com.chess.engine.minigame.GUI.GamePanel;

public class ShoppingSouthPanel extends JPanel {
    private final GamePanel gp;
    private FloorPanel floorPanel;
    private HandPanel handPanel;

    public ShoppingSouthPanel(final GamePanel gp) {
        super(new BorderLayout());
        this.gp = gp;
        setDoubleBuffered(true);
        this.setPreferredSize(new Dimension((int) Game.screenSize.getWidth(), (int) Game.screenSize.getHeight() / 3));
        this.floorPanel = new FloorPanel();
        this.handPanel = new HandPanel();
        JPanel tmpPanel = new JPanel();
        tmpPanel.setPreferredSize(
                new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 3));
        this.add(floorPanel, BorderLayout.WEST);
        this.add(handPanel, BorderLayout.CENTER);
        this.add(tmpPanel, BorderLayout.EAST);
        validate();
    }

    public void draw(Graphics2D g2){
        this.floorPanel.draw(g2);
        this.handPanel.draw(g2);
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
            g2.drawString("Floor " + gp.getGameState().getFloor() + " shopping", 20,
                    (int) Game.screenSize.getHeight() - 20);
        }
    }

    private class HandPanel extends JPanel {

        HandPanel() {
            super(new FlowLayout(FlowLayout.CENTER, 0, 0));
            this.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 3));
            validate();
        }

        public void draw(Graphics2D g2) {
            for (int i = 0; i < gp.getGameState().getMaxHealth(); i++) {
                if (i < gp.getGameState().getCurrentHealth()) {
                    g2.drawImage(Game.imageList.getHeartImage(), (int) Game.screenSize.getWidth() / 3 + i * 30,
                            (int) Game.screenSize.getHeight() * 2 / 3 + 15, 40, 40, null);
                } else {
                    g2.drawImage(Game.imageList.getBheartImage(), (int) Game.screenSize.getWidth() / 3 + i * 30,
                            (int) Game.screenSize.getHeight() * 2 / 3 + 15, 40, 40, null);
                }
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.BOLD, 26));
            g2.drawString("Player can freely move in shopping round", (int) Game.screenSize.getWidth() / 3 - 25, (int) Game.screenSize.getHeight() * 5 / 6);
        }
    }
}
